package com.yk.example.controller;

import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.VideoRate;
import com.yk.example.service.VideoRateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by yk on 2018/4/19.
 */
@RestController
@RequestMapping("videoRate")
public class VideoRateContorller {

    @Autowired
    private VideoRateService videoRateService;

    @RequestMapping(value = "rate/{version}",method = RequestMethod.POST)
    public ControllerResult rate(@RequestBody  VideoRate rate, @PathVariable String version){
        if(rate != null && rate.getUser() != null  && StringUtils.isNotBlank(rate.getUser().getUserId())){
            VideoRate videoRate = videoRateService.findByUserAndVideo(rate);
            if(videoRate != null){
                return new ControllerResult().setRet_code(1).setRet_values("").setMessage("该视频您已评级过");
            }
        }
        videoRateService.save(rate);
        return new ControllerResult().setRet_code(0).setRet_values("").setMessage("");
    }
}
