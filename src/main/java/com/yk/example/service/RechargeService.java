package com.yk.example.service;

import com.yk.example.dao.RechargeDao;
import com.yk.example.dao.UserDao;
import com.yk.example.entity.RechargeRecord;
import com.yk.example.entity.User;
import com.yk.example.enums.PayStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yk on 2018/3/28.
 */
@Service
public class RechargeService {

    @Autowired
    private RechargeDao rechargeDao;

    @Autowired
    private UserDao userDao;

    public RechargeRecord save(RechargeRecord rechargeRecord) {
        rechargeRecord.setPayStatus(PayStatus.wait);
        return rechargeDao.save(rechargeRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updatePayStatus(String rechargeId, PayStatus success) {
        rechargeDao.updatePayStatus(rechargeId, success);
        // 修改用户订单的余额
        RechargeRecord rechargeRecord = rechargeDao.findOne(rechargeId);
        User user = userDao.findOne(rechargeRecord.getUser().getUserId());
        userDao.save(user);
    }

    public RechargeRecord findOne(String rechargeId) {
        return rechargeDao.findOne(rechargeId);
    }

}
