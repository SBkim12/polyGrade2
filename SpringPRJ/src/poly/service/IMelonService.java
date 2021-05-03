package poly.service;

import java.util.List;

import poly.dto.MelonDTO;
import poly.dto.MelonRankDTO;
import poly.dto.MelonSingerDTO;
import poly.dto.MelonSongDTO;

public interface IMelonService {
	
	/**
	 * 멜론 Top100 순위 수집하기
	 */
	public int collectMelonRank() throws Exception;
	
	public List<MelonDTO> getRank() throws Exception;
	
	public List<MelonSingerDTO> getRankForSinger() throws Exception;
	
	public List<MelonSongDTO> getSongForSinger() throws Exception;
	
	public List<MelonRankDTO> getCompareRank() throws Exception;
}
