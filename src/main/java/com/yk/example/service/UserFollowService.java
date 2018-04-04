package com.yk.example.service;

import com.yk.example.dao.UserFollowDao;
import com.yk.example.entity.UserFollow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yk on 2018/4/4.
 */
@Service
public class UserFollowService {

    @Autowired
    private UserFollowDao userFollowDao;

    public UserFollow save(UserFollow userFollow) {
        return userFollowDao.save(userFollow);
    }


}
