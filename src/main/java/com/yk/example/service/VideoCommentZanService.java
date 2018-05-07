package com.yk.example.service;

import com.yk.example.dao.UserDao;
import com.yk.example.dao.VideoCommentDao;
import com.yk.example.dao.VideoCommentZanDao;
import com.yk.example.dao.ZanRecordHistoryDao;
import com.yk.example.entity.*;
import com.yk.example.enums.ZanStatus;
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
            // 保存点赞记录
            videoCommentZanDao.save(videoCommentZan);
        } else {
            zanNum--;
            // 删除点赞记录
            videoCommentZanDao.deleteByUserAndComment(videoCommentZan.getUser(),videoCommentZan.getComment());
        }
        videoCommentDao.updateZanNum(zanNum, commentId);

        return 0;
    }

    public VideoCommentZan findByUserAndComment(User user, VideoComment comment) {
        return videoCommentZanDao.findByUserAndComment(user,comment);
    }
}
