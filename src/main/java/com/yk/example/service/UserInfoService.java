package com.yk.example.service;

import com.yk.example.dao.UserDao;
import com.yk.example.dao.UserInfoDao;
import com.yk.example.dao.UserLocationHistoryDao;
import com.yk.example.dto.UserInfoDto;
import com.yk.example.dto.UserLocation;
import com.yk.example.entity.User;
import com.yk.example.entity.UserInfo;
import com.yk.example.entity.UserLocationHistory;
import org.apache.commons.lang3.StringUtils;
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

        if(StringUtils.isNotBlank(userInfoDto.getHeadImgUrl())){
            user.setHeadImgUrl(userInfoDto.getHeadImgUrl());
        }
        if(StringUtils.isNotBlank(userInfoDto.getNickName())){
            user.setNickName(userInfoDto.getNickName());
        }
        if(userInfoDto.getSex() != null){
            user.setSex(userInfoDto.getSex());
        }

        if(StringUtils.isNotBlank(userInfoDto.getAddress())){
            userInfo.setAddress(userInfoDto.getAddress());
        }
        if(StringUtils.isNotBlank(userInfoDto.getBirth())){
            userInfo.setBirth(userInfoDto.getBirth());
        }
        if(StringUtils.isNotBlank(userInfoDto.getImpressionLabel())){
            userInfo.setImpressionLabel(userInfoDto.getImpressionLabel());
        }
        if(StringUtils.isNotBlank(userInfoDto.getPersonalSign())){
            userInfo.setPersonalSign(userInfoDto.getPersonalSign());
        }
        if(StringUtils.isNotBlank(userInfoDto.getProfession())){
            userInfo.setProfession(userInfoDto.getProfession());
        }
        User newUser = userDao.save(user);
        UserInfo info = userInfoDao.save(userInfo);
        userInfoDto.setHeadImgUrl(newUser.getHeadImgUrl());
        userInfoDto.setNickName(newUser.getNickName());
        userInfoDto.setSex(newUser.getSex());
        userInfoDto.setAddress(info.getAddress());
        userInfoDto.setBirth(info.getBirth());
        userInfoDto.setImpressionLabel(info.getImpressionLabel());
        userInfoDto.setPersonalSign(info.getPersonalSign());
        userInfoDto.setProfession(info.getProfession());
        userInfoDto.setAccountIncome(info.getAccountIncome());
        userInfoDto.setMiaoPeas(info.getMiaoPeas());
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

    public UserInfo findUserInfo(String userId) {
        User user = userDao.findOne(userId);
        UserInfo userInfo = userInfoDao.findByUser(user);
        return userInfo;
    }
}
