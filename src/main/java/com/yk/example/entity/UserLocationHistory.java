package com.yk.example.entity;

import javax.persistence.*;

/**
 * 用户定位历史记录
 * Created by yk on 2018/4/2.
 */
@Entity
@Table(name = "t_user_location_history")
public class UserLocationHistory extends BaseEntity {

    @Column(name = "longitude", columnDefinition = " numeric(10,5) comment '经度' ")
    private double longitude;

    @Column(name = "latitude", columnDefinition = " numeric(10,5) comment '纬度' ")
    private double latitude;

    @Column(name = "address", columnDefinition = " varchar(255) comment '地址' ")
    private String address;

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = " varchar(50) comment '用户id ' ")
    private User user;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}