package com.yk.example.service;

import com.yk.example.dao.BillDetailDao;
import com.yk.example.dao.RechargeDao;
import com.yk.example.dao.UserDao;
import com.yk.example.dao.UserInfoDao;
import com.yk.example.entity.BillDetail;
import com.yk.example.entity.RechargeRecord;
import com.yk.example.entity.User;
import com.yk.example.entity.UserInfo;
import com.yk.example.enums.BillType;
import com.yk.example.enums.PayStatus;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private BillDetailDao billDetailDao;

    public RechargeRecord save(RechargeRecord rechargeRecord) {
        rechargeRecord.setPayStatus(PayStatus.wait);
        return rechargeDao.save(rechargeRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updatePayStatus(String rechargeId, PayStatus success) {
        // 修改订单状态
        rechargeDao.updatePayStatus(rechargeId, success);
        // 如果充值金额大于90元 成为会员 并且给推荐人奖励40元
        RechargeRecord rechargeRecord = rechargeDao.findOne(rechargeId);
        User user = rechargeRecord.getUser();
        if (rechargeRecord.getMoney() > 0) {
            user.setVip(true);
            user = userDao.save(user);
            String directRecommendUser = user.getDirectRecommendUser();
            if (StringUtils.isNotBlank(directRecommendUser)) {
                // 推荐人账号加40元的纳新收入
                User recommendUser = userDao.findOne(directRecommendUser);
                UserInfo recommendUserInfo = userInfoDao.findByUser(recommendUser);
                float newAccountIncome = recommendUserInfo.getAccountIncome() + 40;
                recommendUserInfo.setAccountIncome(newAccountIncome);
                userInfoDao.save(recommendUserInfo);
                // 生成账单明细
                BillDetail billDetail = new BillDetail();
                billDetail.setBillType(BillType.newIncome);
                billDetail.setMoney(40);
                billDetail.setUser(recommendUser);
                billDetailDao.save(billDetail);
                // 如果推荐人还有推荐人 则奖励10元
       /*         String spaceRecommendUserId = recommendUser.getDirectRecommendUser();
                if(StringUtils.isNotBlank(spaceRecommendUserId)){
                    User spaceRecommendUser = userDao.findOne(spaceRecommendUserId);
                    UserInfo spaceRecommendUserInfo = userInfoDao.findByUser(spaceRecommendUser);
                    float spaceUserAccountIncome = spaceRecommendUserInfo.getAccountIncome() + 10;
                    spaceRecommendUserInfo.setAccountIncome(spaceUserAccountIncome);
                    userInfoDao.save(spaceRecommendUserInfo);
                    // 生成账单明细
                    BillDetail billDetail1 = new BillDetail();
                    billDetail1.setBillType(BillType.newIncome);
                    billDetail1.setMoney(10);
                    billDetail1.setUser(spaceRecommendUser);
                    billDetailDao.save(billDetail1);
                }*/
            }
        }
        // 修改用户秒豆 并生成账单
        UserInfo userInfo = userInfoDao.findByUser(user);
        float newMiaoPeas = userInfo.getMiaoPeas() + rechargeRecord.getMoney() * 10;
        userInfo.setMiaoPeas(newMiaoPeas);
        userInfoDao.save(userInfo);
        BillDetail billDetail = new BillDetail();
        billDetail.setBillType(BillType.recharge);
        billDetail.setMoney(rechargeRecord.getMoney() * 10);
        billDetail.setUser(user);
        billDetailDao.save(billDetail);

    }

    public RechargeRecord findOne(String rechargeId) {
        return rechargeDao.findOne(rechargeId);
    }

}
