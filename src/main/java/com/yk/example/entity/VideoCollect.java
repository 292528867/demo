package com.yk.example.entity;

import javax.persistence.*;

/**
 * 秒转视频记录表
 * Created by yk on 2018/4/8.
 */
@Entity
@Table(name = "t_video_collect")
public class VideoCollect extends BaseEntity{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "video_id")
    private VideoRecord videoRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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
}
