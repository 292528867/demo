package com.yk.example.service;

import com.yk.example.dao.UserZfbDao;
import com.yk.example.entity.User;
import com.yk.example.entity.UserZfb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yk on 2018/4/16.
 */
@Service
public class UserZfbService {

    @Autowired
    private UserZfbDao userZfbDao;

    public UserZfb addZfb(UserZfb userZfb) {
        return userZfbDao.save(userZfb);
    }

    public List<UserZfb> findByUser(String userId) {
        User user = new User();
        user.setUserId(userId);
        return userZfbDao.findByUser(user);
    }
}
