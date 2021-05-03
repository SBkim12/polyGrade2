package poly.persistance.mongo;

import java.util.List;

import poly.dto.MelonDTO;
import poly.dto.MelonRankDTO;
import poly.dto.MelonSingerDTO;
import poly.dto.MelonSongDTO;

public interface IMelonMapper {
	/**
	 * MongoDB 컬렉션 생성하기
	 * 
	 * @param colNm 생성하는 컬렉션 이름
	 */
	public boolean createCollection(String colNm) throws Exception;

	/**
	 * MongoDB 데이터 저장하기
	 * 
	 * @param pDTO 저장될 정보
	 */
	public int insertRank(List<MelonDTO> pList, String colNm) throws Exception;
	
	public List<MelonDTO> getRank(String colNm) throws Exception;
	
	public List<MelonSingerDTO>	getRankForSinger(String colNm)throws Exception;
	
	public List<MelonSongDTO> getSongForSinger(String colNm, String singer) throws Exception;
	
	public List<MelonRankDTO> getCompareRank(String curColNm, String preColNm) throws Exception;
}
