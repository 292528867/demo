package com.yk.example.service;

import com.yk.example.dao.VideoCommentDao;
import com.yk.example.dao.VideoDao;
import com.yk.example.entity.VideoComment;
import com.yk.example.entity.VideoRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        videoDao.updateCommentNum(num,videoId);
        return comment;
    }

    public List<VideoComment> findAllByVideoId(String videoId) {
        return videoCommentDao.queryVideoId(videoId);
    }
}
