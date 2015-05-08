package com.socioboard.f_board_pro.models;
//model class for handling pages
public class PagesModel {
	
	String pgCategory;
	String pgName;
	String pgID;
	String pgCreatedTime;
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

}
