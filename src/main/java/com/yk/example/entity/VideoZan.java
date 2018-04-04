package com.yk.example.entity;

import com.yk.example.enums.ZanStatus;

import javax.persistence.*;

/**
 *  视频点赞表
 * Created by yk on 2018/4/4.
 */
@Entity
@Table(name = "t_video_zan")
public class VideoZan extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private VideoRecord videoRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "zanStatus" , columnDefinition = " varchar(2) comment '点赞状态 0 点赞 1 取消点赞' ")
    private ZanStatus zanStatus;

    public VideoRecord getVideoRecord() {
        return videoRecord;
    }

    public void setVideoRecord(VideoRecord videoRecord) {
        this.videoRecord = videoRecord;
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
