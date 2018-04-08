package com.yk.example.entity;

import com.yk.example.enums.ZanStatus;

import javax.persistence.*;

/**
 * 评论点赞实体
 * Created by yk on 2018/4/8.
 */
@Entity
@Table(name = "t_video_comment_zan")
public class VideoCommentZan extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private VideoComment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "zanStatus" , columnDefinition = " varchar(2) comment '点赞状态 0 点赞 1 取消点赞' ")
    private ZanStatus zanStatus;


    public VideoComment getComment() {
        return comment;
    }

    public void setComment(VideoComment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ZanStatus getZanStatus() {
        return zanStatus;
    }

    public void setZanStatus(ZanStatus zanStatus) {
        this.zanStatus = zanStatus;
    }
}
