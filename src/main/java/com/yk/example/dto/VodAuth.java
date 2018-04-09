package com.yk.example.dto;

import java.io.Serializable;

/**
 * 短视频上传凭证和上传地址实体
 * Created by yk on 2018/4/9.
 */
public class VodAuth implements Serializable {

    private String requestId;
    private String uploadAuth;
    private String videoId;
    private String uploadAddress;

    public VodAuth() {
    }

    public VodAuth(String requestId, String uploadAuth, String videoId, String uploadAddress) {
        this.requestId = requestId;
        this.uploadAuth = uploadAuth;
        this.videoId = videoId;
        this.uploadAddress = uploadAddress;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUploadAuth() {
        return uploadAuth;
    }

    public void setUploadAuth(String uploadAuth) {
        this.uploadAuth = uploadAuth;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUploadAddress() {
        return uploadAddress;
    }

    public void setUploadAddress(String uploadAddress) {
        this.uploadAddress = uploadAddress;
    }
}
