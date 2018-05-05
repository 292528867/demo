package com.yk.example.service;

import com.yk.example.dao.ThirduserDao;
import com.yk.example.dao.UserDao;
import com.yk.example.dto.BindDto;
import com.yk.example.entity.ThirdUser;
import com.yk.example.entity.User;
import com.yk.example.enums.UserType;
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
        BindDto dto = new BindDto();
        dto.setPhone(user.getPhone());
        dto.setUserId(user.getUserId());
        dto.setQqStatus(false);
        dto.setWxStatus(false);
        dto.setWbStatus(false);
        if(thirdUsers != null && thirdUsers.size() > 0){
             for(ThirdUser thirdUser : thirdUsers){
                 if(thirdUser.getUserType().equals(UserType.qq)){
                       dto.setQqStatus(true);
                 }
                 if(thirdUser.getUserType().equals(UserType.wx)){
                     dto.setWxStatus(true);
                 }
                 if(thirdUser.getUserType().equals(UserType.wb)){
                     dto.setWbStatus(true);
                 }
             }
        }
        return dto;
    }

    public ThirdUser findThirdUserByUserType(User user, UserType userType) {
        return thirduserDao.findByUserAndUserType(user, userType);
    }
}
