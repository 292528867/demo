package com.yk.example.controller;

import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.PushSet;
import com.yk.example.rongCloud.methods.message._private.Private;
import com.yk.example.service.PushSetService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by yk on 2018/5/2.
 */
@RestController
@RequestMapping("pushSet")
public class PushSetController {

    @Autowired
    private PushSetService pushSetService;

    /**
     * 查询推送设置
     * @param userId
     * @param version
     * @return
     */
    @ApiOperation(value = "查询推送设置")
    @RequestMapping(value = "findPushSet/{userId}/{version}",method = RequestMethod.GET)
    public ControllerResult findPushSet(@PathVariable String userId,@PathVariable String version) {
       PushSet pushSet = pushSetService.findPushSet(userId);
        return new ControllerResult().setRet_code(0).setRet_values(pushSet).setMessage("");
    }

    /**
     * 更改推送设置
     * @param pushSet
     * @return
     */
    @ApiOperation(value = "更改推送设置")
    @RequestMapping(value = "updatePushSet/{version}",method = RequestMethod.POST)
    public ControllerResult updatePushSet(@RequestBody PushSet pushSet){
         pushSet = pushSetService.save(pushSet);
        return new ControllerResult().setRet_code(0).setRet_values(pushSet).setMessage("");
    }
}
