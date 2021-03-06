package com.yk.example.service;

import com.yk.example.dao.*;
import com.yk.example.dto.FansDto;
import com.yk.example.entity.*;
import com.yk.example.utils.BeanSort;
import com.yk.example.utils.JPushUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private VideoZanDao videoZanDao;

    @Autowired
    private PushSetDao pushSetDao;

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
            userFollowDao.deleteByUserIdAndFollowId(userFollow.getUserId(), userFollow.getFollowId());
        }

        PushSet pushSet = pushSetDao.findByUserId(followUser.getUserId());
        if (pushSet.isFollowPush()) {
            // 对follow用户进行推送
            if (userFollow.isStatus()) {
                JPushUtils.sendAlias(user.getNickName() + "关注了您",
                        Collections.singletonList(userFollow.getFollowId()), Collections.emptyMap());
            } else {
                JPushUtils.sendAlias(user.getNickName() + "对您取消关注",
                        Collections.singletonList(userFollow.getFollowId()), Collections.emptyMap());
            }
        }
        return userFollow;
    }


    public List<FansDto> fanList(String userId) {
        List<FansDto> fansDtos = new ArrayList<>();
        //  粉丝用户列表
//        List<UserFollow> fans = userFollowDao.findByFollowIdAndStatus(userId, true);
        List<UserFollow> fans = userFollowDao.findByFollowIdAndStatusOrderByCreateTimeDesc(userId, true);
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
        User user = new User();
        user.setUserId(userId);
        List<VideoZan> videoZanList = videoZanDao.findByUser(user);
        if (byUserId != null && byUserId.size() > 0) {
            for (String id : byUserId) {
                List<VideoRecord> videoRecordList = videoDao.findLastVideoByUser(id);
                if (videoRecordList != null && videoRecordList.size() > 0) {
                    VideoRecord videoRecord = videoRecordList.get(0);
                    videoRecord.setFollow(true);
                    for (VideoZan videoZan : videoZanList) {
                        if (videoRecord.getId().equals(videoZan.getVideoRecord().getId())) {
                            videoRecord.setZan(true);
                        }
                    }
                    videoRecords.add(videoRecord);
                }
            }
        }
        BeanSort.sort(videoRecords, "createTime", false);
        return videoRecords;
    }

    public UserFollow existFollow(UserFollow userFollow) {
        return userFollowDao.findByUserIdAndFollowId(userFollow.getUserId(), userFollow.getFollowId());
    }


    public UserFollow existFollow(String userId, String otherUserId) {
        return userFollowDao.findByUserIdAndFollowId(userId, otherUserId);
    }

    public long countFanNum(String userId) {
        return userFollowDao.countByFollowId(userId);
    }

    public long countFollowNum(String userId) {
        return userFollowDao.countByUserId(userId);
    }

    public List<FansDto> followList(String userId) {
        List<FansDto> followDtos = new ArrayList<>();
        List<UserFollow> follows = userFollowDao.findByUserIdAndStatusOrderByCreateTimeDesc(userId, true);
        // 你的粉丝
        List<String> fans = userFollowDao.findByFollowId(userId, true);
        if (follows != null && follows.size() > 0) {
            for (UserFollow u : follows) {
                FansDto dto = new FansDto();
                User user = userDao.findOne(u.getFollowId());
                dto.setCreateTime(u.getCreateTime());
                dto.setHeadImgUrl(user.getHeadImgUrl());
                dto.setNickName(user.getNickName());
                dto.setFollowId(user.getUserId());
                //  判断是否是你的粉丝
                if (fans.contains(user.getUserId())) {
                    dto.setFollow(true);
                } else {
                    dto.setFollow(false);
                }
                followDtos.add(dto);
            }
        }

        return followDtos;
    }
}
