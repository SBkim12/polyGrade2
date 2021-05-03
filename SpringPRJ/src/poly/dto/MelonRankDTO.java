package poly.dto;

public class MelonRankDTO {
	private String song;
	private String singer;
	private int current_rank;
	private	int pre_rank;
	private String change_rank;
	
	
	public String getSong() {
		return song;
	}
	public void setSong(String song) {
		this.song = song;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	public int getCurrent_rank() {
		return current_rank;
	}
	public void setCurrent_rank(int current_rank) {
		this.current_rank = current_rank;
	}
	public int getPre_rank() {
		return pre_rank;
	}
	public void setPre_rank(int pre_rank) {
		this.pre_rank = pre_rank;
	}
	public String getChange_rank() {
		return change_rank;
	}
	public void setChange_rank(String change_rank) {
		this.change_rank = change_rank;
	}
	
	
}
