package com.yk.example.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户基本信息
 * Created by yk on 2018/4/2.
 */
@Entity
@Table(name = "t_user_info")
public class UserInfo {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 32)
    private String id;

    @Column(name = "last_longitude", columnDefinition = " numeric(10,5) comment '最后的经度' ")
    private double lastLongitude;

    @Column(name = "last_latitude", columnDefinition = " numeric(10,5) comment '最后的纬度' ")
    private double lastLatitude;

    @Column(name = "fan_num", columnDefinition = " bigint  comment '粉丝数量' ")
    private long fanNum;

    @Column(name = "follow_num", columnDefinition = " bigint  comment '关注数量' ")
    private long followNum;

    @Column(name = "zan_num", columnDefinition = " bigint  comment '赞的数量' ")
    private long zanNum;

    @Column(name = "address", columnDefinition = " varchar(100)  comment '所在地' ")
    private String address;

    @Column(name = "birth", columnDefinition = " varchar(20)  comment '生日' ")
    private String birth;

    @Column(name = "personal_sign", columnDefinition = " varchar(255)  comment '个人签名' ")
    private String personalSign;

    @Column(name = "profession ", columnDefinition = " varchar(255)  comment '我的职业' ")
    private String profession;

    @Column(name = "impression_label ", columnDefinition = " varchar(255)  comment '印象标签' ")
    private String impressionLabel;


    @Column(name = "account_income", columnDefinition = " decimal(10,2) comment '账号收入'")
    private float accountIncome;

    @Column(name = "miao_peas", columnDefinition = " decimal(10,2) comment '秒豆'")
    private float miaoPeas;

    @OneToOne()
    @JoinColumn(name = "user_id", columnDefinition = " varchar(50) comment '用户id ' ")
    private User user;


    @CreationTimestamp
    private Date createTime;

    @UpdateTimestamp
    private Date updateTime;

    public float getAccountIncome() {
        return accountIncome;
    }

    public void setAccountIncome(float accountIncome) {
        this.accountIncome = accountIncome;
    }

    public float getMiaoPeas() {
        return miaoPeas;
    }

    public void setMiaoPeas(float miaoPeas) {
        this.miaoPeas = miaoPeas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLastLongitude() {
        return lastLongitude;
    }

    public void setLastLongitude(double lastLongitude) {
        this.lastLongitude = lastLongitude;
    }

    public double getLastLatitude() {
        return lastLatitude;
    }

    public void setLastLatitude(double lastLatitude) {
        this.lastLatitude = lastLatitude;
    }

    public long getFanNum() {
        return fanNum;
    }

    public void setFanNum(long fanNum) {
        this.fanNum = fanNum;
    }

    public long getFollowNum() {
        return followNum;
    }

    public void setFollowNum(long followNum) {
        this.followNum = followNum;
    }

    public long getZanNum() {
        return zanNum;
    }

    public void setZanNum(long zanNum) {
        this.zanNum = zanNum;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPersonalSign() {
        return personalSign;
    }

    public void setPersonalSign(String personalSign) {
        this.personalSign = personalSign;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getImpressionLabel() {
        return impressionLabel;
    }

    public void setImpressionLabel(String impressionLabel) {
        this.impressionLabel = impressionLabel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
