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

    @Column(name = "zan_num",columnDefinition = " int comment '点赞数目'")
    private int zanNum;

    @ManyToOne()
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "video_id")
    private VideoRecord videoRecord;


    public VideoRecord getVideoRecord() {
        return videoRecord;
    }

    public void setVideoRecord(VideoRecord videoRecord) {
        this.videoRecord = videoRecord;
    }

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
