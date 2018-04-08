package com.yk.example.service;

import com.yk.example.dao.UserDao;
import com.yk.example.dao.UserFollowDao;
import com.yk.example.dto.FansDto;
import com.yk.example.entity.User;
import com.yk.example.entity.UserFollow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yk on 2018/4/4.
 */
@Service
public class UserFollowService {

    @Autowired
    private UserFollowDao userFollowDao;

    @Autowired
    private UserDao userDao;

    public UserFollow save(UserFollow userFollow) {
        User user = userDao.findOne(userFollow.getFollowId());
        userFollow.setHeadImgUrl(user.getHeadImgUrl());
        return userFollowDao.save(userFollow);
    }


    public List<FansDto> fanList(String userId) {
        List<FansDto> fansDtos = new ArrayList<>();
        //  粉丝用户列表
        List<UserFollow> fans = userFollowDao.findByFollowIdAndStatus(userId, true);
        // 你关注的用户
        List<String> userFollows = userFollowDao.findByUserId(userId,true);
        if (fans != null && fans.size() > 0) {
            for (UserFollow u : fans) {
                FansDto dto = new FansDto();
                dto.setCreateTime(u.getCreateTime());
                dto.setHeadImgUrl(u.getHeadImgUrl());
                dto.setFollowId(u.getUserId());
                //  判断你是否是该用户的粉丝
                if (userFollows.contains(u.getFollowId())) {
                    dto.setFollow(true);
                } else {
                    dto.setFollow(false);
                }
                fansDtos.add(dto);
            }
        }
        return fansDtos;
    }
}
