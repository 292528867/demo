package com.yk.example.entity;

import com.yk.example.enums.PriceType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 手机彩铃
 * Created by Administrator on 2017/8/22.
 */
@Entity
public class MobileCrbt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String crbtName; //彩铃名称

    private String picPath; //彩铃图片路径

    private String crbtDesc; // 描述

    private String price1; //价格1

    private String price2; //价格2

    private String price3; //价格3

    private String playUrl; //播放url

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCrbtName() {
        return crbtName;
    }

    public void setCrbtName(String crbtName) {
        this.crbtName = crbtName;
    }

    public String getCrbtDesc() {
        return crbtDesc;
    }

    public void setCrbtDesc(String crbtDesc) {
        this.crbtDesc = crbtDesc;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }


    public String getPrice1() {
        return price1;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public String getPrice2() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }

    public String getPrice3() {
        return price3;
    }

    public void setPrice3(String price3) {
        this.price3 = price3;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }
}
