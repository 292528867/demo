package com.yk.example.controller;

import com.google.gson.JsonObject;
import com.yk.example.dto.BindDto;
import com.yk.example.dto.ControllerResult;
import com.yk.example.dto.UserInfoDto;
import com.yk.example.dto.UserLocation;
import com.yk.example.entity.ThirdUser;
import com.yk.example.entity.User;
import com.yk.example.entity.UserFollow;
import com.yk.example.enums.UserType;
import com.yk.example.rongCloud.RongCloud;
import com.yk.example.rongCloud.models.response.TokenResult;
import com.yk.example.rongCloud.models.user.UserModel;
import com.yk.example.service.ThirdUserService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
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
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserFollowService userFollowService;

    @Value("${avatar.path}")
    private String avatarPath;

    @Value("${avatar.url}")
    private String avatarUrl;

    @Value("${user_default_avatar_url}")
    private String userDefaultAvatarUrl;

    @Value("${rongCloud.app_key}")
    private String rongCloudAppKey;

    @Value("${rongCloud.app_secret}")
    private String rongCloudAppSecret;

    @Autowired
    private ThirdUserService thirdUserService;

    /**
     * app 用户注册
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "用户注册", notes = "返回的用户实体")
    @RequestMapping(value = "/register/{version}", method = RequestMethod.POST)
    public ControllerResult registerUser(@RequestBody User user, @PathVariable String version) throws Exception{
   /*     //有推荐人手机号
        String directRecommendUserPhone = user.getDirectRecommendUser();
        if (StringUtils.isNotBlank(directRecommendUserPhone)) {
            User directRecommendUser = userService.findByDirectRecommendUser(directRecommendUserPhone);
            if (directRecommendUser == null) {
                return new ControllerResult().setRet_code(1).setRet_values("")
                        .setMessage("推荐人手机号未注册");
            }
            // 设置推荐人的推荐人
            user.setSpaceRecommendUser(directRecommendUser.getDirectRecommendUser());
        }*/
        // 推荐码
        String inviteCode = user.getInviteCode();
        if (StringUtils.isNotBlank(inviteCode)) {
            User inviteUser = userService.findByInviteCode(inviteCode);
            if (inviteUser == null) {
                return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap())
                        .setMessage("非法的邀请码");
            }
            // 设置推荐人
            user.setDirectRecommendUser(inviteUser.getUserId());
            // 设置推荐人的推荐人
            user.setSpaceRecommendUser(inviteUser.getDirectRecommendUser());
        }
        // 判断用户是否注册过
        User user1 = userService.findByPhone(user.getPhone());
        if (user1 != null) {
            return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap()).setMessage("用户已注册");
        }
        // 验证码校验
        String redisCode = redisTemplate.opsForValue().get(user.getPhone() + "_reg");
        String code = user.getCode();
        if (StringUtils.isNotBlank(redisCode)) {
            if (redisCode.equals(code)) {

                // 密码进行md5加密
                String password = user.getPassword();
                if (StringUtils.isBlank(password)) {
                    return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap())
                            .setMessage("密码不能为空");
                }
                String newPassword = Md5Utlls.getMD5String(password);
                user.setPassword(newPassword);

                user.setUserType(UserType.app);

                if (StringUtils.isBlank(user.getNickName())) {
                    user.setNickName(RandomStringUtils.randomNumeric(8));
                }
                if (StringUtils.isBlank(user.getHeadImgUrl())) {
                    user.setHeadImgUrl(userDefaultAvatarUrl);
                }

                User newUser = userService.insertUser(user);


                // 生成融云token
               RongCloud  rongCloud = RongCloud.getInstance(rongCloudAppKey, rongCloudAppSecret);
                com.yk.example.rongCloud.methods.user.User rongCloudUser = rongCloud.user;
                UserModel userModel = new UserModel()
                        .setId(newUser.getUserId())
                        .setName(newUser.getNickName())
                        .setPortrait(newUser.getHeadImgUrl());
                TokenResult tokenResult = rongCloudUser.register(userModel);
                newUser.setRongCloudToken(tokenResult.getToken());

                return new ControllerResult().setRet_code(0)
                        .setRet_values(userService.insertUser(newUser));
            }
            return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap())
                    .setMessage("验证码错误");
        } else {
            return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap())
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
     * 绑定手机号验证码
     *
     * @param phone
     * @param version
     * @return
     */
    @ApiOperation(value = "绑定手机号验证码")
    @RequestMapping(value = "bindPhoneSendSms/{phone}/{version}", method = RequestMethod.GET)
    public ControllerResult bindPhoneSendSms(@PathVariable String phone, @PathVariable String version) {
        if (StringUtils.isBlank(phone)) {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("手机号不能为空");
        }
        String code = RandomStringUtils.randomNumeric(4);
        boolean result = SmsUtils.sendSms(phone, code, "246427", 10);
        logger.info(new Date() + "发送短信验证码为：" + code);
        if (result) {
            // 保存到redis 5分钟后过期
            redisTemplate.opsForValue().set(phone + "_bind", code, 5, TimeUnit.MINUTES);
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
    public ControllerResult login(@RequestBody User login, @PathVariable String version)throws Exception {
        String code = login.getCode();
        String phone = login.getPhone();
        String password = login.getPassword();
        User user = null;
        // 验证码登陆
        if (StringUtils.isNotBlank(code)) {
            String redisCode = redisTemplate.opsForValue().get(login.getPhone() + "_login");
            if(StringUtils.isBlank(redisCode)){
                return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap()).setMessage("验证码错误");
            }
            if (!redisCode.equals(code)) {
                return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap()).setMessage("验证码错误");
            }
            // 判断是否注册
            user = userService.findByPhone(phone);
            if (user == null) {
                // 注册
                if (StringUtils.isBlank(login.getNickName())) {
                    login.setNickName(RandomStringUtils.randomNumeric(8));
                }
                if (StringUtils.isBlank(login.getHeadImgUrl())) {
                    login.setHeadImgUrl(userDefaultAvatarUrl);
                }
                login.setUserType(UserType.app);
                User newUser = userService.insertUser(login);
                // 生成融云token
                RongCloud  rongCloud = RongCloud.getInstance(rongCloudAppKey, rongCloudAppSecret);
                com.yk.example.rongCloud.methods.user.User rongCloudUser = rongCloud.user;
                UserModel userModel = new UserModel()
                        .setId(newUser.getUserId())
                        .setName(newUser.getNickName())
                        .setPortrait(newUser.getHeadImgUrl());
                TokenResult tokenResult = rongCloudUser.register(userModel);
                newUser.setRongCloudToken(tokenResult.getToken());
                return new ControllerResult().setRet_code(0).setRet_values(userService.insertUser(newUser)).setMessage("登陆成功");
            }
        } else {
            // 密码登陆
            if (StringUtils.isBlank(password)) {
                return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap()).setMessage("密码不能为空");
            }
            if (StringUtils.isBlank(phone)) {
                return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap()).setMessage("手机号不能空");
            }
            user = userService.findByPhone(phone);
            if (user == null) {
                return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap()).setMessage("用户不存在");
            }
            if(StringUtils.isBlank(user.getPassword())){
                return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap()).setMessage("该账号还没有设置密码，请用其他登陆方式");
            }
            if (!user.getPassword().equals(Md5Utlls.getMD5String(password))) {
                return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap()).setMessage("密码错误");
            }
        }
        return new ControllerResult().setRet_code(0).setRet_values(user).setMessage("登陆成功");
    }

    /**
     * 判断是否绑定过手机号
     *
     * @param thirdLogin
     * @param version
     * @return
     */
    @ApiOperation(value = "判断是否绑定过手机号")
    @RequestMapping(value = "judgeIsBindPhone/{version}", method = RequestMethod.POST)
    public ControllerResult judgeIsBindPhone(@RequestBody User thirdLogin, @PathVariable String version) {
        ThirdUser thirdUser = thirdUserService.findThirdUser(thirdLogin.getThirdUserId());
        if (thirdUser == null) {
            return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap()).setMessage("没有绑定过手机号");
        } else {
            User user = userService.findOne(thirdUser.getUser().getUserId());
            return new ControllerResult().setRet_code(0).setRet_values(user).setMessage("登陆成功");
        }
    }

    /**
     * 第三方登陆(绑定手机号)
     *
     * @param thirdLogin
     * @return
     */
    @ApiOperation(value = "第三方登陆(绑定手机号)")
    @RequestMapping(value = "thirdLogin/{version}", method = RequestMethod.POST)
    public ControllerResult thirdLogin(@RequestBody User thirdLogin, @PathVariable String version) throws Exception{
        if ("1".equals(version)) {
            if (StringUtils.isBlank(thirdLogin.getNickName())) {
                thirdLogin.setNickName(RandomStringUtils.randomNumeric(8));
            }
            if (StringUtils.isBlank(thirdLogin.getHeadImgUrl())) {
                thirdLogin.setHeadImgUrl(userDefaultAvatarUrl);
            }
            User user = userService.findByThirdId(thirdLogin.getThirdUserId());
            if (user == null) {
                user = userService.insertUser(thirdLogin);
                // 生成融云token
                RongCloud  rongCloud = RongCloud.getInstance(rongCloudAppKey, rongCloudAppSecret);
                com.yk.example.rongCloud.methods.user.User rongCloudUser = rongCloud.user;
                UserModel userModel = new UserModel()
                        .setId(user.getUserId())
                        .setName(user.getNickName())
                        .setPortrait(user.getHeadImgUrl());
                TokenResult tokenResult = rongCloudUser.register(userModel);
                user.setRongCloudToken(tokenResult.getToken());
                user = userService.insertUser(user);
            }
            return new ControllerResult().setRet_code(0).setRet_values(user).setMessage("登陆成功");
        }
        // 第三方登录版本号2
        else if ("2".equals(version)) {
            String code = thirdLogin.getCode();
            String redisCode = redisTemplate.opsForValue().get(thirdLogin.getPhone() + "_bind");
            if (StringUtils.isBlank(redisCode)) {
                return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap()).setMessage("验证码错误");
            }
            if (!redisCode.equals(code)) {
                return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap()).setMessage("验证码错误");
            }
            // 判断手机号是否已注册过
            User user = userService.findByPhone(thirdLogin.getPhone());
            if (user == null) {
                user = userService.insertUser(thirdLogin);
                // 生成融云token
                RongCloud  rongCloud = RongCloud.getInstance(rongCloudAppKey, rongCloudAppSecret);
                com.yk.example.rongCloud.methods.user.User rongCloudUser = rongCloud.user;
                UserModel userModel = new UserModel()
                        .setId(user.getUserId())
                        .setName(user.getNickName())
                        .setPortrait(user.getHeadImgUrl());
                TokenResult tokenResult = rongCloudUser.register(userModel);
                user.setRongCloudToken(tokenResult.getToken());
                user = userService.insertUser(user);
                ThirdUser thirdUser = new ThirdUser();
                thirdUser.setHeadImgUrl(thirdLogin.getHeadImgUrl());
                thirdUser.setNickName(thirdLogin.getNickName());
                thirdUser.setThirdUserId(thirdLogin.getThirdUserId());
                // 绑定
                thirdUser.setStatus("1");
                thirdUser.setUser(user);
                thirdUser.setUserType(thirdLogin.getUserType());
                thirdUserService.save(thirdUser);
            } else {
                // 判断用户图像头像和昵称是否需要改变

                ThirdUser thirdUser1 = thirdUserService.findThirdUserByUserType(user, thirdLogin.getUserType());
                if (thirdUser1 != null) {
                    return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap()).setMessage("该手机号已绑定其他账号");
                }
                // 如果没有第三方用户 就添加一条记录并绑定userId
                ThirdUser existThirdUser = thirdUserService.findThirdUser(thirdLogin.getThirdUserId());
                if (existThirdUser == null) {
                    ThirdUser thirdUser = new ThirdUser();
                    thirdUser.setHeadImgUrl(thirdLogin.getHeadImgUrl());
                    thirdUser.setNickName(thirdLogin.getNickName());
                    thirdUser.setThirdUserId(thirdLogin.getThirdUserId());
                    // 绑定
                    thirdUser.setStatus("1");
                    thirdUser.setUser(user);
                    thirdUser.setUserType(thirdLogin.getUserType());
                    thirdUserService.save(thirdUser);
                }
            }
            return new ControllerResult().setRet_code(0).setRet_values(user).setMessage("登陆成功");
        }
        return null;
    }

    /**
     * 绑定第三方账号
     *
     * @param thirdLogin
     * @param version
     * @return
     */
    @ApiOperation("绑定第三方账号")
    @RequestMapping(value = "bindThirdUser/{version}", method = RequestMethod.POST)
    public ControllerResult bindThirdUser(@RequestBody User thirdLogin, @PathVariable String version) {
        ThirdUser thirdUser = thirdUserService.findThirdUser(thirdLogin.getThirdUserId());
        if (thirdUser != null) {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("已绑定过该账号");
        }
        thirdUser = new ThirdUser();
        thirdUser.setHeadImgUrl(thirdLogin.getHeadImgUrl());
        thirdUser.setNickName(thirdLogin.getNickName());
        thirdUser.setThirdUserId(thirdLogin.getThirdUserId());
        // 绑定
        thirdUser.setStatus("1");
        User user = new User();
        user.setUserId(thirdLogin.getUserId());
        thirdUser.setUser(user);
        thirdUser.setUserType(thirdLogin.getUserType());
        thirdUserService.save(thirdUser);
        return new ControllerResult().setRet_code(0).setRet_values("").setMessage("绑定成功");
    }

    /**
     * 查看第三方账号绑定状态
     *
     * @param userId
     * @param version
     * @return
     */
    @ApiOperation(value = "查看第三方账号绑定状态")
    @RequestMapping(value = "queryBindStatus/{userId}/{version}", method = RequestMethod.GET)
    public ControllerResult queryBindStatus(@PathVariable String userId, @PathVariable String version) {
        BindDto bindDto = thirdUserService.queryBindStatus(userId);
        return new ControllerResult().setRet_code(0).setRet_values(bindDto).setMessage("");
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
     *
     * @param userId
     * @param version
     * @return
     */
    @ApiOperation(value = "用户资料查询")
    @RequestMapping(value = "personInfo/{userId}/{version}", method = RequestMethod.GET)
    public ControllerResult personInfo(@PathVariable String userId, @PathVariable String version) {
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
        UserFollow follow = userFollowService.existFollow(userFollow);
        if (userFollow.isStatus()) {
            if (follow != null) {
                return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap()).setMessage("已关注该用户");
            }
        }
        if(!userFollow.isStatus()){
           if(follow == null){
               return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap()).setMessage("您还没有关注该用户");
           }
        }
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
     * 上传用户头像
     *
     * @param file
     * @param userId
     * @param version
     * @return
     */
    @ApiOperation(value = "上传用户头像")
    @RequestMapping(value = "uploadAvatar/{version}", method = RequestMethod.POST)
    public ControllerResult uploadAvatar(MultipartFile file, String userId, @PathVariable String version) {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {
            FileOutputStream outputStream = new FileOutputStream(avatarPath + "/" + fileName);
            outputStream.write(file.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String headImageUrl = avatarUrl + fileName;
        userService.updateHeadImgUrl(userId, headImageUrl);
        return new ControllerResult().setRet_code(0).setRet_values(Collections.singletonMap("url", headImageUrl)).setMessage("上传成功");
    }


    /**
     * 用户退出登录
     *
     * @param userId
     * @param version
     * @return
     */
    @ApiOperation(value = "用户退出登录")
    @RequestMapping(value = "loginOut/{userId}/{version}", method = RequestMethod.POST)
    public ControllerResult loginOut(@PathVariable String userId, @PathVariable String version) {
        return new ControllerResult().setRet_code(0).setRet_values("").setMessage("成功退出");
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
            return new ControllerResult().setRet_code(1).setRet_values(new JsonObject()).setMessage("密码错误");
        }
        Date date = new Date();
        DateTime dateTime = new DateTime(date);
        Date exprireDate = dateTime.plusMinutes(1).toDate();
        jwtToken = Jwts.builder().setSubject(login.getPhone()).setIssuedAt(date).setExpiration(exprireDate)
                .signWith(SignatureAlgorithm.HS256, "secretkey").compact();
        return new ControllerResult().setRet_code(0).setRet_values(jwtToken).setMessage("");

    }


}
