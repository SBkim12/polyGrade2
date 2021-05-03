package poly.persistance.mongo.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.AggregationOptions;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import poly.dto.MelonDTO;
import poly.dto.MelonRankDTO;
import poly.dto.MelonSingerDTO;
import poly.dto.MelonSongDTO;
import poly.persistance.mongo.IMelonMapper;
import poly.util.CmmUtil;

@Component("MelonMapper")
public class MelonMapper implements IMelonMapper {

	@Autowired
	private MongoTemplate mongodb;

	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public boolean createCollection(String colNm) throws Exception {

		log.info(this.getClass().getName() + ".createCollection Start!");

		boolean res = false;

		// 기존에 등록된 컬렉션 이름이 존재하는지 체크하고, 존재하면 기존 컬렉션 삭제함
		if (mongodb.collectionExists(colNm)) {
			mongodb.dropCollection(colNm); // 기존 컬렉션 삭제

		}

		// 컬렉션 생성 및 인덱스 생성, MongoDB에서 데이터 가져오는 방식에 맞게 인덱스는 반드시 생성하자!
		// 데이터 양이 많지 않으면 문제되지 않으나, 최소 10만건 이상 데이터 저장시 속도가 약 10배 이상 발생함
		mongodb.createCollection(colNm).createIndex(new BasicDBObject("collect_time", 1).append("rank", 1), "rankIdx");

		res = true;

		log.info(this.getClass().getName() + ".createCollection End!");

		return res;
	}

	@Override
	public int insertRank(List<MelonDTO> pList, String colNm) throws Exception {

		log.info(this.getClass().getName() + ".insertRank Start!");

		int res = 0;

		if (pList == null) {
			pList = new ArrayList<MelonDTO>();
		}

		Iterator<MelonDTO> it = pList.iterator();

		while (it.hasNext()) {
			MelonDTO pDTO = (MelonDTO) it.next();

			if (pDTO == null) {
				pDTO = new MelonDTO();
			}

			mongodb.insert(pDTO, colNm);

		}

		res = 1;

		log.info(this.getClass().getName() + ".insertRank End!");

		return res;
	}

	@Override
	public List<MelonDTO> getRank(String colNm) throws Exception {

		log.info(this.getClass().getName() + ".getRank Start!");

		DBCollection rCol = mongodb.getCollection(colNm);

		Iterator<DBObject> cursor = rCol.find();

		List<MelonDTO> rList = new ArrayList<MelonDTO>();

		MelonDTO rDTO = null;

		while (cursor.hasNext()) {

			rDTO = new MelonDTO();

			final DBObject current = cursor.next();

			String collect_time = CmmUtil.nvl((String) current.get("collect_time"));
			String rank = CmmUtil.nvl((String) current.get("rank"));
			String song = CmmUtil.nvl((String) current.get("song"));
			String singer = CmmUtil.nvl((String) current.get("singer"));
			String album = CmmUtil.nvl((String) current.get("album"));

			rDTO.setCollect_time(collect_time);
			rDTO.setRank(rank);
			rDTO.setSong(song);
			rDTO.setSinger(singer);
			rDTO.setAlbum(album);

			rList.add(rDTO);

			rDTO = null;

		}

		log.info(this.getClass().getName() + ".getRank End!");

		return rList;
	}

	@Override
	public List<MelonSingerDTO> getRankForSinger(String colNm) throws Exception {
		log.info(this.getClass().getName() + ".getRankForSinger Start!");

		// 데이터를 가져올 컬렉션 선택
		DBCollection rCol = mongodb.getCollection(colNm);

		// 쿼리 만들기
		List<DBObject> pipeline = Arrays.asList(
				// SQL의 Group by와 같은 역할을 수행하는 MongoDB group 함수 호출
				new BasicDBObject().append("$group",
						new BasicDBObject().append("_id", new BasicDBObject().append("singer", "$singer") // 그룹으로 묶을 필드
						)
								// 그룹으로 묶인 함수를 통해 계산될 내용(레코드수 세기)
								.append("COUNT(singer)", new BasicDBObject().append("$sum", 1)// count는 레코드별로 1씩 더하는 것과
																								// 동일함
								)),
				// project는 결과 보여줄 내용, SQL의 select와 from 절 사이 내용으로 이해
				new BasicDBObject().append("$project",
						new BasicDBObject().append("singer", "$_id.singer").append("song_cnt", "$COUNT(singer)")
								.append("_id", 0)),
				// SQL의 order by와 같은 역할으 수행하는 MongoDB sort 함수 호출
				// 1차 정렬은 song_cnt인 노래 수가 큰 순서, 2차 정렬은 가수이름의 가나다 순서
				new BasicDBObject().append("$sort", new BasicDBObject().append("song_cnt", -1).append("singer", 1)));
		// 메모리가 부족할경우 하드디스크를 가상메모리로 사용할수 있게 하는 옵션 추가(find가 아니라 aggregate를 쓰는 핵심 이유)
		AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();

		// 쿼리 실행하기
		Cursor cursor = rCol.aggregate(pipeline, options);

		// 컬렉션으로부터 전체 데이터 가져온 것을 List 형태로 저장하기 위한 변수 선언
		List<MelonSingerDTO> rList = new ArrayList<>();

		MelonSingerDTO rDTO = null;

		int rank = 1;

		while (cursor.hasNext()) {
			rDTO = new MelonSingerDTO();

			final DBObject current = cursor.next();

			String singer = CmmUtil.nvl((String) current.get("singer")); // 가수
			int song_cnt = (int) current.get("song_cnt"); // 랭크에 올라간 노래의 수

			log.info("singer : " + singer);
			log.info("song_cnt : " + song_cnt);

			rDTO.setRank(rank);
			rDTO.setSinger(singer);
			rDTO.setSong_cnt(song_cnt);

			rList.add(rDTO);

			rDTO = null;

			rank++;// 랭크값 1씩 증가

		}

		log.info(this.getClass().getName() + ".getRankForSinger End!");

		return rList;

	}

