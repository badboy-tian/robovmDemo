package net.mwplay.beans;

public class Channel {
	private int id;
	private String name;
	private String name_en;
	private int seq_id;
	private String abbr_en;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName_en() {
		return name_en;
	}
	public void setName_en(String name_en) {
		this.name_en = name_en;
	}
	public int getSeq_id() {
		return seq_id;
	}
	public void setSeq_id(int seq_id) {
		this.seq_id = seq_id;
	}
	public String getAbbr_en() {
		return abbr_en;
	}
	public void setAbbr_en(String abbr_en) {
		this.abbr_en = abbr_en;
	}
	@Override
	public String toString() {
		return "Channels [id=" + id + ", name=" + name + ", name_en=" + name_en
				+ ", seq_id=" + seq_id + ", abbr_en=" + abbr_en + "]";
	}
	
	
	
}
