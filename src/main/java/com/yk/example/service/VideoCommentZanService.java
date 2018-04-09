package com.yk.example.service;

import com.yk.example.dao.UserDao;
import com.yk.example.dao.VideoCommentDao;
import com.yk.example.dao.VideoCommentZanDao;
import com.yk.example.dao.ZanRecordHistoryDao;
import com.yk.example.entity.*;
import com.yk.example.enums.ZanStatus;
import com.yk.example.enums.ZanType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yk on 2018/4/8.
 */
@Service
public class VideoCommentZanService {

    @Autowired
    private VideoCommentZanDao videoCommentZanDao;

    @Autowired
    private VideoCommentDao videoCommentDao;

    @Autowired
    private ZanRecordHistoryDao zanRecordHistoryDao;

    @Autowired
    private UserDao userDao;


    @Transactional(rollbackFor = Exception.class)
    public int zan(VideoCommentZan videoCommentZan) {
        VideoCommentZan commentZan = videoCommentZanDao.save(videoCommentZan);
        String commentId = videoCommentZan.getComment().getId();
        VideoComment videoComment = videoCommentDao.findOne(commentId);
        int zanNum = videoComment.getZanNum();
        if (ZanStatus.zan.equals(videoCommentZan.getZanStatus())) {
            zanNum++;
        } else {
            zanNum--;
        }
        videoCommentDao.updateZanNum(zanNum, commentId);
        // 保存点赞记录
        VideoRecord videoRecord = videoCommentZan.getComment().getVideoRecord();
        ZanRecordHistory zanRecordHistory = new ZanRecordHistory();
        zanRecordHistory.setZanType(ZanType.comment);
        zanRecordHistory.setZanStatus(videoCommentZan.getZanStatus());
        zanRecordHistory.setVideoId(videoRecord.getId());
        zanRecordHistory.setCommentId(commentId);
        zanRecordHistory.setVideoImgUrl(videoRecord.getVideoImgUrl());
        // 被点赞人id
        zanRecordHistory.setToUserId(videoCommentZan.getUser().getUserId());
        // 点赞人的信息
        User user = userDao.findOne(videoCommentZan.getUser().getUserId());
        zanRecordHistory.setFromUserId(user.getUserId());
        zanRecordHistory.setNickName(user.getNickName());
        zanRecordHistory.setHeadImgUrl(user.getHeadImgUrl());
        zanRecordHistoryDao.save(zanRecordHistory);
        return 0;
    }
}
