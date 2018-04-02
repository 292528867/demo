package com.yk.example.entity;

import javax.persistence.Column;

/**
 * 用户定位历史记录
 * Created by yk on 2018/4/2.
 */
public class UserLocationHistory extends BaseEntity {

    @Column(name = "longitude", columnDefinition = " numeric(10,5) comment '经度' ")
    private String longitude;

    @Column(name = "latitude", columnDefinition = " numeric(10,5) comment '纬度' ")
    private String latitude;

    @Column(name = "address", columnDefinition = " varchar(255) comment '地址' ")
    private String address;


    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}