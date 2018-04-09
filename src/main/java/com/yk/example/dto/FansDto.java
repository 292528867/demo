package com.yk.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yk on 2018/4/8.
 */
@ApiModel(value = "粉丝对象", description = "粉丝对象")
public class FansDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 被关注用户图像
     */
    @ApiModelProperty(value = "被关注用户图像", name = "headImgUrl")
    private String headImgUrl;

    /**
     * 被关注用户图像
     */
    @ApiModelProperty(value = "用户昵称", name = "nickName")
    private String nickName;

    /**
     * 粉丝用户id
     */
    @ApiModelProperty(value = "粉丝用户id", name = "followId")
    private String followId;

    /**
     * 关注时间
     */
    @ApiModelProperty(value = "经度", name = "longitude")
    private Date createTime;

    /**
     * 是否关注
     */
    @ApiModelProperty(value = "是否关注", name = "isFollow")
    private boolean isFollow;


    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getFollowId() {
        return followId;
    }

    public void setFollowId(String followId) {
        this.followId = followId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
