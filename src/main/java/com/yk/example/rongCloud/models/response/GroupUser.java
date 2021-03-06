package com.yk.example.rongCloud.models.response;


import com.yk.example.rongCloud.util.GsonUtil;

/**
 * 群组用户信息。
 * @author RongCloud
 */
public class GroupUser {
	// 用户 Id。
	String id;
	
	public GroupUser(String id) {
		this.id = id;
	}
	
	/**
	 * 设置id
	 *
	 */	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 获取id
	 *
	 * @return String
	 */
	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return GsonUtil.toJson(this, GroupUser.class);
	}
}
