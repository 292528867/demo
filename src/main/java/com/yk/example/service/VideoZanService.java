package com.yk.example.service;

import com.yk.example.dao.*;
import com.yk.example.entity.*;
import com.yk.example.enums.ZanStatus;
import com.yk.example.enums.ZanType;
import com.yk.example.utils.JPushUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;

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

    @Autowired
    private UserInfoDao userInfoDao;

    @Transactional(rollbackFor = Exception.class)
    public VideoZan save(VideoZan videoZan) {
        VideoZan zan = videoZanDao.save(videoZan);
        String videoId = videoZan.getVideoRecord().getId();
        VideoRecord videoRecord = videoDao.findOne(videoId);
        UserInfo userInfo = userInfoDao.findByUser(videoZan.getUser());
        int zanNum = videoRecord.getZanNum();
        long userZanNum = userInfo.getZanNum();
        if (videoZan.getZanStatus().equals(ZanStatus.zan)) {
            zanNum += 1;
            userZanNum += 1;
        } else {
            zanNum -= 1;
            userZanNum += 1;
        }
        videoDao.updateZanNum(zanNum, videoId);
        userInfo.setZanNum(userZanNum);
        userInfoDao.save(userInfo);
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
        // 对被点赞人进行推送
        if (videoZan.getZanStatus().equals(ZanStatus.zan)) {
            JPushUtils.sendAlias(user.getNickName() + "在" + new DateTime(new Date()).toString("yyyy-MM-dd hh:mm") + "点赞您的视频",
                    Collections.singletonList(videoRecord.getUser().getUserId()), Collections.singletonMap("videoId", videoRecord.getId()));
        }
        return zan;
    }

    public VideoZan findByUserAndVideo(VideoZan videoZan) {
        return videoZanDao.findByUserAndVideoRecord(videoZan.getUser(),videoZan.getVideoRecord());
    }
}
