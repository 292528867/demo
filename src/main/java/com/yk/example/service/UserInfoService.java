package com.yk.example.service;

import com.yk.example.dao.UserDao;
import com.yk.example.dao.UserInfoDao;
import com.yk.example.dao.UserLocationHistoryDao;
import com.yk.example.dto.UserLocation;
import com.yk.example.entity.UserInfo;
import com.yk.example.entity.UserLocationHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by yk on 2018/4/4.
 */
@Service
public class UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserLocationHistoryDao userLocationHistoryDao;

    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserLocation(UserLocation userLocation) {
        String longitude = userLocation.getLongitude();
        String latitude = userLocation.getLatitude();
        String userId = userLocation.getUserId();
        UserLocationHistory history = new UserLocationHistory();
        history.setLatitude(latitude);
        history.setLongitude(longitude);
        history.setUser(userDao.findOne(userId));
        userLocationHistoryDao.save(history);
        userInfoDao.updateUserLocation(longitude, latitude, userId);
        return true;
    }

    public UserInfo editUserInfo(UserInfo userInfo) {
        return userInfoDao.save(userInfo);
    }
}
