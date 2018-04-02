package com.yk.example.entity;

import javax.persistence.*;

/**
 * 视频评论表
 * Created by yk on 2018/4/2.
 */
@Entity
@Table(name = "t_video_comment")
public class VideoComment extends BaseEntity {

    @Column(name = "content",columnDefinition = " varchar(255) comment '评论内容'")
    private String content;

    @Column(name = "zan_num",columnDefinition = " int comment '评论内容'")
    private int zanNum;

    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name="user_id")
    private User user;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getZanNum() {
        return zanNum;
    }

    public void setZanNum(int zanNum) {
        this.zanNum = zanNum;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
