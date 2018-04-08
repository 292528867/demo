package com.yk.example.service;

import com.yk.example.dao.VideoCollectDao;
import com.yk.example.entity.VideoCollect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yk on 2018/4/8.
 */
@Service
public class VideoCollectService {

    @Autowired
    private VideoCollectDao videoCollectDao;

    public VideoCollect existCollect(String id, String userId) {
        VideoCollect videoCollect =   videoCollectDao.existCollect(id,userId);
        return videoCollect;
    }

    public void save(VideoCollect videoCollect) {
        videoCollectDao.save(videoCollect);
    }
}
