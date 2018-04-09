package com.yk.example.service;

import com.yk.example.dao.UserDao;
import com.yk.example.dao.VideoDao;
import com.yk.example.dao.VideoZanDao;
import com.yk.example.dao.ZanRecordHistoryDao;
import com.yk.example.entity.User;
import com.yk.example.entity.VideoRecord;
import com.yk.example.entity.VideoZan;
import com.yk.example.entity.ZanRecordHistory;
import com.yk.example.enums.ZanStatus;
import com.yk.example.enums.ZanType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yk on 2018/4/4.
 */
@Service
public class VideoZanService {

    @Autowired
    private VideoZanDao videoZanDao;

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private ZanRecordHistoryDao zanRecordHistoryDao;

    @Autowired
    private UserDao userDao;

    @Transactional(rollbackFor = Exception.class)
    public VideoZan save(VideoZan videoZan) {
        VideoZan zan = videoZanDao.save(videoZan);
        String videoId = videoZan.getVideoRecord().getId();
        VideoRecord videoRecord = videoDao.findOne(videoId);
        int zanNum = videoRecord.getZanNum();
        if (videoZan.getZanStatus().equals(ZanStatus.zan)) {
            zanNum += 1;
        } else {
            zanNum -= 1;
        }
        videoDao.updateZanNum(zanNum, videoId);
        // 保存点赞记录
        ZanRecordHistory zanRecordHistory = new ZanRecordHistory();
        zanRecordHistory.setZanType(ZanType.video);
        zanRecordHistory.setZanStatus(videoZan.getZanStatus());
        zanRecordHistory.setVideoId(videoId);
        zanRecordHistory.setVideoImgUrl(videoRecord.getVideoImgUrl());
        // 被点赞人id
        zanRecordHistory.setToUserId(videoRecord.getUser().getUserId());
        // 点赞人的信息
        User user = userDao.findOne(videoZan.getUser().getUserId());
        zanRecordHistory.setFromUserId(user.getUserId());
        zanRecordHistory.setNickName(user.getNickName());
        zanRecordHistory.setHeadImgUrl(user.getHeadImgUrl());
        zanRecordHistoryDao.save(zanRecordHistory);
        return zan;
    }
}
