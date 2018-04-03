package com.yk.example.controller;

import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.VideoRecord;
import com.yk.example.entity.VideoTag;
import com.yk.example.service.VideoService;
import com.yk.example.service.VideoTagService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
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


    @Autowired
    private VideoTagService videoTagService;

    /**
     * 查询附近的用户最新的视频
     *
     * @param longitude
     * @param latitude
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "附近的视频")
    @RequestMapping(value = "nearby/{version}", method = RequestMethod.GET)
    public ControllerResult nearby(@ApiParam(name = "longitude", value = "经度", required = true) double longitude,
                                   @ApiParam(name = "latitude", value = "纬度", required = true) double latitude,
                                   int page, int size, @PathVariable String version) {
        List<VideoRecord> videoRecords = videoService.nearby(longitude, latitude, page, size);
        return new ControllerResult().setRet_values(videoRecords).setMessage("");
    }

    /**
     * 查询所有的视频标签
     *
     * @return
     */
    @ApiOperation(value = "视频标签列表")
    @RequestMapping(value = "allTag/{version}", method = RequestMethod.GET)
    public ControllerResult allTag(@PathVariable String version) {
        List<VideoTag> list = videoTagService.findAll(null);
        return new ControllerResult().setRet_values(list).setMessage("");
    }


}
