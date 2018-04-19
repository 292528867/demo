package com.yk.example.entity;

import javax.persistence.*;

/**
 * 视频评级
 * Created by yk on 2018/4/19.
 */
@Entity
@Table(name = "t_video_rate")
public class VideoRate extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private VideoRecord videoRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "rate",columnDefinition = "varchar(2) comment '0 正能量 1 内容一般 2 负能量'")
    private String rate;

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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
