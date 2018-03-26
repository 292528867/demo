package com.yk.example.controller;

import com.yk.example.dao.UserDao;
import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */
@ApiIgnore
@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * app 用户注册
     *
     * @param user
     * @param code
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ControllerResult registerUser(User user, String code) {
        if (StringUtils.isNoneBlank(code)) {
            if (redisTemplate.opsForValue().get(user.getPhone()).toString().equals(code)) { // 验证码校验
                // 密码进行md5加密
                String password = user.getPassword();

                return new ControllerResult().setRet_code(0)
                        .setRet_values(userDao.save(user));
            }
            return new ControllerResult().setRet_code(1)
                    .setMessage("验证码错误");
        } else {
            return new ControllerResult().setRet_code(1)
                    .setMessage("非法的验证码");
        }
    }

    /**
     * 动态查询所有用户信息
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "findAllUser", method = RequestMethod.GET)
    public ControllerResult findAllUser(User user, Pageable pageable) {
        Specification<User> specification = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>(); //所有的断言
                if (StringUtils.isNoneBlank(user.getPhone())) {
                    predicates.add(criteriaBuilder.like(root.get("phone").as(String.class), user.getPhone() + "%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return new ControllerResult().setRet_code(0).setRet_values(userDao.findAll(specification, pageable));
    }



    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody User login) throws ServletException {

        String jwtToken = "";

     /*   if (login.getEmail() == null || login.getPassword() == null) {
            throw new ServletException("Please fill in username and password");
        }

        String email = login.getEmail();*/
        String email = "";
        String password = login.getPassword();

        User user = null;

        if (user == null) {
            throw new ServletException("User email not found.");
        }

        String pwd = user.getPassword();

        if (!password.equals(pwd)) {
            throw new ServletException("Invalid login. Please check your name and password.");
        }

        Date date = new Date();
        DateTime dateTime = new DateTime(date);
        Date exprireDate = dateTime.plusMinutes(1).toDate();
        jwtToken = Jwts.builder().setSubject(email).claim("roles", "user").setIssuedAt(date).setExpiration(exprireDate)
                .signWith(SignatureAlgorithm.HS256, "secretkey").compact();

        return jwtToken;
    }


}
