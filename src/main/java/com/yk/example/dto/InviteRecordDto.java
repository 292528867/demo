package com.yk.example.dto;

import java.util.Date;

/**
 * Created by yk on 2018/4/16.
 */
public class InviteRecordDto {

    private String phone;

    private String progress;

    private Date time;

    private double money;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