	@Override
	public List<MelonSongDTO> getSongForSinger(String colNm, String singer) throws Exception {
		log.info(this.getClass().getName() + ".getSongForSinger Start!");

		// 데이터를 가져올 컬렉션 선택
		DBCollection rCol = mongodb.getCollection(colNm);

		// 쿼리 만들기
		BasicDBObject query = new BasicDBObject();
		query.put("singer", singer);

		// 쿼리 실행하기
		Cursor cursor = rCol.find(query);

		// 컬렉션으로부터 전체 데이터 가졍노 것을 list 형태로 저장하기 위한 변수 선언
		List<MelonSongDTO> rList = new ArrayList<MelonSongDTO>();

		MelonSongDTO rDTO = null;

		while (cursor.hasNext()) {
			rDTO = new MelonSongDTO();

			final DBObject current = cursor.next();

			String rank = CmmUtil.nvl((String) current.get("rank"));// 순위
			String song = CmmUtil.nvl((String) current.get("song"));// 노래

			log.info("song : " + song);

			rDTO.setRank(rank);
			rDTO.setSong(song);

			rList.add(rDTO);

			rDTO = null;
		}
		log.info(this.getClass().getName() + "getSongForSinger End!");

		return rList;
	}

	@Override
	public List<MelonRankDTO> getCompareRank(String curColNm, String preColNm) throws Exception {
		
		log.info(this.getClass().getName() + ".getCompareRank Start!");
		
		// 기준이 되는 현재 컬렉션 선택
		DBCollection rCol = mongodb.getCollection(curColNm);
		
		//쿼리 만들기
		List<DBObject> pipeline = Arrays.asList(
                new BasicDBObject()
                        .append("$project", new BasicDBObject()
                                .append("_id", 0)
                                .append(curColNm, "$$ROOT")
                        ), 
                //MongoDB는 조인이 없기 때문에 LookUp을 통해 유사하게 구현 가능
                new BasicDBObject()
                        .append("$lookup", new BasicDBObject()
                                .append("localField", curColNm+".song")
                                .append("from", preColNm)
                                .append("foreignField", "song")
                                .append("as", preColNm)
                        ), 
                new BasicDBObject()
                        .append("$unwind", new BasicDBObject()
                                .append("path", "$"+preColNm)
                                .append("preserveNullAndEmptyArrays", true) //left 조인은 True, inner 조인은 false
                        ), 
                        //실제 최종 조회되는 데이터 표시를 위한 항목
                new BasicDBObject()
                        .append("$project", new BasicDBObject()
                                .append("song", "$"+curColNm+".song")
                                .append("singer", "$"+curColNm+".singer")
                                .append("current_rank", "$"+curColNm+".rank")
                                .append("pre_rank", "$"+preColNm+".rank")
                                .append("_id", 0)
                        )
        );

		// 메모리가 부족할경우 하드디스크를 가상메모리로 사용할수 있게 하는 옵션 추가(find가 아니라 aggregate를 쓰는 핵심 이유)
		AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();

		// 쿼리 실행하기
		Cursor cursor = rCol.aggregate(pipeline, options);

		// 컬렉션으로부터 전체 데이터 가져온 것을 List 형태로 저장하기 위한 변수 선언
		List<MelonRankDTO> rList = new ArrayList<>();

		MelonRankDTO rDTO = null;

		while (cursor.hasNext()) {
			rDTO = new MelonRankDTO();
			
			final DBObject current = cursor.next();
			
			String song = CmmUtil.nvl((String) current.get("song"));// 노래
			String singer = CmmUtil.nvl((String) current.get("singer"));//가수
			
			//현재 랭킹
			int current_rank = Integer.parseInt(CmmUtil.nvl((String) current.get("current_rank"),"0"));
			
			//이전 랭킹
			int pre_rank = Integer.parseInt(CmmUtil.nvl((String) current.get("pre_rank"),"0"));
			
			int change = Math.abs(current_rank - pre_rank);//랭킹 변화(절대값 변환)
			
			String change_rank = ""; //랭킹 변화 문구
			
			if(pre_rank==0) {
				change_rank = "신규 랭킹 진입";
			}else if(current_rank < pre_rank) {
				change_rank = change + "단계 상승!";
			}else {
				change_rank = change + "단계 하락!";
			}
			
			log.info("song : " + song);
			log.info("singer : " + singer);
			log.info("current_rank : " + current_rank);
			log.info("pre_rank : " + pre_rank);
			log.info("change_rank : " + change_rank);
			
			rDTO.setSong(song);
			rDTO.setSinger(singer);
			rDTO.setCurrent_rank(current_rank);
			rDTO.setPre_rank(pre_rank);
			rDTO.setChange_rank(change_rank);
			
			rList.add(rDTO);
			
			rDTO = null;
		}
		
		log.info(this.getClass().getName() + ".getCompareRank End!");
		
		return rList;
	}
	
	
}
