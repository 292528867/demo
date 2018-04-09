package com.yk.example.service;

import com.yk.example.dao.UserDao;
import com.yk.example.dao.UserfriendDao;
import com.yk.example.entity.User;
import com.yk.example.entity.UserFriend;
import com.yk.example.enums.FriendStatus;
import com.yk.example.utils.JPushUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yk on 2018/4/8.
 */
@Service
public class UserFriendService {

    @Autowired
    private UserfriendDao userfriendDao;

    @Autowired
    private UserDao userDao;

    public void friendApply(UserFriend userFriend) {
        userFriend.setFriendStatus(FriendStatus.waitAgree);
         userFriend = userfriendDao.save(userFriend);
        // 查询被添加好友的用户信息
        User user = userDao.findOne(userFriend.getToUser());
        // 极光推送好友申请通知
        Map<String, String> extras = new HashMap<String, String>();
        extras.put("userFriendId", userFriend.getId());
        JPushUtils.sendAlias("好友申请", Collections.singletonList(user.getDeviceToken()),extras );
    }

    @Transactional(rollbackFor = Exception.class)
    public void agreeApply(UserFriend userFriend) {
        userfriendDao.agreeApply(userFriend.getId(), FriendStatus.agree);
    }

    public List<UserFriend> findByToUser(String userId) {
        return userfriendDao.findByToUser(userId);
    }
}
