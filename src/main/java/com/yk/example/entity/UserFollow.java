package com.yk.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户关注表
 * Created by yk on 2018/4/2.
 */
@Entity
@Table(name = "t_user_follow")
public class UserFollow extends BaseEntity {

    @Column(name = "user_id", columnDefinition = " varchar(50) comment '关注用户id'")
    private String userId;

    @Column(name = "follow_id", columnDefinition = " varchar(50) comment '被关注用户id'")
    private String followId;

    @Column(name = "status", columnDefinition = " varchar(50) comment '关注1,取消 0'")
    private boolean status;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFollowId() {
        return followId;
    }

    public void setFollowId(String followId) {
        this.followId = followId;
    }
}
