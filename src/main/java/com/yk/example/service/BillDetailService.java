package com.yk.example.service;

import com.yk.example.dao.BillDetailDao;
import com.yk.example.dao.UserDao;
import com.yk.example.dto.InviteRecordDto;
import com.yk.example.entity.BillDetail;
import com.yk.example.entity.User;
import com.yk.example.enums.BillType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yk on 2018/4/12.
 */
@Service
public class BillDetailService {


    @Autowired
    private BillDetailDao billDetailDao;

    @Autowired
    private UserDao userDao;


    public Page<BillDetail> findByUserId(String userId, Pageable pageable) {
        return billDetailDao.findByUser(userDao.findOne(userId), pageable);
    }

    public Map<String, Object> inviteRecord(String userId) {
        Map<String, Object> result = new HashMap<>();
        List<InviteRecordDto> inviteRecordDtos = new ArrayList<>();
        User user = new User();
        user.setUserId(userId);
        List<BillDetail> billDetails = billDetailDao.findByUserAndBillType(user, BillType.newIncome);
        // 总奖励金额
        double totalMoney = 0;
        if (billDetails != null && billDetails.size() > 0) {
            for (BillDetail detail : billDetails) {
                totalMoney += detail.getMoney();
                InviteRecordDto dto = new InviteRecordDto();
                dto.setMoney(detail.getMoney());
                dto.setPhone(detail.getUser().getPhone());
                dto.setTime(detail.getCreateTime());
                dto.setProgress("完成");
                inviteRecordDtos.add(dto);
            }
        }
        int totalUser =  userDao.countInviteUser(userId);
        result.put("totalMoney", totalMoney);
        result.put("totalUser", 0);
        result.put("list", inviteRecordDtos);
        return result;
    }
}
