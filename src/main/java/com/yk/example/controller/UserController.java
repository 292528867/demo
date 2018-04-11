package com.yk.example.controller;

import com.yk.example.dao.UserDao;
import com.yk.example.dto.ControllerResult;
import com.yk.example.dto.UserInfoDto;
import com.yk.example.dto.UserLocation;
import com.yk.example.entity.User;
import com.yk.example.entity.UserFollow;
import com.yk.example.entity.UserInfo;
import com.yk.example.enums.UserType;
import com.yk.example.rongCloud.models.response.TokenResult;
import com.yk.example.service.UserFollowService;
import com.yk.example.service.UserInfoService;
import com.yk.example.service.UserService;
import com.yk.example.utils.Md5Utlls;
import com.yk.example.utils.RongCloudUtils;
import com.yk.example.utils.SmsUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserFollowService userFollowService;

    /**
     * app 用户注册
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "用户注册", notes = "返回的用户实体")
    @RequestMapping(value = "/register/{version}", method = RequestMethod.POST)
    public ControllerResult registerUser(@RequestBody User user, @PathVariable String version) {
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
        String redisCode = redisTemplate.opsForValue().get(user.getPhone() + "_reg");
        String code = user.getCode();
        if (StringUtils.isNoneBlank(redisCode)) {
            if (redisCode.equals(code)) {

                // 密码进行md5加密
                String password = user.getPassword();
                if (StringUtils.isBlank(password)) {
                    return new ControllerResult().setRet_code(1).setRet_values("")
                            .setMessage("密码不能为空");
                }
                String newPassword = Md5Utlls.getMD5String(password);
                user.setPassword(newPassword);

                user.setUserType(UserType.app);

                if (StringUtils.isBlank(user.getNickName())) {
                    user.setNickName(RandomStringUtils.randomNumeric(8));
                }
                if (StringUtils.isBlank(user.getHeadImgUrl())) {
                    user.setHeadImgUrl("https://pic.qqtn.com/up/2018-3/15223765997238966.jpg");
                }

                User newUser = userService.insertUser(user);


                // 生成融云token
                TokenResult tokenResult = RongCloudUtils.registerRongCloudUser(newUser.getUserId(), user.getNickName(), user.getHeadImgUrl());
                user.setRongCloudToken(tokenResult.getToken());

                return new ControllerResult().setRet_code(0)
                        .setRet_values(userService.insertUser(newUser));
            }
            return new ControllerResult().setRet_code(1).setRet_values("")
                    .setMessage("验证码错误");
        } else {
            return new ControllerResult().setRet_code(1).setRet_values("")
                    .setMessage("非法的验证码");
        }
    }

    /**
     * 注册发送短信验证码
     *
     * @param phone
     * @return
     */
    @ApiOperation(value = "注册发送短信验证码")
    @RequestMapping(value = "regSendSms/{phone}/{version}", method = RequestMethod.GET)
    public ControllerResult regSendSms(@PathVariable String phone, @PathVariable String version) {
        if (StringUtils.isBlank(phone)) {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("手机号不能为空");
        }
        User user = userService.findByPhone(phone);
        if (user != null) {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("用户已注册");
        }
        String code = RandomStringUtils.randomNumeric(4);
        boolean result = SmsUtils.sendSms(phone, code, "241779", 5);
        logger.info(new Date() + "发送短信验证码为：" + code);
        if (result) {
            // 保存到redis 5分钟后过期
            redisTemplate.opsForValue().set(phone + "_reg", code, 5, TimeUnit.MINUTES);
            return new ControllerResult().setRet_code(0).setRet_values("").setMessage("发送成功，耐心等待");
        } else {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("发送失败");
        }
    }

    /**
     * 登陆验证码
     *
     * @param phone
     * @return
     */
    @ApiOperation(value = "登陆验证码")
    @RequestMapping(value = "loginSendSms/{phone}/{version}", method = RequestMethod.GET)
    public ControllerResult loginSendSms(@PathVariable String phone, @PathVariable String version) {
        if (StringUtils.isBlank(phone)) {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("手机号不能为空");
        }
        String code = RandomStringUtils.randomNumeric(4);
        boolean result = SmsUtils.sendSms(phone, code, "241783", 15);
        logger.info(new Date() + "发送短信验证码为：" + code);
        if (result) {
            // 保存到redis 5分钟后过期
            redisTemplate.opsForValue().set(phone + "_login", code, 15, TimeUnit.MINUTES);
            return new ControllerResult().setRet_code(0).setRet_values("").setMessage("发送成功，耐心等待");
        } else {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("发送失败");
        }
    }

    /**
     * 修改密码验证码
     *
     * @param phone
     * @return
     */
    @ApiOperation(value = "修改密码验证码")
    @RequestMapping(value = "updateSendSms/{phone}/{version}", method = RequestMethod.GET)
    public ControllerResult updateSendSms(@PathVariable String phone, @PathVariable String version) {
        if (StringUtils.isBlank(phone)) {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("手机号不能为空");
        }
        User user = userService.findByPhone(phone);
        if (user == null) {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("该手机号未注册");
        }
        String code = RandomStringUtils.randomNumeric(4);
        boolean result = SmsUtils.sendSms(phone, code, "241786", 15);
        logger.info(new Date() + "发送短信验证码为：" + code);
        if (result) {
            // 保存到redis 5分钟后过期
            redisTemplate.opsForValue().set(phone + "_update", code, 15, TimeUnit.MINUTES);
            return new ControllerResult().setRet_code(0).setRet_values("").setMessage("发送成功，耐心等待");
        } else {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("发送失败");
        }
    }

    /**
     * 登陆
     *
     * @param login
     * @return
     */
    @ApiOperation(value = "登录")
    @RequestMapping(value = "login/{version}", method = RequestMethod.POST)
    public ControllerResult login(@RequestBody User login, @PathVariable String version) {
        String code = login.getCode();
        String phone = login.getPhone();
        String password = login.getPassword();
        User user = null;
        // 验证码登陆
        if (StringUtils.isNoneBlank(code)) {
            String redisCode = redisTemplate.opsForValue().get(login.getPhone() + "_login");
            if (!redisCode.equals(code)) {
                return new ControllerResult().setRet_code(1).setRet_values("").setMessage("验证码错误");
            }
            // 判断是否注册
            user = userService.findByPhone(phone);
            if (user == null) {
                // 注册
                if (StringUtils.isBlank(login.getNickName())) {
                    user.setNickName(RandomStringUtils.randomNumeric(8));
                }
                if (StringUtils.isBlank(login.getHeadImgUrl())) {
                    user.setHeadImgUrl("https://pic.qqtn.com/up/2018-3/15223765997238966.jpg");
                }
                login.setUserType(UserType.app);
                User newUser = userService.insertUser(login);
                // 生成融云token
                TokenResult tokenResult = RongCloudUtils.registerRongCloudUser(newUser.getUserId(), user.getNickName(), user.getHeadImgUrl());
                newUser.setRongCloudToken(tokenResult.getToken());
                return new ControllerResult().setRet_code(0).setRet_values(userService.insertUser(newUser)).setMessage("登陆成功");
            }
        } else {
            // 密码登陆
            if (StringUtils.isBlank(password)) {
                return new ControllerResult().setRet_code(1).setRet_values("").setMessage("密码不能为空");
            }
            if (StringUtils.isBlank(phone)) {
                return new ControllerResult().setRet_code(1).setRet_values("").setMessage("手机号不能空");
            }
            user = userService.findByPhone(phone);
            if (user == null) {
                return new ControllerResult().setRet_code(1).setRet_values("").setMessage("用户不存在");
            }
            if (!user.getPassword().equals(Md5Utlls.getMD5String(password))) {
                return new ControllerResult().setRet_code(1).setRet_values("").setMessage("密码错误");
            }
        }
        return new ControllerResult().setRet_code(0).setRet_values(user).setMessage("登陆成功");
    }

    /**
     * 第三方登陆
     *
     * @param thirdLogin
     * @return
     */
    @ApiOperation(value = "第三方登录")
    @RequestMapping(value = "thirdLogin/{version}", method = RequestMethod.POST)
    public ControllerResult thirdLogin(@RequestBody User thirdLogin, @PathVariable String version) {
        if (StringUtils.isBlank(thirdLogin.getNickName())) {
            thirdLogin.setNickName(RandomStringUtils.randomNumeric(8));
        }
        if (StringUtils.isBlank(thirdLogin.getHeadImgUrl())) {
            thirdLogin.setHeadImgUrl("https://pic.qqtn.com/up/2018-3/15223765997238966.jpg");
        }
        User user = userService.insertUser(thirdLogin);
        // 生成融云token
        TokenResult tokenResult = RongCloudUtils.registerRongCloudUser(user.getUserId(), user.getNickName(), user.getHeadImgUrl());
        user.setRongCloudToken(tokenResult.getToken());
        user = userService.insertUser(user);
        return new ControllerResult().setRet_code(0).setRet_values(user).setMessage("登陆成功");
    }

    /**
     * 修改密码
     *
     * @param login
     * @return
     */
    @ApiOperation(value = "修改密码")
    @RequestMapping(value = "updatePassword/{version}", method = RequestMethod.POST)
    public ControllerResult updatePassword(@RequestBody User login, @PathVariable String version) {
        String code = login.getCode();
        String phone = login.getPhone();
        String password = login.getPassword();
        String redisCode = redisTemplate.opsForValue().get(login.getPhone() + "_update");
        if (!code.equals(redisCode)) {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("验证码错误");
        }
        User user = userService.findByPhone(phone);
        if (user == null) {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("该手机号未注册");
        }
        int i = userService.updatePassword(Md5Utlls.getMD5String(password), phone);
        if (i == 0) {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("修改失败");
        }
        return new ControllerResult().setRet_code(0).setRet_values("").setMessage("修改成功");
    }

    /**
     * 用户定位
     *
     * @param userLocation
     * @param version
     * @return
     */
    @ApiOperation(value = "用户定位")
    @RequestMapping(value = "saveUserLocation/{version}", method = RequestMethod.POST)
    public ControllerResult saveUserLocation(@RequestBody UserLocation userLocation, @PathVariable String version) {
        boolean result = userInfoService.updateUserLocation(userLocation);
        if (result) {
            return new ControllerResult().setRet_code(0).setRet_values("").setMessage("定位成功");
        }
        return new ControllerResult().setRet_code(1).setRet_values("").setMessage("定位失败");
    }

    /**
     * 用户资料编辑
     *
     * @param userInfoDto
     * @param version
     * @return
     */
    @ApiOperation(value = "用户资料编辑")
    @RequestMapping(value = "editUserInfo/{version}", method = RequestMethod.POST)
    public ControllerResult editUserInfo(@RequestBody UserInfoDto userInfoDto, @PathVariable String version) {
        UserInfoDto UserInfoDto = userInfoService.editUserInfo(userInfoDto);
        return new ControllerResult().setRet_code(0).setRet_values(UserInfoDto).setMessage("修改成功");
    }


    /**
     * 用户资料查询
     * @param userId
     * @param version
     * @return
     */
    @ApiOperation(value = "用户资料查询")
    @RequestMapping(value = "personInfo/{userId}/{version}", method = RequestMethod.GET)
    public ControllerResult personInfo(@PathVariable String userId,@PathVariable String version){
        UserInfoDto UserInfoDto = userInfoService.personInfo(userId);
        return new ControllerResult().setRet_code(0).setRet_values(UserInfoDto).setMessage("");
    }

    /**
     * 关注用户
     *
     * @param userFollow
     * @param version
     * @return
     */
    @ApiOperation(value = "关注用户")
    @RequestMapping(value = "followUser/{version}", method = RequestMethod.POST)
    public ControllerResult followUser(@RequestBody UserFollow userFollow, @PathVariable String version) {
        UserFollow userFollow1 = userFollowService.save(userFollow);
        return new ControllerResult().setRet_code(0).setRet_values(userFollow1).setMessage("");
    }

    /**
     * 修改用户的设备token和手机平台
     *
     * @param user
     * @param version
     * @return
     */
    @ApiOperation(value = "修改用户的设备token和手机平台")
    @RequestMapping(value = "updateDeviceToken/{version}", method = RequestMethod.POST)
    public ControllerResult updateDeviceToken(@RequestBody User user, @PathVariable String version) {
        userService.updateDeviceToken(user);
        return new ControllerResult().setRet_code(0).setRet_values("").setMessage("");
    }

    /**
     * @param login
     * @return
     * @throws ServletException
     */
    @ApiIgnore
    @RequestMapping(value = "/jwtLogin/{version}", method = RequestMethod.POST)
    public ControllerResult jwtLogin(@RequestBody User login, @PathVariable String version) throws ServletException {

        String jwtToken = "";

        String password = login.getPassword();

        User user = userService.findByPhone(login.getPhone());

        if (user == null) {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("手机号未被注册");
        }
        if (!user.getPassword().equals(Md5Utlls.getMD5String(password))) {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("密码错误");
        }
        Date date = new Date();
        DateTime dateTime = new DateTime(date);
        Date exprireDate = dateTime.plusMinutes(1).toDate();
        jwtToken = Jwts.builder().setSubject(login.getPhone()).setIssuedAt(date).setExpiration(exprireDate)
                .signWith(SignatureAlgorithm.HS256, "secretkey").compact();
        return new ControllerResult().setRet_code(0).setRet_values(jwtToken).setMessage("");

    }


}
