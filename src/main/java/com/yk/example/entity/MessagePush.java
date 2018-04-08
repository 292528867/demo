package com.yk.example.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 友盟推送
 * Created by yk on 2018/4/8.
 */
public class MessagePush implements Serializable{

    /**
     * 必填 应用唯一标识
     */
    @JsonProperty(required = true)
    private String appKey;

    /**
     * 必填 时间戳，10位或者13位均可，时间戳有效期为10分钟
     */
    @JsonProperty(required = true)
    private long timestamp = System.currentTimeMillis();

    /**
     * 必填 消息发送类型,其值可以为: (unicast-单播, listcast-列播, filecast-文件播, broadcast-广播, groupcast-组播, customizedcast)
     */
    @JsonProperty(required = true)
    private String type = "unicast";

    @JsonProperty(value = "device_tokens")
    private String deviceTokens;


    private Payload payload;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeviceTokens() {
        return deviceTokens;
    }

    public void setDeviceTokens(String deviceTokens) {
        this.deviceTokens = deviceTokens;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public static class Payload {

        /**
         * 必填 消息类型，值可以为: notification-通知，message-消息
         */
        @JsonProperty(value = "display_type", required = true)
        private String displayType;

        public Payload() {
        }

        public Payload(String displayType) {
            this.displayType = displayType;
        }

        public Payload(String displayType, Body body) {
            this.displayType = displayType;
            this.body = body;
        }

        /**
         * 必填 消息体
         */
        @JsonProperty(required = true)
        private Body body;

        public String getDisplayType() {
            return displayType;
        }

        public void setDisplayType(String displayType) {
            this.displayType = displayType;
        }

        public Body getBody() {
            return body;
        }

        public void setBody(Body body) {
            this.body = body;
        }

        public static class Body {

            /**
             * 必填 通知栏提示文字
             */
            @JsonProperty(required = true)
            private String ticker;

            /**
             * 必填 通知标题
             */
            @JsonProperty(required = true)
            private String title;

            /**
             * 必填 通知文字描述
             */
            @JsonProperty(required = true)
            private String text;

            public Body() {
            }

            public Body(String ticker, String title, String text) {
                this.ticker = ticker;
                this.title = title;
                this.text = text;
            }

            public String getTicker() {
                return ticker;
            }

            public void setTicker(String ticker) {
                this.ticker = ticker;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }

    }
}
