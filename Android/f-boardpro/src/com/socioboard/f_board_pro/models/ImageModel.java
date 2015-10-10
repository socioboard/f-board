package com.socioboard.f_board_pro.models;

public class ImageModel
{
	String imageId=null;
	String imageUrl=null;
	String imageDate=null;
	String imageName=null;
	int imageLikes=0;
	int imageComments=0;
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getImageDate() {
		return imageDate;
	}
	public void setImageDate(String imageDate) {
		this.imageDate = imageDate;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public int getImageLikes() {
		return imageLikes;
	}
	public void setImageLikes(int imageLikes) {
		this.imageLikes = imageLikes;
	}
	public int getImageComments() {
		return imageComments;
	}
	public void setImageComments(int imageComments) {
		this.imageComments = imageComments;
	}
	
	
}
