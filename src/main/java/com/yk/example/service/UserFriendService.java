package com.yk.example.service;

import com.yk.example.dao.UserDao;
import com.yk.example.dao.UserFollowDao;
import com.yk.example.dao.UserfriendDao;
import com.yk.example.dto.SearchUserDto;
import com.yk.example.entity.User;
import com.yk.example.entity.UserFollow;
import com.yk.example.entity.UserFriend;
import com.yk.example.enums.FriendStatus;
import com.yk.example.utils.JPushUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by yk on 2018/4/8.
 */
@Service
public class UserFriendService {

    @Autowired
    private UserfriendDao userfriendDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserFollowDao userFollowDao;

    public List<SearchUserDto> searchFriend(String userId, String nickName) {
        List<User> users = userDao.findByNickNameLike("%"+nickName+"%");
        final CopyOnWriteArrayList<User> cowList = new CopyOnWriteArrayList<User>(users);
     /*    // 登陆用户关注所有的用户id
        List<String> userFollows = userFollowDao.findByUserId(userId,true);
        // 登陆用户所有的粉丝
        List<UserFollow> fanList = userFollowDao.findByFollowIdAndStatus(userId, true);*/
        List<SearchUserDto> dtos = new ArrayList<>();
        for (User user : cowList){
            if(userId.equals(user.getUserId())){
                cowList.remove(user);
                break;
            }
            UserFollow userFollow = userFollowDao.findByUserIdAndFollowId(userId, user.getUserId());
            UserFollow fan = userFollowDao.findByUserIdAndFollowId(user.getUserId(), userId);
            SearchUserDto dto = new SearchUserDto();
            dto.setUserId(user.getUserId());
            dto.setNickName(user.getNickName());
            dto.setHeadImgUrl(user.getHeadImgUrl());
            dto.setInviteCode(user.getInviteCode());
            dto.setRongCloudToken(user.getRongCloudToken());
            dto.setVip(user.isVip());
            if(userFollow == null && fan == null){
               dto.setFollowStatus("0");
            }else if(userFollow  != null && fan == null){
               dto.setFollowStatus("1");
            }else if (userFollow == null && fan != null){
                dto.setFollowStatus("2");
            }else {
                dto.setFollowStatus("3");
            }
            dtos.add(dto);
        }
        return dtos;
    }
}
