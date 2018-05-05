package com.yk.example.entity;

import com.yk.example.enums.FriendStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户好友关系
 * Created by yk on 2018/4/8.
 */
@Entity
@Table(name = "t_user_friend")
public class UserFriend extends BaseEntity {

    @Column(name = "from_user", columnDefinition = "varchar(50) comment '请求人用户id' ")
    private String fromUser;

    @Column(name = "to_user", columnDefinition = "varchar(50) comment '被加好友用户id' ")
    private String toUser;


    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }


}
