package com.yk.example.service;

import com.yk.example.dao.VideoDao;
import com.yk.example.dao.VideoZanDao;
import com.yk.example.entity.VideoRecord;
import com.yk.example.entity.VideoZan;
import com.yk.example.enums.ZanStatus;
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
        return zan;
    }
}
