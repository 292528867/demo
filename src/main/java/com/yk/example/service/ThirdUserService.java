package com.yk.example.service;

import com.yk.example.dao.ThirduserDao;
import com.yk.example.dao.UserDao;
import com.yk.example.dto.BindDto;
import com.yk.example.entity.ThirdUser;
import com.yk.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yk on 2018/5/2.
 */
@Service
public class ThirdUserService {


    @Autowired
    private ThirduserDao thirduserDao;

    @Autowired
    private UserDao userDao;

    public ThirdUser findThirdUser(String thirdUserId) {
        // 1 为绑定状态
        return thirduserDao.findByThirdUserIdAndStatus(thirdUserId,"1");
    }

    public ThirdUser save(ThirdUser thirdUser) {
        return thirduserDao.save(thirdUser);
    }

    public BindDto queryBindStatus(String userId) {
        User user = userDao.findOne(userId);
        List<ThirdUser> thirdUsers = thirduserDao.findByUser(user);
        return null;
    }
}
