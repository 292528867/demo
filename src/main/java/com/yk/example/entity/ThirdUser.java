package com.yk.example.entity;

import com.yk.example.enums.UserType;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 *  第三方用户
 * Created by yk on 2018/5/2.
 */
@Entity
@Table(name = "t_third_user")
public class ThirdUser extends BaseEntity{

    @ApiModelProperty(value = "nick_name", name = "nickName")
    @Column(name = "nick_name", columnDefinition = "varchar(100) COMMENT '用户名' ")
    private String nickName;

    @ApiModelProperty(value = "用户图像", name = "headImgUrl")
    @Column(name = "head_img_url", columnDefinition = "varchar(255) COMMENT '用户图像' ")
    private String headImgUrl;

    @ApiModelProperty(value = "第三方id", name = "thirdUserId")
    @Column(name = "third_user_id", columnDefinition = "varchar(100) COMMENT '第三方平台唯一标识符' ")
    private String thirdUserId;

    @ApiModelProperty(value = "用户注册类型", name = "userType")
    @Column(name = "user_type", columnDefinition = "varchar(2) COMMENT '用户类型 0 app用户 1 微信 2 qq 3 微博' ")
    private UserType userType;

    @Column(name = "status", columnDefinition = "varchar(2) COMMENT '绑定状态 0 绑定 1 解绑' ")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = " varchar(50) comment '用户id ' ")
    private User user;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getThirdUserId() {
        return thirdUserId;
    }

    public void setThirdUserId(String thirdUserId) {
        this.thirdUserId = thirdUserId;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
