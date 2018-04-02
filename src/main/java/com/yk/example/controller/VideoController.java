package com.yk.example.controller;

import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.VideoRecord;
import com.yk.example.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yk on 2018/4/2.
 */
@RestController
@RequestMapping("video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    /**
     * 查询附近的用户最新的视频
     *
     * @param longitude
     * @param latitude
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "nearby", method = RequestMethod.GET)
    private ControllerResult nearby(double longitude, double latitude, int page, int size) {
        List<VideoRecord> videoRecords = videoService.nearby(longitude, latitude, page, size);
        return new ControllerResult().setRet_values(videoRecords).setMessage("");
    }


}
