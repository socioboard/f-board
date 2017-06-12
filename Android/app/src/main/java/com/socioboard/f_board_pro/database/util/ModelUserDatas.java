package com.socioboard.f_board_pro.database.util;

public class ModelUserDatas {

	String userid;
	String username;
	String userAcessToken;
	String userimage;
	String userEmail;




	public String getAdminPageAcessToken() {
		return adminPageAcessToken;
	}

	public void setAdminPageAcessToken(String adminPageAcessToken) {
		this.adminPageAcessToken = adminPageAcessToken;
	}

	String adminPageAcessToken;
	

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserimage()
	{
		return userimage;
	}

	public void setUserimage(String userimage) {
		this.userimage = userimage;
	}

	public String getUserAcessToken() {
		return userAcessToken;
	}

	public void setUserAcessToken(String userAcessToken) {
		this.userAcessToken = userAcessToken;
	}

	@Override
	public String toString() {
		return "\nModelUserDatas [userid=" + userid + ", username=" + username
				+ ", userAcessToken=" + userAcessToken + "]";
	}

	public ModelUserDatas()
	{
	}

	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * @param userid
	 *            the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the level
	 */

}
