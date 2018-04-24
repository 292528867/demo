package com.yk.example.service;

import com.yk.example.dao.VideoCommentDao;
import com.yk.example.dao.VideoDao;
import com.yk.example.entity.User;
import com.yk.example.entity.VideoComment;
import com.yk.example.entity.VideoRecord;
import com.yk.example.utils.JPushUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by yk on 2018/4/4.
 */
@Service
public class VideoCommentService {

    @Autowired
    private VideoCommentDao videoCommentDao;

    @Autowired
    private VideoDao videoDao;

    @Transactional(rollbackFor = Exception.class)
    public VideoComment save(VideoComment videoComment) {
        VideoComment comment = videoCommentDao.save(videoComment);
        String videoId = videoComment.getVideoRecord().getId();
        VideoRecord video = videoDao.findOne(videoId);
        int num = video.getCommentNum() + 1;
        videoDao.updateCommentNum(num, videoId);
        // 对被评论人进行推送
        JPushUtils.sendAlias(videoComment.getUser().getNickName() + "在" + new DateTime(new Date()).toString("yyyy-MM-dd hh:mm") + "评论了您的视频",
                Collections.singletonList(video.getUser().getUserId()), Collections.singletonMap("videoId", videoId));
        return comment;
    }

    public Page<VideoComment> findAllByVideoId(String videoId, Pageable pageable) {
        VideoRecord videoRecord = new VideoRecord();
        videoRecord.setId(videoId);
        return videoCommentDao.findByVideoRecord(videoRecord, pageable);
    }

    public Page<VideoComment> findAllCommentByUser(String userId, Pageable pageable) {
        User user = new User();
        user.setUserId(userId);
        List<VideoRecord> videoRecords = videoDao.findByUserAndFlag(user, "1");
        Page<VideoComment> page = videoCommentDao.findByVideoRecordIn(videoRecords, pageable);
        return page;
    }
}
