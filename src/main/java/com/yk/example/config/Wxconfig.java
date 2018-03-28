package com.yk.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by yk on 2018/3/28.
 */
@Component
@PropertySource(value = "wx.properties")
public class Wxconfig {

    @Value("${AppID}")
    private String AppID;
    @Value("${AppSecret}")
    private String AppSecret;
    @Value("${mch_id}")
    private String mch_id;
    @Value("${unifiedorder_url}")
    private String unifiedorder_url;
    @Value("${redirect_uri}")
    private String redirect_uri;
    @Value("${menuCreate_url}")
    private String menuCreate_url;
    @Value("${getOpenid_url}")
    private String getOpenid_url;
    @Value("${SendPayOrder_id}")
    private String SendPayOrder_id;
    @Value("${sendTemplate_url}")
    private String sendTemplate_url;
    @Value("${notify_url}")
    private String notify_url;

    public String getAppID() {
        return AppID;
    }

    public void setAppID(String appID) {
        AppID = appID;
    }

    public String getAppSecret() {
        return AppSecret;
    }

    public void setAppSecret(String appSecret) {
        AppSecret = appSecret;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getUnifiedorder_url() {
        return unifiedorder_url;
    }

    public void setUnifiedorder_url(String unifiedorder_url) {
        this.unifiedorder_url = unifiedorder_url;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public String getMenuCreate_url() {
        return menuCreate_url;
    }

    public void setMenuCreate_url(String menuCreate_url) {
        this.menuCreate_url = menuCreate_url;
    }

    public String getGetOpenid_url() {
        return getOpenid_url;
    }

    public void setGetOpenid_url(String getOpenid_url) {
        this.getOpenid_url = getOpenid_url;
    }

    public String getSendPayOrder_id() {
        return SendPayOrder_id;
    }

    public void setSendPayOrder_id(String sendPayOrder_id) {
        SendPayOrder_id = sendPayOrder_id;
    }

    public String getSendTemplate_url() {
        return sendTemplate_url;
    }

    public void setSendTemplate_url(String sendTemplate_url) {
        this.sendTemplate_url = sendTemplate_url;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }
}
