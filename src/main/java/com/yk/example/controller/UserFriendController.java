package com.yk.example.controller;

import com.yk.example.dto.ControllerResult;
import com.yk.example.dto.SearchUserDto;
import com.yk.example.entity.Message;
import com.yk.example.entity.User;
import com.yk.example.entity.UserFriend;
import com.yk.example.rongCloud.models.response.ResponseResult;
import com.yk.example.service.MessageService;
import com.yk.example.service.UserFriendService;
import com.yk.example.utils.RongCloudUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * Created by yk on 2018/4/8.
 */
@RestController
@RequestMapping("friend")
public class UserFriendController {

    @Autowired
    private UserFriendService userFriendService;

    @Autowired
    private MessageService messageService;

    /**
     *  搜索用户
     * @param version
     * @param userId
     * @param nickName
     * @return
     */
    @ApiOperation("搜索用户")
    @RequestMapping(value = "searchUser/{userId}/{version}",method = RequestMethod.GET)
    public ControllerResult searchFriend(@PathVariable String version ,@PathVariable String userId, String nickName){
        List<SearchUserDto> searchUserDtos = userFriendService.searchFriend(userId,nickName);
        return new ControllerResult().setRet_code(0).setRet_values(searchUserDtos).setMessage("");
    }

    /**
     * 发送聊天信息
     *
     * @param version
     * @param message
     * @return
     */
    @ApiOperation(value = "发送聊天信息")
    @RequestMapping(value = "sendMessage/{version}", method = RequestMethod.POST)
    public ControllerResult sendMessage(@PathVariable String version, @RequestBody Message message) {
        ResponseResult responseResult = RongCloudUtils.sendPrivateMessage(message.getFromUser().getUserId(), message.getToUser().getUserId(), message.getContent());
        if(responseResult.getCode() == 200){
            Message newMessage =  messageService.sendMessage(message);
            return new ControllerResult().setRet_code(0).setRet_values(newMessage).setMessage("发送成功");
        }
        return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap()).setMessage("发送失败");
    }
}
