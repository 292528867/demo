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

    @Column(name = "head_img_url", columnDefinition = "varchar(255) COMMENT '请求人的用户图像' ")
    private String headImgUrl;

    @Column(name = "from_content", columnDefinition = "varchar(255) comment '请求内容描述' ")
    private String fromContent;

    @Column(name = "friend_status", columnDefinition = "varchar(2) comment '好友状态  0等待同意 1 同意 2 拒绝 3 忽略 ' ")
    private FriendStatus friendStatus;

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

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getFromContent() {
        return fromContent;
    }

    public void setFromContent(String fromContent) {
        this.fromContent = fromContent;
    }

    public FriendStatus getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(FriendStatus friendStatus) {
        this.friendStatus = friendStatus;
    }
}
