package com.yk.example.rongCloud.methods.chatroom.whitelist;



import com.yk.example.rongCloud.RongCloud;
import com.yk.example.rongCloud.exception.ParamException;
import com.yk.example.rongCloud.models.CheckMethod;
import com.yk.example.rongCloud.models.CommonConstrants;
import com.yk.example.rongCloud.models.chatroom.ChatroomMember;
import com.yk.example.rongCloud.models.chatroom.ChatroomModel;
import com.yk.example.rongCloud.models.response.ResponseResult;
import com.yk.example.rongCloud.models.response.WhiteListResult;
import com.yk.example.rongCloud.util.CommonUtil;
import com.yk.example.rongCloud.util.GsonUtil;
import com.yk.example.rongCloud.util.HttpUtil;

import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class User {
    private static final String UTF8 = "UTF-8";
    private static final String PATH = "chatroom/whitelist/user";
    private String appKey;
    private String appSecret;
    private RongCloud rongCloud;

    public RongCloud getRongCloud() {
        return rongCloud;
    }
    public void setRongCloud(RongCloud rongCloud) {
        this.rongCloud = rongCloud;
    }
    public User(String appKey, String appSecret) {
        this.appKey = appKey;
        this.appSecret = appSecret;

    }
    /**
     * 添加聊天室白名单成员方法
     *
     * @param  chatroom:聊天室.Id，memberIds 聊天室中白名单成员最多不超过 5 个。（必传）
     *
     * @return ResponseResult
     **/
    public ResponseResult add(ChatroomModel chatroom) throws Exception {

        if (chatroom == null) {
            throw new ParamException(CommonConstrants.RCLOUD_PARAM_NULL,"/chatroom/whitelist/user/add", "Paramer 'chatroom' is required");
        }

        String message = CommonUtil.checkFiled(chatroom,PATH, CheckMethod.ADD);
        if(null != message){
            return (ResponseResult) GsonUtil.fromJson(message,ResponseResult.class);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&chatroomId=").append(URLEncoder.encode(chatroom.getId().toString(), UTF8));

        ChatroomMember[] members = chatroom.getMembers();
        for(ChatroomMember member : members){
            sb.append("&userId=").append(URLEncoder.encode(member.getId(), UTF8));
        }
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(rongCloud.getApiHostType(), appKey, appSecret, "/chatroom/user/whitelist/add.json", "application/x-www-form-urlencoded");
        HttpUtil.setBodyParameter(body, conn);

        return (ResponseResult) GsonUtil.fromJson(CommonUtil.getResponseByCode(PATH,CheckMethod.ADD,HttpUtil.returnResult(conn)), ResponseResult.class);
    }
    /**
     * 添加聊天室白名单成员方法
     *
     * @param  chatroom:聊天室.Id，memberIds 聊天室中白名单成员最多不超过 5 个。（必传）
     *
     * @return ResponseResult
     **/
    public ResponseResult remove(ChatroomModel chatroom) throws Exception {
        if (chatroom == null) {
            throw new ParamException(CommonConstrants.RCLOUD_PARAM_NULL,"/chatroom/whitelist/user/remove", "Paramer 'chatroom' is required");
        }

        String message = CommonUtil.checkFiled(chatroom,PATH, CheckMethod.REMOVE);
        if(null != message){
            return (ResponseResult)GsonUtil.fromJson(message,ResponseResult.class);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&chatroomId=").append(URLEncoder.encode(chatroom.getId().toString(), UTF8));

        ChatroomMember[] members = chatroom.getMembers();
        for(ChatroomMember member : members){
            sb.append("&userId=").append(URLEncoder.encode(member.getId(), UTF8));
        }

        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(rongCloud.getApiHostType(), appKey, appSecret, "/chatroom/user/whitelist/remove.json", "application/x-www-form-urlencoded");
        HttpUtil.setBodyParameter(body, conn);

        return (ResponseResult) GsonUtil.fromJson(CommonUtil.getResponseByCode(PATH,CheckMethod.REMOVE,HttpUtil.returnResult(conn)), ResponseResult.class);
    }

    /**
     * 添加聊天室白名单成员方法
     *
     *
     * @return WhiteListResult
     **/
    public WhiteListResult getList(ChatroomModel chatroom) throws Exception {
        if (chatroom == null) {
            throw new ParamException(CommonConstrants.RCLOUD_PARAM_NULL,"/chatroom/whitelist/user/getList", "Paramer 'chatroom' is required");
        }

        String message = CommonUtil.checkFiled(chatroom,PATH,CheckMethod.GETLIST);
        if(null != message){
            return (WhiteListResult)GsonUtil.fromJson(message,WhiteListResult.class);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&chatroomId=").append(URLEncoder.encode(chatroom.getId().toString(), UTF8));

        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(rongCloud.getApiHostType(), appKey, appSecret, "/chatroom/user/whitelist/query.json", "application/x-www-form-urlencoded");
        HttpUtil.setBodyParameter(body, conn);

        return (WhiteListResult) GsonUtil.fromJson(CommonUtil.getResponseByCode(PATH,CheckMethod.GETLIST,HttpUtil.returnResult(conn)), WhiteListResult.class);
    }
}
