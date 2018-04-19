package com.yk.example.service;

import com.yk.example.dao.VideoRateDao;
import com.yk.example.entity.User;
import com.yk.example.entity.VideoRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yk on 2018/4/19.
 */
@Service
public class VideoRateService {

    @Autowired
    private VideoRateDao videoRateDao;

    public void save(VideoRate rate) {
        videoRateDao.save(rate);
    }

    public VideoRate findByUserAndVideo(VideoRate rate) {
        return videoRateDao.findByUserAndVideo(rate.getUser().getUserId(),rate.getVideoRecord().getId());
    }
}
