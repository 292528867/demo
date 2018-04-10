package com.yk.example.controller;

import com.yk.example.dto.ControllerResult;
import com.yk.example.dto.VodAuth;
import com.yk.example.entity.*;
import com.yk.example.service.*;
import com.yk.example.utils.VodUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private VideoZanService videoZanService;

    @Autowired
    private MusicService musicService;

    @Autowired
    private VideoCollectService videoCollectService;

    @Autowired
    private UserFollowService userFollowService;


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
                                   @PathVariable String version) {
        List<VideoRecord> videoRecords = videoService.nearby(longitude, latitude, 0, 0);
        return new ControllerResult().setRet_code(0).setRet_values(videoRecords).setMessage("");
    }

    /**
     * 推荐的视频
     *
     * @param userId
     * @param version
     * @return
     */
    @ApiOperation(value = "推荐的视频")
    @RequestMapping(value = "recommend/{version}", method = RequestMethod.GET)
    public ControllerResult recommend(String userId, @PathVariable String version) {
        List<VideoRecord> videoRecords = videoService.recommend(userId);
        return new ControllerResult().setRet_code(0).setRet_values(videoRecords).setMessage("");
    }

    /**
     * 查询标签
     *
     * @return
     */
    @ApiOperation(value = "查询标签")
    @RequestMapping(value = "queryTag/{version}", method = RequestMethod.GET)
    public ControllerResult allTag(@PathVariable String version, VideoTag videoTag) {
        List<VideoTag> list = videoTagService.findAll(videoTag);
        return new ControllerResult().setRet_code(0).setRet_values(list).setMessage("");
    }

    /**
     * 视频详情
     *
     * @param videoId
     * @param version
     * @return
     */
    @ApiOperation(value = "视频详情")
    @RequestMapping(value = "videoDetail/{videoId}/{version}", method = RequestMethod.GET)
    public ControllerResult videoDetail(@PathVariable String videoId, @PathVariable String version, String userId) {
        VideoRecord videoRecord = videoService.findOne(videoId, userId);
        return new ControllerResult().setRet_code(0).setRet_values(videoRecord).setMessage("");
    }

    /**
     * 视频点赞
     *
     * @param videoZan
     * @param version
     * @return
     */
    @ApiOperation(value = "视频点赞")
    @RequestMapping(value = "videoZan/{version}", method = RequestMethod.POST)
    public ControllerResult videoZan(@RequestBody VideoZan videoZan, @PathVariable String version) {
        if (StringUtils.isNotBlank(videoZan.getUser().getUserId())) {
            videoZanService.save(videoZan);
            return new ControllerResult().setRet_code(0).setRet_values("").setMessage("");
        }
        return new ControllerResult().setRet_code(99).setRet_values("").setMessage("请先登录");
    }

    /**
     * 音乐列表
     *
     * @param version
     * @return
     */
    @ApiOperation(value = "音乐列表")
    @RequestMapping(value = "allMusic/{version}", method = RequestMethod.GET)
    public ControllerResult allMusic(@PathVariable String version) {
        List<Music> musics = musicService.findAll();
        return new ControllerResult().setRet_code(0).setRet_values(musics).setMessage("");
    }

    /**
     * 秒转
     *
     * @param VideoCollect
     * @param version
     * @return
     */
    @ApiOperation(value = "秒转")
    @RequestMapping(value = "collectVideo/{version}", method = RequestMethod.POST)
    public ControllerResult collectVideo(@RequestBody VideoCollect VideoCollect, @PathVariable String version) {
        VideoCollect collect = videoCollectService.existCollect(VideoCollect.getVideoRecord().getId(), VideoCollect.getUser().getUserId());
        if (collect != null) {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("已经秒转过");
        }
        videoCollectService.save(VideoCollect);
        return new ControllerResult().setRet_code(0).setRet_values("").setMessage("");
    }

    /**
     * 获取短视频上传凭证
     *
     * @param version
     * @param fileName
     * @param userId
     * @param title
     * @return
     */
    @ApiOperation(value = "获取短视频上传凭证")
    @RequestMapping(value = "createUploadVideo/{version}", method = RequestMethod.GET)
    public ControllerResult createUploadVideo(@PathVariable String version, String fileName, String userId, String title) {
        VodAuth vodAuth = VodUtils.createUploadVideo(userId + "_" + System.currentTimeMillis() + "_" + fileName, title);
        return new ControllerResult().setRet_code(0).setRet_values(vodAuth).setMessage("");
    }

    /**
     * 刷新短视频上传凭证
     *
     * @param version
     * @param aliVideoId
     * @return
     */
    @ApiOperation(value = "刷新短视频上传凭证")
    @RequestMapping(value = "refreshUploadVideo/{version}", method = RequestMethod.GET)
    public ControllerResult refreshUploadVideo(@PathVariable String version, String aliVideoId) {
        VodUtils.refreshUploadVideo(aliVideoId);
        return new ControllerResult().setRet_code(0).setRet_values("").setMessage("");
    }

    /**
     * 关注用户列表
     *
     * @param version
     * @param userId
     * @return
     */
    @ApiOperation(value = "关注列表")
    @RequestMapping(value = "allFollow/{userId}/{version}", method = RequestMethod.GET)
    public ControllerResult allFollow(@PathVariable String version, @PathVariable String userId) {
        List<VideoRecord> videoRecords = userFollowService.findByUserId(userId);
        return new ControllerResult().setRet_code(0).setRet_values(videoRecords).setMessage("");
    }

    /**
     *  我喜欢的视频
     * @param version
     * @param userId
     * @return
     */
    @ApiOperation(value = "我喜欢的视频")
    @RequestMapping(value = "myLike/{userId}/{version}", method = RequestMethod.GET)
    public ControllerResult myLike(@PathVariable String version, @PathVariable String userId,int page,int size){
        Page<VideoCollect> videoCollects = videoCollectService.findByUserId(userId,new PageRequest(page,size));
        return new ControllerResult().setRet_code(0).setRet_values(videoCollects).setMessage("");
    }

    /**
     *  我的作品
     * @param version
     * @param userId
     * @return
     */
    @ApiOperation(value = "我的作品")
    @RequestMapping(value = "myVideo/{userId}/{version}", method = RequestMethod.GET)
    public ControllerResult myVideo (@PathVariable String version, @PathVariable String userId, int page,int size){
        Page<VideoRecord> videoRecords = videoService.findByUser(userId,new PageRequest(page,size));
        return new ControllerResult().setRet_code(0).setRet_values(videoRecords).setMessage("");
    }
}
