package com.yk.example.entity;

import com.yk.example.enums.Sex;
import com.yk.example.enums.UserType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/8.
 */
@Entity
@Table(name = "t_user")

public class User {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String userId;

    @Column(name = "phone", columnDefinition = "varchar(50) COMMENT '手机号码'")
    private String phone;

    @Column(name = "password", columnDefinition = "varchar(50) COMMENT '密码'")
    private String password;

    @Column(name = "nick_name", columnDefinition = "varchar(100) COMMENT '用户名'")
    private String nickName;

    @Column(name = "head_img_url", columnDefinition = "varchar(100) COMMENT '用户图像'")
    private String headImgUrl;

    @Column(name = "sex", columnDefinition = "varchar(100) COMMENT '0为男性，1为女性,2未知'")
    private Sex sex;

    @Column(name = "third_user_id", columnDefinition = "varchar(100) COMMENT '第三方平台唯一标识符'")
    private String thirdUserId;

    @Column(name = "user_type", columnDefinition = "varchar(100) COMMENT '用户类型 0 app用户 1 微信 2 qq 3 微博'")
    private UserType userType;

    @CreationTimestamp
    private Date createTime;

    @UpdateTimestamp
    private Date updateTime;

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
}
