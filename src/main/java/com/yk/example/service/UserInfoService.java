package com.yk.example.service;

import com.yk.example.dao.UserDao;
import com.yk.example.dao.UserInfoDao;
import com.yk.example.dao.UserLocationHistoryDao;
import com.yk.example.dto.UserInfoDto;
import com.yk.example.dto.UserLocation;
import com.yk.example.entity.User;
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
        double longitude = userLocation.getLongitude();
        double latitude = userLocation.getLatitude();
        String userId = userLocation.getUserId();
        UserLocationHistory history = new UserLocationHistory();
        history.setLatitude(latitude);
        history.setLongitude(longitude);
        history.setUser(userDao.findOne(userId));
        userLocationHistoryDao.save(history);
        userInfoDao.updateUserLocation(longitude, latitude, userId);
        return true;
    }

    public UserInfoDto editUserInfo(UserInfoDto userInfoDto) {
        String userId = userInfoDto.getUserId();
        User user = userDao.findOne(userId);
        UserInfo userInfo = userInfoDao.findByUser(user);

        user.setHeadImgUrl(userInfoDto.getHeadImgUrl());
        user.setNickName(userInfoDto.getNickName());
        user.setSex(userInfoDto.getSex());
        userDao.save(user);

        userInfo.setAddress(userInfoDto.getAddress());
        userInfo.setBirth(userInfoDto.getBirth());
        userInfo.setImpressionLabel(userInfoDto.getImpressionLabel());
        userInfo.setPersonalSign(userInfoDto.getPersonalSign());
        userInfo.setProfession(userInfoDto.getProfession());
        userInfoDao.save(userInfo);
        return userInfoDto;
    }

    public UserInfoDto personInfo(String userId) {
        User user = userDao.findOne(userId);
        UserInfo userInfo = userInfoDao.findByUser(user);
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setUserId(userId);
        userInfoDto.setHeadImgUrl(user.getHeadImgUrl());
        userInfoDto.setNickName(user.getNickName());
        userInfoDto.setSex(user.getSex());
        userInfoDto.setAddress(userInfo.getAddress());
        userInfoDto.setBirth(userInfo.getBirth());
        userInfoDto.setImpressionLabel(userInfo.getImpressionLabel());
        userInfoDto.setPersonalSign(userInfo.getPersonalSign());
        userInfoDto.setProfession(userInfo.getProfession());
        userInfoDto.setAccountIncome(userInfo.getAccountIncome());
        userInfoDto.setMiaoPeas(userInfo.getMiaoPeas());
        return userInfoDto;
    }
}
