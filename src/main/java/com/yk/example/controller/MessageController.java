package com.yk.example.controller;

import com.yk.example.dto.ControllerResult;
import com.yk.example.dto.FansDto;
import com.yk.example.entity.UserFollow;
import com.yk.example.service.UserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yk on 2018/4/8.
 */
@RestController
@RequestMapping(value = "message")
public class MessageController {


    @Autowired
    private UserFollowService userFollowService;

    /**
     *  粉丝列表
     * @param userId
     * @param version
     * @return
     */
    @RequestMapping(value = "fanList/{userId}/{version}",method = RequestMethod.GET)
    public ControllerResult fanList(@PathVariable String userId,@PathVariable String version){
       List<FansDto> fansDtos = userFollowService.fanList(userId);
        return new ControllerResult().setRet_code(0).setRet_values(fansDtos).setMessage("");
    }
}
