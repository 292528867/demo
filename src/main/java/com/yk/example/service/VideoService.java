package com.yk.example.service;

import com.yk.example.dao.UserInfoDao;
import com.yk.example.dao.VideoDao;
import com.yk.example.entity.UserInfo;
import com.yk.example.entity.VideoRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yk on 2018/4/2.
 */
@Service
public class VideoService {

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private UserInfoDao userInfoDao;

    public List<VideoRecord> nearby(double longitude, double latitude, int page, int size) {
        //先计算查询点的经纬度范围
        //地球半径千米
        double r = 6371;
        // 30 千米距离
        double dis = 30;
        double dlng = 2 * Math.asin(Math.sin(dis / (2 * r)) / Math.cos(latitude * Math.PI / 180));
        //角度转为弧度
        dlng = dlng * 180 / Math.PI;
        double dlat = dis / r;
        dlat = dlat * 180 / Math.PI;
        double minlat = latitude - dlat;
        double maxlat = latitude + dlat;
        double minlng = longitude - dlng;
        double maxlng = longitude + dlng;
        // 查询附近 30 km的用户
        List<UserInfo> userInfos = userInfoDao.findNearbyUser(minlng, maxlng, minlat, maxlat);
        List<VideoRecord> videoRecords = null;
        if (userInfos != null && userInfos.size() > 0) {
            for (UserInfo userInfo : userInfos) {
                // 查询用户的最后一个视频
              VideoRecord videoRecord =   videoDao.findLastVideoByUser(userInfo.getUser().getUserId());
                videoRecords.add(videoRecord);
            }
        }
        return videoRecords;
    }
}
