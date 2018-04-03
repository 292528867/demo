package com.yk.example.entity;

import javax.persistence.*;

/**
 * 视频记录表
 * Created by yk on 2018/4/2.
 */
@Entity
@Table(name = "t_video_record")
public class VideoRecord extends BaseEntity {

    @Column(name = "title", columnDefinition = "  varchar(255) comment '视频标题' ")
    private String title;

    @Column(name = "longitude", columnDefinition = " numeric(10,5) comment '经度' ")
    private String longitude;

    @Column(name = "latitude", columnDefinition = " numeric(10,5) comment '纬度' ")
    private String latitude;

    @Column(name = "video_url", columnDefinition = " varchar(255) comment '视频url' ")
    private String videoUrl;

    @Column(name = "music_name", columnDefinition = "varchar(50) comment '音乐名称'")
    private String musicName;

    @Column(name = "music_url", columnDefinition = " varchar(255) comment '音乐url' ")
    private String musicUrl;

    @Column(name = "zan_num", columnDefinition = " numeric(10,0) comment '点赞数目' ")
    private String zanNum;

    @Column(name = "comment_num", columnDefinition = " numeric(10,0) comment '评论数目' ")
    private String commentNum;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "tag_id")
    private VideoTag tag;

    @Column(name = "status", columnDefinition = " varchar(2) comment '0 草稿 1 发布' ")
    private String status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getZanNum() {
        return zanNum;
    }

    public void setZanNum(String zanNum) {
        this.zanNum = zanNum;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public VideoTag getTag() {
        return tag;
    }

    public void setTag(VideoTag tag) {
        this.tag = tag;
    }
}
