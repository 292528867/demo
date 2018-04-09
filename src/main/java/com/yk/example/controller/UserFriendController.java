package com.yk.example.controller;

import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.UserFriend;
import com.yk.example.service.UserFriendService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yk on 2018/4/8.
 */
@RestController
@RequestMapping("friend")
public class UserFriendController {

    @Autowired
    private UserFriendService userFriendService;

    /**
     * 添加好友
     *
     * @param version
     * @param userFriend
     * @return
     */
    @ApiOperation(value = "添加好友")
    @RequestMapping(value = "friendApply/{version}", method = RequestMethod.POST)
    public ControllerResult friendApply(@PathVariable String version, @RequestBody UserFriend userFriend) {
        userFriendService.friendApply(userFriend);
        return new ControllerResult().setRet_code(0).setRet_values("").setMessage("");
    }

    /**
     * 同意添加好友
     *
     * @param version
     * @param userFriend
     * @return
     */
    @ApiOperation(value = "同意添加好友")
    @RequestMapping(value = "agreeApply/{version}", method = RequestMethod.POST)
    public ControllerResult agreeApply(@PathVariable String version, @RequestBody UserFriend userFriend) {
        userFriendService.agreeApply(userFriend);
        return new ControllerResult().setRet_code(0).setRet_values("").setMessage("");
    }

    /**
     * 好友列表
     * @param version
     * @param userId
     * @return
     */
    @ApiOperation(value = "好友列表")
    @RequestMapping(value = "allFriend/{userId}/{version}", method = RequestMethod.POST)
    public ControllerResult allFriend(@PathVariable String version, @PathVariable String userId) {
        List<UserFriend> userFriends = userFriendService.findByToUser(userId);
        return new ControllerResult().setRet_code(0).setRet_values(userFriends).setMessage("");
    }
}
