package com.yk.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by yk on 2018/4/3.
 */
@ApiModel(value="user定位对象",description="user定位对象")
public class UserLocation implements Serializable {

    @ApiModelProperty(value="经度",name="longitude")
    private String longitude;

    @ApiModelProperty(value="纬度",name="latitude")
    private String latitude;

    @ApiModelProperty(value="用户id",name="userId")
    private String userId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
