package com.yk.example.service;

import com.yk.example.dao.*;
import com.yk.example.dto.VideoCommentDto;
import com.yk.example.entity.*;
import com.yk.example.utils.JPushUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Autowired
    private PushSetDao pushSetDao;

    @Transactional(rollbackFor = Exception.class)
    public VideoComment save(VideoComment videoComment) {
        User user = userDao.findOne(videoComment.getUser().getUserId());
        VideoComment comment = videoCommentDao.save(videoComment);
        String videoId = videoComment.getVideoRecord().getId();
        VideoRecord video = videoDao.findOne(videoId);
        int num = video.getCommentNum() + 1;
        videoDao.updateCommentNum(num, videoId);

        PushSet pushSet = pushSetDao.findByUserId(user.getUserId());
        if(pushSet.isCommentPush()){
            // 对被评论人进行推送
            JPushUtils.sendAlias(user.getNickName() + "评论了您的视频",
                    Collections.singletonList(video.getUser().getUserId()), Collections.singletonMap("headImgUrl", user.getHeadImgUrl()));
        }
        return comment;
    }

    public Page<VideoComment> findAllByVideoId(String videoId, Pageable pageable) {
        VideoRecord videoRecord = new VideoRecord();
        videoRecord.setId(videoId);
        return videoCommentDao.findByVideoRecord(videoRecord, pageable);
    }

    public Page<VideoCommentDto> findAllByVideoIdV2(String videoId, Pageable pageable, String userId) throws Exception{
        VideoRecord videoRecord = new VideoRecord();
        videoRecord.setId(videoId);
        Page<VideoComment> videoCommentPage = videoCommentDao.findByVideoRecord(videoRecord, pageable);
        List<VideoComment> content = videoCommentPage.getContent();
        List<VideoCommentDto> videoCommentDtoList = new ArrayList<>();
        if (StringUtils.isNoneBlank(userId)) {
            User user = new User();
            user.setUserId(userId);
            List<VideoCommentZan> commentZanList = videoCommentZanDao.findByUser(user);
            if (content != null && content.size() > 0) {
                for (VideoComment comment : content) {
                    VideoCommentDto videoCommentDto = new VideoCommentDto();
                    BeanUtils.copyProperties(videoCommentDto,comment);
                    boolean flag = false;
                    for (VideoCommentZan commentZan : commentZanList) {
                        if(comment.getId().equals(commentZan.getComment().getId())){
                            flag = true;
                            videoCommentDto.setZanStatus(true);
                            break;
                        }
                    }
                    if(!flag){
                        videoCommentDto.setZanStatus(false);
                    }
                    videoCommentDtoList.add(videoCommentDto);
                }
            }
        }
        return new PageImpl<VideoCommentDto>(videoCommentDtoList,pageable,videoCommentPage.getTotalElements());
    }

    public Page<VideoComment> findAllCommentByUser(String userId, Pageable pageable) {
        User user = new User();
        user.setUserId(userId);
        List<VideoRecord> videoRecords = videoDao.findByUserAndFlag(user, "1");
        Page<VideoComment> page = videoCommentDao.findByVideoRecordIn(videoRecords, pageable);
        return page;
    }
}
