package com.yk.example.dto;

import com.yk.example.enums.Sex;
import sun.rmi.runtime.Log;

import java.io.Serializable;

/**
 * Created by yk on 2018/4/11.
 */
public class UserInfoDto implements Serializable {

    private String userId;

    private String nickName;

    private String headImgUrl;

    private Sex sex;

    private String birth;

    private String personalSign;

    private String profession;

    private String impressionLabel;

    private String address;

    private float accountIncome;

    private float miaoPeas;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
