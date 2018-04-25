package com.yk.example.utils;


import com.yk.example.rongCloud.RongCloud;
import com.yk.example.rongCloud.messages.TxtMessage;
import com.yk.example.rongCloud.methods.message._private.Private;
import com.yk.example.rongCloud.methods.user.User;
import com.yk.example.rongCloud.models.Result;
import com.yk.example.rongCloud.models.message.PrivateMessage;
import com.yk.example.rongCloud.models.response.ResponseResult;
import com.yk.example.rongCloud.models.response.TokenResult;
import com.yk.example.rongCloud.models.user.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RongCloudUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RongCloudUtils.class);

    private static final String APP_KEY;
    private static final String APP_SECRET;
    private static final RongCloud rongCloud;


    static {
        Properties props = new Properties();
        try (InputStream stream = QiniuUtils.class.getResourceAsStream("/application.properties")) {
            if (stream == null) {
                throw new RuntimeException("找不到相应的配置文件！[classpath:/application.properties]");
            }
            props.load(stream);
            APP_KEY = props.getProperty("rongCloud.app_key");
            APP_SECRET = props.getProperty("rongCloud.app_secret");
            rongCloud = RongCloud.getInstance(APP_KEY, APP_SECRET);

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    /**
     *  注册融云用户
     * @param userId
     * @param nickName
     * @param headImgUrl
     * @return
     */
    public static TokenResult registerRongCloudUser(String userId, String nickName, String headImgUrl){
        User rongCloudUser = rongCloud.user;
        UserModel user = new UserModel()
                .setId(userId)
                .setName(nickName)
                .setPortrait(headImgUrl);
        try {
            return rongCloudUser.register(user);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 刷新融云用户信息
     * @param userId
     * @param nickName
     * @param headImgUrl
     * @return
     */
    public static Result refreshRongCloudUser(String userId, String nickName, String headImgUrl){
        User rongCloudUser = rongCloud.user;
        UserModel user = new UserModel()
                .setId(userId)
                .setName(nickName)
                .setPortrait(headImgUrl);
        try {
            return rongCloudUser.update(user);
        } catch (Exception e) {

        }
        return null;
    }

    public static ResponseResult sendPrivateMessage(String fromUserId,String toUserId,String content){
        Private msgPrivate = rongCloud.message.msgPrivate;
        PrivateMessage message = new PrivateMessage()
                .setSenderUserId(fromUserId)
                .setTargetId(new String[]{toUserId})
                .setObjectName("RC:TxtMsg")
                .setContent(new TxtMessage(content, ""));
        try {
            return msgPrivate.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}