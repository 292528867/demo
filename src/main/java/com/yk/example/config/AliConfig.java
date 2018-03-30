package com.yk.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by yk on 2018/3/28.
 */
@Component
@PropertySource(value = "classpath:ali.properties")
public class AliConfig {

    @Value("${APP_ID}")
    private String APP_ID;
    @Value("${sellerId}")
    private String sellerId;
    @Value("${APP_PRIVATE_KEY}")
    private String APP_PRIVATE_KEY;
    @Value("${ALIPAY_PUBLIC_KEY}")
    private String ALIPAY_PUBLIC_KEY;
    @Value("${notifyUrl}")
    private String notifyUrl;
    @Value("${returnUrl}")
    private String returnUrl;
    @Value("${signType}")
    private String signType;
    @Value("${logPath}")
    private String logPath;
    @Value("${CHARSET}")
    private String CHARSET;
    @Value("${antiPhishingKey}")
    private String antiPhishingKey;
    @Value("${exterInvokeIp}")
    private String exterInvokeIp;

    public String getAPP_ID() {
        return APP_ID;
    }

    public void setAPP_ID(String APP_ID) {
        this.APP_ID = APP_ID;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getAPP_PRIVATE_KEY() {
        return APP_PRIVATE_KEY;
    }

    public void setAPP_PRIVATE_KEY(String APP_PRIVATE_KEY) {
        this.APP_PRIVATE_KEY = APP_PRIVATE_KEY;
    }

    public String getALIPAY_PUBLIC_KEY() {
        return ALIPAY_PUBLIC_KEY;
    }

    public void setALIPAY_PUBLIC_KEY(String ALIPAY_PUBLIC_KEY) {
        this.ALIPAY_PUBLIC_KEY = ALIPAY_PUBLIC_KEY;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getCHARSET() {
        return CHARSET;
    }

    public void setCHARSET(String CHARSET) {
        this.CHARSET = CHARSET;
    }

    public String getAntiPhishingKey() {
        return antiPhishingKey;
    }

    public void setAntiPhishingKey(String antiPhishingKey) {
        this.antiPhishingKey = antiPhishingKey;
    }

    public String getExterInvokeIp() {
        return exterInvokeIp;
    }

    public void setExterInvokeIp(String exterInvokeIp) {
        this.exterInvokeIp = exterInvokeIp;
    }
}
