package com.yk.example.service;

import com.yk.example.dao.UserDao;
import com.yk.example.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User save(User user) {
        return userDao.save(user);
    }


    public Page<User> findAll(User user, Pageable pageable) {
        Specification<User> specification = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>(); //所有的断言
                if (StringUtils.isNoneBlank(user.getPhone())) {
                    predicates.add(criteriaBuilder.like(root.get("phone").as(String.class), user.getPhone() + "%"));
                }
                if (StringUtils.isNotBlank(user.getNickName())) {
                    predicates.add(criteriaBuilder.like(root.get("nickName").as(String.class), user.getNickName() + "%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return userDao.findAll(specification, pageable);
    }

    @Transactional
    public int updateIsValidByUserId(String userId, String isValid) {
        return userDao.updateIsValidByUserId(userId, isValid);
    }

    public User findOne(String userId) {
        return userDao.findOne(userId);
    }

}
