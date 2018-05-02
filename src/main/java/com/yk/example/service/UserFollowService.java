package com.yk.example.service;

import com.yk.example.dao.UserDao;
import com.yk.example.dao.UserFollowDao;
import com.yk.example.dao.UserInfoDao;
import com.yk.example.dao.VideoDao;
import com.yk.example.dto.FansDto;
import com.yk.example.entity.User;
import com.yk.example.entity.UserFollow;
import com.yk.example.entity.UserInfo;
import com.yk.example.entity.VideoRecord;
import com.yk.example.utils.Distance;
import com.yk.example.utils.JPushUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.*;

/**
 * Created by yk on 2018/4/4.
 */
@Service
public class UserFollowService {

    @Autowired
    private UserFollowDao userFollowDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Transactional(rollbackFor = Exception.class)
    public UserFollow save(UserFollow userFollow) {
        User followUser = userDao.findOne(userFollow.getFollowId());
        userFollow.setHeadImgUrl(followUser.getHeadImgUrl());
        userFollow.setNickName(followUser.getNickName());

        // 关注用户的关注数目改变
        User user = userDao.findOne(userFollow.getUserId());
        UserInfo userInfo = userInfoDao.findByUser(user);
        long followNum = userInfo.getFollowNum();
        if (userFollow.isStatus()) {
            followNum = followNum + 1;
        } else {
            followNum = followNum - 1;
        }
        userInfo.setFollowNum(followNum);
        userInfoDao.save(userInfo);
        // 被关注用户的粉丝数量改变
        UserInfo followUserInfo = userInfoDao.findByUser(followUser);
        long fanNum = followUserInfo.getFanNum();
        if (userFollow.isStatus()) {
            fanNum = fanNum + 1;
        } else {
            fanNum = fanNum - 1;
        }
        followUserInfo.setFanNum(fanNum);
        userInfoDao.save(followUserInfo);
        if (userFollow.isStatus()) {
             userFollowDao.save(userFollow);
        } else {
            userFollowDao.deleteByUserIdAndFollowId(userFollow.getUserId(),userFollow.getFollowId());
        }
        // 对follow用户进行推送
    /*    JPushUtils.sendAlias(user.getNickName() + new DateTime(new Date()).toString("yyyy-MM-dd") + "关注了您",
                Collections.singletonList(userFollow.getFollowId()), Collections.emptyMap());*/
        return userFollow;
    }


    public List<FansDto> fanList(String userId) {
        List<FansDto> fansDtos = new ArrayList<>();
        //  粉丝用户列表
        List<UserFollow> fans = userFollowDao.findByFollowIdAndStatus(userId, true);
        // 你关注的用户
        List<String> userFollows = userFollowDao.findByUserId(userId, true);
        if (fans != null && fans.size() > 0) {
            for (UserFollow u : fans) {
                FansDto dto = new FansDto();
                User user = userDao.findOne(u.getUserId());
                dto.setCreateTime(u.getCreateTime());
                dto.setHeadImgUrl(user.getHeadImgUrl());
                dto.setNickName(user.getNickName());
                dto.setFollowId(user.getUserId());
                //  判断你是否是该用户的粉丝
                if (userFollows.contains(user.getUserId())) {
                    dto.setFollow(true);
                } else {
                    dto.setFollow(false);
                }
                fansDtos.add(dto);
            }
        }
        return fansDtos;
    }

    public List<VideoRecord> findByUserId(String userId) {
        List<VideoRecord> videoRecords = new ArrayList<>();
        List<String> byUserId = userFollowDao.findByUserId(userId, true);
        if (byUserId != null && byUserId.size() > 0) {
            for (String id : byUserId) {
                List<VideoRecord> videoRecordList= videoDao.findLastVideoByUser(id);
                if(videoRecordList != null && videoRecordList.size() >0){
                    VideoRecord videoRecord = videoRecordList.get(0);
                    videoRecords.add(videoRecord);
                }
            }
        }
        return videoRecords;
    }

    public UserFollow existFollow(UserFollow userFollow) {
       return userFollowDao.findByUserIdAndFollowId(userFollow.getUserId(),userFollow.getFollowId());
    }
}
