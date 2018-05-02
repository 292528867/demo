package com.yk.example.dto;

/**
 * Created by yk on 2018/5/2.
 */
public class BindDto {

    private String userId;

    private String phone;

    private boolean qqStatus;

    private boolean wxStatus;

    private boolean wbStatus;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isQqStatus() {
        return qqStatus;
    }

    public void setQqStatus(boolean qqStatus) {
        this.qqStatus = qqStatus;
    }

    public boolean isWxStatus() {
        return wxStatus;
    }

    public void setWxStatus(boolean wxStatus) {
        this.wxStatus = wxStatus;
    }

    public boolean isWbStatus() {
        return wbStatus;
    }

    public void setWbStatus(boolean wbStatus) {
        this.wbStatus = wbStatus;
    }
}
