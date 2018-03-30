package com.yk.example.rongCloud.methods.chatroom.ban;



import com.yk.example.rongCloud.RongCloud;
import com.yk.example.rongCloud.exception.ParamException;
import com.yk.example.rongCloud.models.CheckMethod;
import com.yk.example.rongCloud.models.CommonConstrants;
import com.yk.example.rongCloud.models.chatroom.ChatroomMember;
import com.yk.example.rongCloud.models.chatroom.ChatroomModel;
import com.yk.example.rongCloud.models.response.ListGagChatroomUserResult;
import com.yk.example.rongCloud.models.response.ResponseResult;
import com.yk.example.rongCloud.util.CommonUtil;
import com.yk.example.rongCloud.util.GsonUtil;
import com.yk.example.rongCloud.util.HttpUtil;

import java.net.HttpURLConnection;
import java.net.URLEncoder;

/**
 * 聊天室全局禁言服务
 * docs:http://www.rongcloud.cn/docs/server.html#chatroom_user_ban
 *
 * */
public class Ban {
    private static final String UTF8 = "UTF-8";
    private static final String PATH = "chatroom/global-gag";
    private String appKey;
    private String appSecret;
    private RongCloud rongCloud;

    public RongCloud getRongCloud() {
        return rongCloud;
    }
    public void setRongCloud(RongCloud rongCloud) {
        this.rongCloud = rongCloud;
    }
    public Ban(String appKey, String appSecret) {
        this.appKey = appKey;
        this.appSecret = appSecret;
    }
    /**
     * 添加用户聊天室全局禁言方法
     *
     * @param  chatroom : Id,minute。（必传）
     *
     * @return ResponseResult
     **/
    public ResponseResult add(ChatroomModel chatroom) throws Exception {
        String errMsg = CommonUtil.checkFiled(chatroom,PATH, CheckMethod.ADD);
        if(null != errMsg){
            return (ResponseResult) GsonUtil.fromJson(errMsg,ResponseResult.class);
        }

        StringBuilder sb = new StringBuilder();
        ChatroomMember[] members = chatroom.getMembers();
        for(ChatroomMember member : members){
            sb.append("&userId=").append(URLEncoder.encode(member.getId(), UTF8));
        }
        sb.append("&minute=").append(URLEncoder.encode(chatroom.getMinute().toString(), UTF8));
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(rongCloud.getApiHostType(), appKey, appSecret, "/chatroom/user/ban/add.json", "application/x-www-form-urlencoded");
        HttpUtil.setBodyParameter(body, conn);

        return (ResponseResult) GsonUtil.fromJson(CommonUtil.getResponseByCode(PATH,CheckMethod.ADD,HttpUtil.returnResult(conn)), ResponseResult.class);
    }

    /**
     * 查询被聊天室全局禁言用户方法
     *
     * @return ListGagChatroomUserResult
     **/
    public ListGagChatroomUserResult getList() throws Exception {
        StringBuilder sb = new StringBuilder();

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(rongCloud.getApiHostType(), appKey, appSecret, "/chatroom/user/ban/query.json", "application/x-www-form-urlencoded");

        return (ListGagChatroomUserResult) GsonUtil.fromJson(CommonUtil.getResponseByCode(PATH,CheckMethod.GETLIST,HttpUtil.returnResult(conn)), ListGagChatroomUserResult.class);
    }

    /**
     * 移除用户聊天室全局禁言方法
     *
     * @param  chatroom: memberIds。（必传）
     *
     * @return ResponseResult
     **/
    public ResponseResult remove(ChatroomModel chatroom) throws Exception {
        if (chatroom == null) {
            throw new ParamException(CommonConstrants.RCLOUD_PARAM_NULL,"/chatroom/user/ban/remove","Paramer 'chatroom' is required");
        }

        String errMsg = CommonUtil.checkFiled(chatroom,PATH,CheckMethod.REMOVE);
        if(null != errMsg){
            return (ResponseResult)GsonUtil.fromJson(errMsg,ResponseResult.class);
        }

        StringBuilder sb = new StringBuilder();
        ChatroomMember[] members = chatroom.getMembers();
        for(ChatroomMember member : members){
            sb.append("&userId=").append(URLEncoder.encode(member.getId(), UTF8));
        }
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(rongCloud.getApiHostType(), appKey, appSecret, "/chatroom/user/ban/remove.json", "application/x-www-form-urlencoded");
        HttpUtil.setBodyParameter(body, conn);

        return (ResponseResult) GsonUtil.fromJson(CommonUtil.getResponseByCode(PATH,CheckMethod.REMOVE,HttpUtil.returnResult(conn)), ResponseResult.class);
    }
}


