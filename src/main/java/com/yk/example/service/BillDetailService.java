package com.yk.example.service;

import com.yk.example.dao.BillDetailDao;
import com.yk.example.dao.UserDao;
import com.yk.example.entity.BillDetail;
import com.yk.example.entity.User;
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
import java.util.List;

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
        return billDetailDao.findByUser(userDao.findOne(userId),pageable);
    }
}
