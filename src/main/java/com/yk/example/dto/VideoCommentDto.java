package com.yk.example.dto;

import com.yk.example.entity.User;
import com.yk.example.entity.VideoRecord;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

/**
 * @author yk
 * @date 2018/5/612:48
 */
public class VideoCommentDto {

    private String content;

    private int zanNum;

    private User user;

    private VideoRecord videoRecord;

    private boolean zanStatus = false;

    private String id;

    private Date createTime;

    private Date updateTime;

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

    public VideoRecord getVideoRecord() {
        return videoRecord;
    }

    public void setVideoRecord(VideoRecord videoRecord) {
        this.videoRecord = videoRecord;
    }

    public boolean isZanStatus() {
        return zanStatus;
    }

    public void setZanStatus(boolean zanStatus) {
        this.zanStatus = zanStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}