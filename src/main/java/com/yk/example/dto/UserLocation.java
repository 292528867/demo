package com.yk.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by yk on 2018/4/3.
 */
@ApiModel(value="user定位对象",description="user定位对象")
public class UserLocation implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="经度",name="longitude")
    private double longitude;

    @ApiModelProperty(value="纬度",name="latitude")
    private double latitude;

    @ApiModelProperty(value="用户id",name="userId")
    private String userId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
