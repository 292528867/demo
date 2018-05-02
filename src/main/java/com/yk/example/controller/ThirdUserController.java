package com.yk.example.controller;

import cn.jpush.api.report.UsersResult;
import com.yk.example.dto.BindDto;
import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.ThirdUser;
import com.yk.example.entity.User;
import com.yk.example.service.ThirdUserService;
import com.yk.example.service.UserService;
import com.yk.example.utils.SmsUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by yk on 2018/5/2.
 */
@RestController
@RequestMapping(value = "thirdUser")
public class ThirdUserController {

    private static final Logger logger = LoggerFactory.getLogger(ThirdUserController.class);


    @Autowired
    private ThirdUserService thirdUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;



}
