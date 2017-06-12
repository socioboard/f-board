package com.socioboard.f_board_pro.models;

public class PagesSearch_Model {

	String pgCategory;
	String pgName;
	String pgID;
	String pgCreatedTime;
	String likesCount;	
	String eventStartTim;
	String eventEndTim;
	String location;
	String wereHere;
	
	public String getWereHere() {
		return wereHere;
	}
	public void setWereHere(String wereHere) {
		this.wereHere = wereHere;
	}
	public String getEventStartTim() {
		return eventStartTim;
	}
	public void setEventStartTim(String eventStartTim) {
		this.eventStartTim = eventStartTim;
	}
	public String getEventEndTim() {
		return eventEndTim;
	}
	public void setEventEndTim(String eventEndTim) {
		this.eventEndTim = eventEndTim;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getPgCategory() {
		return pgCategory;
	}
	public void setPgCategory(String pgCategory) {
		this.pgCategory = pgCategory;
	}
	public String getPgName() {
		return pgName;
	}
	public void setPgName(String pgName) {
		this.pgName = pgName;
	}
	public String getPgID() {
		return pgID;
	}
	public void setPgID(String pgID) {
		this.pgID = pgID;
	}
	public String getPgCreatedTime() {
		return pgCreatedTime;
	}
	public void setPgCreatedTime(String pgCreatedTime) {
		this.pgCreatedTime = pgCreatedTime;
	}
	public String getLikesCount() {
		return likesCount;
	}
	public void setLikesCount(String likesCount) {
		this.likesCount = likesCount;
	}



}
