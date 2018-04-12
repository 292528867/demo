package com.yk.example.service;

import com.yk.example.dao.UserDao;
import com.yk.example.dao.UserInfoDao;
import com.yk.example.entity.User;
import com.yk.example.entity.UserInfo;
import com.yk.example.utils.Md5Utlls;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/8/8.
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private RedisTemplate redisTemplate;

    public User insertUser(User user) {
        if (StringUtils.isNotBlank(user.getUserId())) {
            // 修改
            user = userDao.save(user);
        } else {
            // 生成邀请码 后期可以考虑通过加密用户id生成
            Set<String> codes = redisTemplate.opsForSet().members("invite_code");
            String inviteCode = Md5Utlls.generateInviteCode();
            if(codes.contains(inviteCode)){
                inviteCode = Md5Utlls.generateInviteCode();
                redisTemplate.opsForSet().add("invite_code", inviteCode);
            }
            // 新增
            user.setInviteCode(inviteCode);
            user = userDao.save(user);
            UserInfo userInfo = new UserInfo();
            userInfo.setUser(user);
            userInfoDao.save(userInfo);
        }
        return user;
    }


    public Page<User> findAll(User user, Pageable pageable) {
        Specification<User> specification = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //所有的断言
                List<Predicate> predicates = new ArrayList<>();
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

    @Transactional(rollbackFor = Exception.class)
    public int updateIsValidByUserId(String userId, String isValid) {
        return userDao.updateIsValidByUserId(userId, isValid);
    }

    public User findOne(String userId) {
        return userDao.findOne(userId);
    }

    public User findByDirectRecommendUser(String directRecommendUserPhone) {
        return userDao.findByDirectRecommendUser(directRecommendUserPhone);
    }

    public User findByPhone(String phone) {
        return userDao.findByPhone(phone);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updatePassword(String password, String phone) {
        return userDao.updatePassword(password, phone);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDeviceToken(User user) {
        userDao.updateDeviceToken(user.getUserId(), user.getDeviceToken(), user.getPlatform());
    }

    public User findByInviteCode(String inviteCode) {
        User user = userDao.findByInviteCode(inviteCode);
        return user;
    }
}
