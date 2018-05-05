package com.yk.example.service;

import com.yk.example.dao.UserDao;
import com.yk.example.dao.VideoCommentDao;
import com.yk.example.dao.VideoCommentZanDao;
import com.yk.example.dao.VideoDao;
import com.yk.example.entity.User;
import com.yk.example.entity.VideoComment;
import com.yk.example.entity.VideoCommentZan;
import com.yk.example.entity.VideoRecord;
import com.yk.example.utils.JPushUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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

    @Autowired
    private UserDao userDao;

    @Autowired
    private VideoCommentZanDao videoCommentZanDao;

    @Transactional(rollbackFor = Exception.class)
    public VideoComment save(VideoComment videoComment) {
        User user = userDao.findOne(videoComment.getUser().getUserId());
        VideoComment comment = videoCommentDao.save(videoComment);
        String videoId = videoComment.getVideoRecord().getId();
        VideoRecord video = videoDao.findOne(videoId);
        int num = video.getCommentNum() + 1;
        videoDao.updateCommentNum(num, videoId);
        // 对被评论人进行推送
        JPushUtils.sendAlias(user.getNickName() + "评论了您的视频",
                Collections.singletonList(video.getUser().getUserId()), Collections.singletonMap("videoId", videoId));
        return comment;
    }

    public Page<VideoComment> findAllByVideoId(String videoId, Pageable pageable, String userId) {
        VideoRecord videoRecord = new VideoRecord();
        videoRecord.setId(videoId);
        Page<VideoComment> videoCommentPage = videoCommentDao.findByVideoRecord(videoRecord, pageable);
        List<VideoComment> content = videoCommentPage.getContent();
        if (StringUtils.isNoneBlank(userId)) {
            User user = new User();
            user.setUserId(userId);
            List<VideoCommentZan> commentZanList = videoCommentZanDao.findByUser(user);
            if (content != null && content.size() > 0) {
                for (VideoComment comment : content) {
                    boolean flag = false;
                    for (VideoCommentZan commentZan : commentZanList) {
                        if(comment.getId().equals(commentZan.getComment().getId())){
                            flag = true;
                            comment.setZanStatus(true);
                            break;
                        }
                    }
                    if(!flag){
                        comment.setZanStatus(false);
                    }
                }
            }
        }
        return new PageImpl<VideoComment>(content,pageable,videoCommentPage.getTotalElements());
    }

    public Page<VideoComment> findAllCommentByUser(String userId, Pageable pageable) {
        User user = new User();
        user.setUserId(userId);
        List<VideoRecord> videoRecords = videoDao.findByUserAndFlag(user, "1");
        Page<VideoComment> page = videoCommentDao.findByVideoRecordIn(videoRecords, pageable);
        return page;
    }
}
