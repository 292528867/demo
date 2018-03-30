package com.yk.example.rongCloud.methods.chatroom.keepalive;


import com.yk.example.rongCloud.RongCloud;
import com.yk.example.rongCloud.models.CheckMethod;
import com.yk.example.rongCloud.models.chatroom.ChatroomModel;
import com.yk.example.rongCloud.models.response.ChatroomKeepaliveResult;
import com.yk.example.rongCloud.models.response.ResponseResult;
import com.yk.example.rongCloud.util.CommonUtil;
import com.yk.example.rongCloud.util.GsonUtil;
import com.yk.example.rongCloud.util.HttpUtil;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
/**
 *
 * 聊天室保活服务
 * docs: "http://www.rongcloud.cn/docs/server.html#chatroom_keepalive"
 *
 * */
public class Keepalive {
    private static final String UTF8 = "UTF-8";
    private static final String PATH = "chatroom/keepalive";
    private String appKey;
    private String appSecret;
    private RongCloud rongCloud;

    public RongCloud getRongCloud() {
        return rongCloud;
    }
    public void setRongCloud(RongCloud rongCloud) {
        this.rongCloud = rongCloud;
    }
    public Keepalive(String appKey, String appSecret) {
        this.appKey = appKey;
        this.appSecret = appSecret;

    }
    /**
     * 添加聊天室保活方法
     *
     * @param   chatroom: 聊天室信息，id（必传）
     *
     * @return ResponseResult
     **/
    public ResponseResult add(ChatroomModel chatroom) throws Exception {

        String message = CommonUtil.checkFiled(chatroom,PATH, CheckMethod.ADD);
        if(null != message){
            return (ResponseResult) GsonUtil.fromJson(message,ResponseResult.class);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("&chatroomId=").append(URLEncoder.encode(chatroom.getId().toString(), UTF8));
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(rongCloud.getApiHostType(), appKey, appSecret, "/chatroom/keepalive/add.json", "application/x-www-form-urlencoded");
        HttpUtil.setBodyParameter(body, conn);

        return (ResponseResult) GsonUtil.fromJson(CommonUtil.getResponseByCode(PATH,CheckMethod.ADD,HttpUtil.returnResult(conn)), ResponseResult.class);
    }
    /**
     * 删除聊天室保活方法
     *
     * @param  chatroom: 聊天室信息，id（必传）
     *
     * @return ResponseResult
     **/
    public ResponseResult remove(ChatroomModel chatroom) throws Exception {
        String message = CommonUtil.checkFiled(chatroom,PATH,CheckMethod.REMOVE);
        if(null != message){
            return (ResponseResult)GsonUtil.fromJson(message,ResponseResult.class);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("&chatroomId=").append(URLEncoder.encode(chatroom.getId().toString(), UTF8));
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(rongCloud.getApiHostType(), appKey, appSecret, "/chatroom/keepalive/remove.json", "application/x-www-form-urlencoded");
        HttpUtil.setBodyParameter(body, conn);

        return (ResponseResult) GsonUtil.fromJson(CommonUtil.getResponseByCode(PATH,CheckMethod.REMOVE,HttpUtil.returnResult(conn)), ResponseResult.class);

    }
    /**
     * 获取聊天室保活
     *
     *
     * @return ResponseResult
     **/
    public ChatroomKeepaliveResult getList() throws Exception {
        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(rongCloud.getApiHostType(), appKey, appSecret, "/chatroom/keepalive/query.json", "application/x-www-form-urlencoded");

        return (ChatroomKeepaliveResult) GsonUtil.fromJson(CommonUtil.getResponseByCode(PATH,CheckMethod.GETLIST,HttpUtil.returnResult(conn)), ChatroomKeepaliveResult.class);
    }
}
