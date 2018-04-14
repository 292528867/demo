package com.yk.example.service;

import com.yk.example.dao.UserDao;
import com.yk.example.dao.VideoCollectDao;
import com.yk.example.entity.VideoCollect;
import com.yk.example.entity.VideoRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yk on 2018/4/8.
 */
@Service
public class VideoCollectService {

    @Autowired
    private VideoCollectDao videoCollectDao;

    @Autowired
    private UserDao userDao;

    public VideoCollect existCollect(String id, String userId) {
        VideoCollect videoCollect = videoCollectDao.existCollect(id, userId);
        return videoCollect;
    }

    public void save(VideoCollect videoCollect) {
        videoCollectDao.save(videoCollect);
    }

    public Page<VideoCollect> findByUserId(String userId, Pageable pageable) {
        Page<VideoCollect> collects = videoCollectDao.findByUser(userDao.findOne(userId), pageable);

        return collects;
    }

    public long countByUserId(String userId) {

        return videoCollectDao.countByUser(userDao.findOne(userId));
    }
}
