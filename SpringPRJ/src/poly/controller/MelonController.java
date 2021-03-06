package poly.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import poly.dto.MelonDTO;
import poly.dto.MelonRankDTO;
import poly.dto.MelonSingerDTO;
import poly.dto.MelonSongDTO;
import poly.service.IMelonService;

@Controller
public class MelonController {
	private Logger log = Logger.getLogger(this.getClass());

	/*
	 * 비즈니스 로직(중요 로직을 수행하기 위해 사용되는 서비스를 메모리에 적재(싱글톤패턴 적용됨)
	 */
	@Resource(name = "MelonService")
	private IMelonService melonService;

	/**
	 * 멜론 Top100 수집하기
	 */
	@RequestMapping(value = "melon/collectMelonRank")
	@ResponseBody
	public String collectMelonRank(HttpServletRequest request, HttpServletResponse response) throws Exception {

		log.info(this.getClass().getName() + ".collectMelonRank Start!");

		melonService.collectMelonRank();

		log.info(this.getClass().getName() + ".collectMelonRank End!");

		return "success";
	}
	
	@RequestMapping(value = "melon/melonTop100")
	public String melonTop100(HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		log.info(this.getClass().getName() + ".melonTop100 start");
		
		log.info(this.getClass().getName() + ".melonTop100 End!");
		
		
		return "melon/melonTop100";		
	}
	
	@RequestMapping(value = "melon/getRank")
	@ResponseBody
	public List<MelonDTO> getRank(HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		log.info(this.getClass().getName() + ".getRank Start!");
		
		List<MelonDTO> rList = melonService.getRank();
		
		if(rList==null) {
			rList = new ArrayList<MelonDTO>();
		}
		
		log.info(this.getClass().getName() + ".getRank End!");
		
		return rList;
	}
	
	@RequestMapping(value= "melon/melonSingerRank")
	public String melonSingerRank(HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		log.info(this.getClass().getName() + ".melonSingerRank Start!");
		log.info(this.getClass().getName() + ".melonSingerRank End!");
		
		return "/melon/melonSingerRank";
	}
	
	@RequestMapping(value= "melon/getRankForSinger")
	@ResponseBody
	public List<MelonSingerDTO> getRankForSinger(HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		log.info(this.getClass().getName() + ".getRankForSinger Start!");
		
		List<MelonSingerDTO> rList = melonService.getRankForSinger();
		
		if(rList==null) {
			rList = new ArrayList<MelonSingerDTO>();
		}
		
		log.info(this.getClass().getName() + ".getRankForSinger End!");
		
		return rList;
	}
	
	//가수의 노래 데이터 가져오는 화면 불러오기
	@RequestMapping(value = "melon/melonSongForSinger")
	public String melonSongForSinger(HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		log.info(this.getClass().getName() + ".melonSongForsinger Start!");
		log.info(this.getClass().getName() + ".melonSongForSinger End!");
		
		return "/melon/melonSongForSinger";
		
	}
	
	//가수 노래 데이터 가져오기
	@RequestMapping(value= "melon/getSongForSinger")
	@ResponseBody
	public List<MelonSongDTO> getSongForSinger(HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		log.info(this.getClass().getName() + ".getSongForSinger Start!");
		
		List<MelonSongDTO> rList = melonService.getSongForSinger();
		
		if(rList==null) {
			rList = new ArrayList<MelonSongDTO>();
		}
		
		log.info(this.getClass().getName() + ".getSongForSinger End!");
		
		return rList;
	}
	
		//가수의 노래 데이터 가져오는 화면 불러오기
		@RequestMapping(value = "melon/melonCompareRank")
		public String melonCompareRank(HttpServletRequest request, HttpServletResponse response)throws Exception{
			
			log.info(this.getClass().getName() + "melonCompareRank Start!");
			log.info(this.getClass().getName() + ".melonCompareRank End!");
			
			return "/melon/melonCompareRank";
			
		}
		
		//가수 노래 데이터 가져오기
		@RequestMapping(value= "melon/getCompareRank")
		@ResponseBody
		public List<MelonRankDTO> getCompareRank(HttpServletRequest request, HttpServletResponse response)throws Exception{
			
			log.info(this.getClass().getName() + ".getCompareRank Start!");
			
			List<MelonRankDTO> rList = melonService.getCompareRank();
			
			if(rList==null) {
				rList = new ArrayList<MelonRankDTO>();
			}
			
			log.info(this.getClass().getName() + ".getCompareRank End!");
			
			return rList;
		}
	
}
