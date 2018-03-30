package com.yk.example.rongCloud.models.response;


import com.yk.example.rongCloud.models.Result;
import com.yk.example.rongCloud.util.GsonUtil;

import java.util.List;

/**
 *  lisitGagGroupUser 返回结果
 */
public class ListGagGroupUserResult extends Result {
	// 群组被禁言用户列表。
	List<GagGroupUser> users;

	public ListGagGroupUserResult(Integer code, List<GagGroupUser> users, String errorMessage) {
		this.code = code;
		this.users = users;
		this.msg = errorMessage;
	}

	/**
	 * 设置users
	 *
	 */	
	public void setUsers(List<GagGroupUser> users) {
		this.users = users;
	}
	
	/**
	 * 获取users
	 *
	 * @return List<GagGroupUser>
	 */
	public List<GagGroupUser> getUsers() {
		return users;
	}

	@Override
	public String toString() {
		return GsonUtil.toJson(this, ListGagGroupUserResult.class);
	}
}
