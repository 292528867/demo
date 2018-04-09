package com.yk.example.entity;

import com.yk.example.enums.ZanStatus;
import com.yk.example.enums.ZanType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 点赞历史记录表
 * Created by yk on 2018/4/9.
 */
@Entity
@Table(name = "t_zan_record")
public class ZanRecordHistory extends BaseEntity {

    @Column(name = "from_user_id", columnDefinition = "varchar(50) comment '点赞用户id'")
    private String fromUserId;

    @Column(name = "to_user_id", columnDefinition = "varchar(50) comment '被点赞用户id'")
    private String toUserId;

    @Column(name = "nick_name", columnDefinition = "varchar(100) COMMENT '点赞用户名' ")
    private String nickName;

    @Column(name = "head_img_url", columnDefinition = "varchar(255) COMMENT '点赞用户图像' ")
    private String headImgUrl;


    @Column(name = "comment_id", columnDefinition = "varchar(50) comment '被点赞用户评论id'")
    private String commentId;

    @Column(name = "video_id", columnDefinition = "varchar(50) comment '被点赞用户视频id'")
    private String videoId;

    @Column(name = "video_img_url", columnDefinition = "varchar(255) comment '被点赞用户视频图片url'")
    private String videoImgUrl;

    @Column(name = "zan_type", columnDefinition = "varchar(2) comment '0 点赞的是视频 1 点赞评论'")
    private ZanType zanType;

    @Column(name = "zanStatus" , columnDefinition = " varchar(2) comment '点赞状态 0 点赞 1 取消点赞' ")
    private ZanStatus zanStatus;


    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoImgUrl() {
        return videoImgUrl;
    }

    public void setVideoImgUrl(String videoImgUrl) {
        this.videoImgUrl = videoImgUrl;
    }

    public ZanType getZanType() {
        return zanType;
    }

    public void setZanType(ZanType zanType) {
        this.zanType = zanType;
    }

    public ZanStatus getZanStatus() {
        return zanStatus;
    }

    public void setZanStatus(ZanStatus zanStatus) {
        this.zanStatus = zanStatus;
    }
}
