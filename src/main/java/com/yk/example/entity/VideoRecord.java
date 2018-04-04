package com.yk.example.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * 视频记录表
 * Created by yk on 2018/4/2.
 */
@ApiModel(value = "视频实体", description = "视频对象")
@Entity
@Table(name = "t_video_record")
public class VideoRecord extends BaseEntity {

    @ApiModelProperty(value = "视频标题", name = "title")
    @Column(name = "title", columnDefinition = "  varchar(255) comment '视频标题' ")
    private String title;

    @Column(name = "longitude", columnDefinition = " numeric(10,5) comment '经度' ")
    private String longitude;

    @Column(name = "latitude", columnDefinition = " numeric(10,5) comment '纬度' ")
    private String latitude;

    @ApiModelProperty(value = "视频url", name = "videoUrl")
    @Column(name = "video_url", columnDefinition = " varchar(255) comment '视频url' ")
    private String videoUrl;

    @ApiModelProperty(value = "音乐名称", name = "musicName")
    @Column(name = "music_name", columnDefinition = "varchar(50) comment '音乐名称'")
    private String musicName;

    @ApiModelProperty(value = "音乐url", name = "musicUrl")
    @Column(name = "music_url", columnDefinition = " varchar(255) comment '音乐url' ")
    private String musicUrl;

    @Column(name = "zan_num", columnDefinition = " numeric(10,0) comment '点赞数目' ")
    private int zanNum;

    @Column(name = "comment_num", columnDefinition = " numeric(10,0) comment '评论数目' ")
    private int commentNum;


    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "tag_id")
    private VideoTag tag;

    @ApiModelProperty(value = "0 草稿 1 发布", name = "status")
    @Column(name = "status", columnDefinition = " varchar(2) comment '0 草稿 1 发布' ")
    private String status;


    // 用户是否关注
    @Transient
    private boolean isFollow;

    // 用户是否点赞
    @Transient
    private boolean isZan;


    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public boolean isZan() {
        return isZan;
    }

    public void setZan(boolean zan) {
        isZan = zan;
    }

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

    public int getZanNum() {
        return zanNum;
    }

    public void setZanNum(int zanNum) {
        this.zanNum = zanNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
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