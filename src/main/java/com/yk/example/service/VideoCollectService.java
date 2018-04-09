package com.yk.example.service;

import com.yk.example.dao.VideoCollectDao;
import com.yk.example.entity.VideoCollect;
import com.yk.example.entity.VideoRecord;
import org.springframework.beans.factory.annotation.Autowired;
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

    public VideoCollect existCollect(String id, String userId) {
        VideoCollect videoCollect = videoCollectDao.existCollect(id, userId);
        return videoCollect;
    }

    public void save(VideoCollect videoCollect) {
        videoCollectDao.save(videoCollect);
    }

    public List<VideoRecord> findByUserId(String userId) {
        List<VideoRecord> videoRecords = new ArrayList<>();
        List<VideoCollect> collects = videoCollectDao.findByUser(userId);
        if (collects != null && collects.size() > 0) {
            for (VideoCollect vc : collects) {
                videoRecords.add(vc.getVideoRecord());
            }
        }
        return videoRecords;
    }
}
