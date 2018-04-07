package com.yk.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yk.example.enums.Sex;
import com.yk.example.enums.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/8.
 */
@ApiModel(value = "user登录对象", description = "用户登录对象user")
@Entity
@Table(name = "t_user")

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String userId;

    @ApiModelProperty(value = "手机号", name = "phone")
    @Column(name = "phone", columnDefinition = "varchar(50) COMMENT '手机号码' ")
    private String phone;

    @ApiModelProperty(value = "密码", name = "password")
    @Column(name = "password", columnDefinition = "varchar(100) COMMENT '密码' ")
    private String password;

    @ApiModelProperty(value = "nick_name", name = "nickName")
    @Column(name = "nick_name", columnDefinition = "varchar(100) COMMENT '用户名' ")
    private String nickName;

    @ApiModelProperty(value = "用户图像", name = "headImgUrl")
    @Column(name = "head_img_url", columnDefinition = "varchar(255) COMMENT '用户图像' ")
    private String headImgUrl;

    @ApiModelProperty(value = "用户性别", name = "sex")
    @Column(name = "sex", columnDefinition = "varchar(100) COMMENT '0为男性，1为女性,2未知' ")
    private Sex sex;

    @ApiModelProperty(value = "第三方id", name = "thirdUserId")
    @Column(name = "third_user_id", columnDefinition = "varchar(100) COMMENT '第三方平台唯一标识符' ")
    private String thirdUserId;

    @ApiModelProperty(value = "用户注册类型", name = "userType")
    @Column(name = "user_type", columnDefinition = "varchar(2) COMMENT '用户类型 0 app用户 1 微信 2 qq 3 微博' ")
    private UserType userType;

    @ApiModelProperty(value = "用户在融云的唯一token", name = "rongCloudToken")
    @Column(name = "rong_cloud_token", columnDefinition = "varchar(255) COMMENT '用户在融云的唯一token' ")
    private String rongCloudToken;

    @Column(name = "is_valid", columnDefinition = "varchar(2) COMMENT '用户类型 0 有效用户 1 无效用户' ")
    private String isValid = "0";

    @ApiModelProperty(value = "直接推荐人", name = "directRecommendUser")
    @Column(name = "direct_recommend_user", columnDefinition = "varchar(100) COMMENT '直接推荐人' ")
    private String directRecommendUser;

    @Column(name = "space_recommend_user", columnDefinition = "varchar(100) COMMENT '推荐人的推荐人' ")
    private String spaceRecommendUser;

    @CreationTimestamp
    private Date createTime;

    @UpdateTimestamp
    private Date updateTime;


    /**
     * 验证码 （非用户表中字段）
     */
    @ApiModelProperty(value = "验证码", name = "code")
    @Transient
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRongCloudToken() {
        return rongCloudToken;
    }

    public void setRongCloudToken(String rongCloudToken) {
        this.rongCloudToken = rongCloudToken;
    }

    public String getDirectRecommendUser() {
        return directRecommendUser;
    }

    public void setDirectRecommendUser(String directRecommendUser) {
        this.directRecommendUser = directRecommendUser;
    }

    public String getSpaceRecommendUser() {
        return spaceRecommendUser;
    }

    public void setSpaceRecommendUser(String spaceRecommendUser) {
        this.spaceRecommendUser = spaceRecommendUser;
    }
}
