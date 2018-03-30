package com.yk.example.rongCloud.methods.group.gag;



import com.yk.example.rongCloud.RongCloud;
import com.yk.example.rongCloud.models.CheckMethod;
import com.yk.example.rongCloud.models.Result;
import com.yk.example.rongCloud.models.group.GroupMember;
import com.yk.example.rongCloud.models.group.GroupModel;
import com.yk.example.rongCloud.models.response.ListGagGroupUserResult;
import com.yk.example.rongCloud.models.response.ResponseResult;
import com.yk.example.rongCloud.util.CommonUtil;
import com.yk.example.rongCloud.util.GsonUtil;
import com.yk.example.rongCloud.util.HttpUtil;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
/**
 * 群组成员禁言服务
 * docs : http://www.rongcloud.cn/docs/server.html#group_user_gag
 *
 * */
public class Gag {
    private static final String UTF8 = "UTF-8";
    private static final String PATH = "group/gag";
    private static String method = "";
    private String appKey;
    private String appSecret;
    private RongCloud rongCloud;

    public RongCloud getRongCloud() {
        return rongCloud;
    }
    public void setRongCloud(RongCloud rongCloud) {
        this.rongCloud = rongCloud;
    }
    public Gag(String appKey, String appSecret) {
        this.appKey = appKey;
        this.appSecret = appSecret;

    }
    /**
     * 添加禁言群成员方法（在 App 中如果不想让某一用户在群中发言时，可将此用户在群组中禁言，被禁言用户可以接收查看群组中用户聊天信息，但不能发送消息。）
     *
     * @param group:群组信息。id , munite , memberIds（必传）
     *
     * @return Result
     **/
    public Result add(GroupModel group) throws Exception {
        String message = CommonUtil.checkFiled(group,PATH, CheckMethod.ADD);
        if(null != message){
            return (ResponseResult) GsonUtil.fromJson(message,ResponseResult.class);
        }

       /* message = CommonUtil.checkParam("munite",munite,PATH,CheckMethod.ADD);
        if(null != message){
            return (Result)GsonUtil.fromJson(message,Result.class);
        }*/

        StringBuilder sb = new StringBuilder();
        GroupMember[] members = group.getMembers();
        for(GroupMember member : members){
            sb.append("&userId=").append(URLEncoder.encode(member.getId().toString(), UTF8));
        }
        sb.append("&groupId=").append(URLEncoder.encode(group.getId().toString(), UTF8));
        sb.append("&minute=").append(URLEncoder.encode(group.getMunite().toString(), UTF8));
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(rongCloud.getApiHostType(), appKey, appSecret, "/group/user/gag/add.json", "application/x-www-form-urlencoded");
        HttpUtil.setBodyParameter(body, conn);

        return (ResponseResult) GsonUtil.fromJson(CommonUtil.getResponseByCode(PATH,CheckMethod.ADD,HttpUtil.returnResult(conn)), ResponseResult.class);
    }

    /**
     * 查询被禁言群成员方法
     *
     * @param  groupId:群组Id。（必传）
     *
     * @return ListGagGroupUserResult
     **/
    public ListGagGroupUserResult getList(String groupId) throws Exception {
        String message = CommonUtil.checkParam("id",groupId,PATH,CheckMethod.GETLIST);
        if(null != message){
            return (ListGagGroupUserResult)GsonUtil.fromJson(message,ListGagGroupUserResult.class);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("&groupId=").append(URLEncoder.encode(groupId.toString(), UTF8));
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(rongCloud.getApiHostType(), appKey, appSecret, "/group/user/gag/list.json", "application/x-www-form-urlencoded");
        HttpUtil.setBodyParameter(body, conn);

        return (ListGagGroupUserResult) GsonUtil.fromJson(CommonUtil.getResponseByCode(PATH,CheckMethod.GETLIST,HttpUtil.returnResult(conn)), ListGagGroupUserResult.class);
    }

    /**
     * 移除禁言群成员方法
     *
     * @param  group:群组（必传）
     *
     * @return ResponseResult
     **/
    public Result remove(GroupModel group) throws Exception {
        //参数校验
        String message = CommonUtil.checkFiled(group,PATH, CheckMethod.REMOVE);
        if(null != message){
            return (ResponseResult)GsonUtil.fromJson(message,ResponseResult.class);
        }
        StringBuilder sb = new StringBuilder();

        GroupMember[] members = group.getMembers();
        for(GroupMember member : members){
            sb.append("&userId=").append(URLEncoder.encode(member.getId().toString(), UTF8));
        }

        sb.append("&groupId=").append(URLEncoder.encode(group.getId().toString(), UTF8));
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(rongCloud.getApiHostType(), appKey, appSecret, "/group/user/gag/rollback.json", "application/x-www-form-urlencoded");
        HttpUtil.setBodyParameter(body, conn);

        return (ResponseResult) GsonUtil.fromJson(CommonUtil.getResponseByCode(PATH,CheckMethod.REMOVE,HttpUtil.returnResult(conn)), ResponseResult.class);
    }
}
