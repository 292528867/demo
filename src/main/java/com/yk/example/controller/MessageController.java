package com.yk.example.controller;

import com.yk.example.dto.ControllerResult;
import com.yk.example.dto.FansDto;
import com.yk.example.entity.Message;
import com.yk.example.entity.UserFollow;
import com.yk.example.entity.ZanRecordHistory;
import com.yk.example.rongCloud.models.response.ResponseResult;
import com.yk.example.service.MessageService;
import com.yk.example.service.UserFollowService;
import com.yk.example.service.ZanRecordHistoryService;
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
@RequestMapping(value = "message")
public class MessageController {


    @Autowired
    private MessageService messageService;

    @Autowired
    private UserFollowService userFollowService;

    @Autowired
    private ZanRecordHistoryService zanRecordHistoryService;

    /**
     * 粉丝列表
     *
     * @param userId
     * @param version
     * @return
     */
    @ApiOperation(value = "粉丝列表")
    @RequestMapping(value = "fanList/{userId}/{version}", method = RequestMethod.GET)
    public ControllerResult fanList(@PathVariable String userId, @PathVariable String version) {
        List<FansDto> fansDtos = userFollowService.fanList(userId);
        return new ControllerResult().setRet_code(0).setRet_values(fansDtos).setMessage("");
    }

    /**
     * 点赞消息列表
     *
     * @param userId
     * @param version
     * @return
     */
    @ApiOperation(value = "点赞消息列表")
    @RequestMapping(value = "zanList/{userId}/{version}", method = RequestMethod.GET)
    public ControllerResult zanList(@PathVariable String userId, @PathVariable String version) {
        List<ZanRecordHistory> zanRecordHistories = zanRecordHistoryService.zanList(userId);
        return new ControllerResult().setRet_code(0).setRet_values(zanRecordHistories).setMessage("");
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
