package com.yk.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by yk on 2018/5/2.
 */
@Entity
@Table(name = "t_push_set")
public class PushSet extends BaseEntity {

    @Column(name = "zan_push", columnDefinition = "tinyint(1) NOT NULL DEFAULT '1' comment '是否推送点赞'")
    private boolean zanPush ;

    @Column(name = "follow_push", columnDefinition = "tinyint(1) NOT NULL DEFAULT '1' comment '是否推送关注'")
    private boolean followPush ;

    @Column(name = "comment_push", columnDefinition = "tinyint(1) NOT NULL DEFAULT '1' comment '是否推送评论'")
    private boolean commentPush ;

    @Column(name = "video_follow_push", columnDefinition = "tinyint(1) NOT NULL DEFAULT '1' comment '是否推送关注人视频'")
    private boolean videoFollowPush ;

    @Column(name = "user_id", columnDefinition = "varchar(32) comment '用户id'")
    private String userId;

    public boolean isZanPush() {
        return zanPush;
    }

    public void setZanPush(boolean zanPush) {
        this.zanPush = zanPush;
    }

    public boolean isFollowPush() {
        return followPush;
    }

    public void setFollowPush(boolean followPush) {
        this.followPush = followPush;
    }

    public boolean isCommentPush() {
        return commentPush;
    }

    public void setCommentPush(boolean commentPush) {
        this.commentPush = commentPush;
    }

    public boolean isVideoFollowPush() {
        return videoFollowPush;
    }

    public void setVideoFollowPush(boolean videoFollowPush) {
        this.videoFollowPush = videoFollowPush;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
