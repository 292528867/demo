package com.yk.example.controller;

import com.yk.example.dao.UserDao;
import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.User;
import com.yk.example.enums.UserType;
import com.yk.example.rongCloud.models.response.TokenResult;
import com.yk.example.service.UserService;
import com.yk.example.utils.Md5Utlls;
import com.yk.example.utils.RongCloudUtils;
import com.yk.example.utils.SmsUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/8/8.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * app 用户注册
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ControllerResult registerUser(@RequestBody User user) {
        //有推荐人手机号
        String directRecommendUserPhone = user.getDirectRecommendUser();
        if (StringUtils.isNotBlank(directRecommendUserPhone)) {
            User directRecommendUser = userService.findByDirectRecommendUser(directRecommendUserPhone);
            if (directRecommendUser == null) {
                return new ControllerResult().setRet_code(1).setRet_values("")
                        .setMessage("推荐人手机号未注册");
            }
            // 设置推荐人的推荐人
            user.setSpaceRecommendUser(directRecommendUser.getDirectRecommendUser());
        }
        // 判断用户是否注册过
        User user1 = userService.findByPhone(user.getPhone());
        if (user1 != null) {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("用户已注册");
        }
        // 验证码校验
        String redisCode = redisTemplate.opsForValue().get(user.getPhone());
        String code = user.getCode();
        if (StringUtils.isNoneBlank(redisCode)) {
            if (redisCode.equals(code)) {
                // 密码进行md5加密
                String password = user.getPassword();
                String newPassword = Md5Utlls.getMD5String(password);
                user.setPassword(newPassword);

                user.setUserType(UserType.app);

                User newUser = userService.save(user);

                // 生成融云token
                TokenResult tokenResult = RongCloudUtils.registerRongCloudUser(newUser.getUserId(), user.getNickName(), user.getHeadImgUrl());
                user.setRongCloudToken(tokenResult.getToken());

                return new ControllerResult().setRet_code(0)
                        .setRet_values(userService.save(newUser));
            }
            return new ControllerResult().setRet_code(1).setRet_values("")
                    .setMessage("验证码错误");
        } else {
            return new ControllerResult().setRet_code(1).setRet_values("")
                    .setMessage("非法的验证码");
        }
    }

    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "sendSms/{phone}")
    public ControllerResult sendSms(@PathVariable String phone) {
        if (StringUtils.isBlank(phone)) {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("手机号不能为空");
        }
        User user = userService.findByPhone(phone);
        if (user != null) {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("用户已注册");
        }
        String code = RandomStringUtils.randomNumeric(6);
        boolean result = SmsUtils.sendSms(phone, code);
        logger.info(new Date() + "发送短信验证码为：" + code);
        if (result) {
            // 保存到redis 5分钟后过期
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return new ControllerResult().setRet_code(0).setRet_values("").setMessage("发送成功，耐心等待");
        } else {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("发送失败");
        }
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
