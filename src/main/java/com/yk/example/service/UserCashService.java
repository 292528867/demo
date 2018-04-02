package com.yk.example.service;

import com.yk.example.dao.UserCashDao;
import com.yk.example.entity.User;
import com.yk.example.entity.UserCash;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
 * Created by yk on 2018/4/2.
 */
@Service
public class UserCashService {

    @Autowired
    private UserCashDao userCashDao;


    public Page<UserCash> findAll(UserCash userCash, Pageable pageable) {
        Specification<UserCash> specification = new Specification<UserCash>() {
            @Override
            public Predicate toPredicate(Root<UserCash> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>(); //所有的断言
             /*   if (StringUtils.isNoneBlank(userCash.getAuditStatus().toString())) {
                    predicates.add(criteriaBuilder.equal(root.get("auditStatus").as(String.class), userCash.getAuditStatus()));
                }*/
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return userCashDao.findAll(specification, pageable);
    }
}
