package com.socioboard.f_board_pro.models;

//model class for group lists
public class GroupModel
{
	private String groupName;
	private String groupIcon;
	private int groupUnread;
	private String groupId;
	
	
	public String getGroupId() 
	{
		return groupId;
	}
	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}
	public int getGroupUnread()
	{
		return groupUnread;
	}
	public void setGroupUnread(int groupUnread)
	{
		this.groupUnread = groupUnread;
	}
	public String getGroupName()
	{
		return groupName;
	}
	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}
	public String getGroupIcon() 
	{
		return groupIcon;
	}
	public void setGroupIcon(String groupIcon)
	{
		this.groupIcon = groupIcon;
	}
	

}
