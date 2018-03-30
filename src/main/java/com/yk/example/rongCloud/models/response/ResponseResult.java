package com.yk.example.rongCloud.models.response;


import com.yk.example.rongCloud.models.Result;
import com.yk.example.rongCloud.util.GsonUtil;

/**
 *  http 成功返回结果
 */
public class ResponseResult extends Result {

	public ResponseResult(Integer code, String msg) {
		super(code, msg);
		this.code = code;
		this.msg = msg;
	}

	@Override
	public String toString() {
		return GsonUtil.toJson(this, ResponseResult.class);
	}
}
