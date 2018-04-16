package com.yk.example.controller;

import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.ProtocolType;
import com.yk.example.dto.ControllerResult;
import com.yk.example.dto.UserInfoDto;
import com.yk.example.entity.*;
import com.yk.example.service.*;
import com.yk.example.utils.VodUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private UserInfoService userInfoService;

    @Value("${AccessKeyId}")
    private String accessKeyId;

    @Value("${AccessKeySecret}")
    private String accessKeySecret;

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
     * 获取ststoken
     *
     * @param version
     * @return
     */
    @ApiOperation(value = "获取ststoken")
    @RequestMapping(value = "getStsToken/{version}", method = RequestMethod.GET)
    public ControllerResult getStsToken(@PathVariable String version) {
        String roleArn = "acs:ram::1358121793615316:role/ramupload";
        String roleSessionName = "miaoou_upload";
        String policy = "{\n" +
                "  \"Version\": \"1\",\n" +
                "  \"Statement\": [\n" +
                "    {\n" +
                "      \"Action\": [\"vod:CreateUploadVideo\",\"vod:RefreshUploadVideo\",\"vod:CreateUploadImage\"],\n" +
                "      \"Resource\": \"*\",\n" +
                "      \"Effect\": \"Allow\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        try {
            AssumeRoleResponse response = VodUtils.assumeRole(accessKeyId, accessKeySecret, roleArn, roleSessionName, policy, ProtocolType.HTTPS);
            return new ControllerResult().setRet_code(0).setRet_values(response.getCredentials()).setMessage("");
        } catch (ClientException e) {
            e.printStackTrace();
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("获取失败");
        }
    }

    /**
     * 上传短视频
     *
     * @param videoRecord
     * @param version
     * @return
     */
    @ApiOperation(value = "上传短视频")
    @RequestMapping(value = "uploadVideo/{version}", method = RequestMethod.POST)
    public ControllerResult uploadVideo(@RequestBody VideoRecord videoRecord, @PathVariable String version) {
        VideoRecord newVideo = videoService.save(videoRecord);
        return new ControllerResult().setRet_code(0).setRet_values(newVideo).setMessage("");
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
     * 我喜欢的视频
     *
     * @param version
     * @param userId
     * @return
     */
    @ApiOperation(value = "我喜欢的视频")
    @RequestMapping(value = "myLike/{userId}/{version}", method = RequestMethod.GET)
    public ControllerResult myLike(@PathVariable String version, @PathVariable String userId, int page, int size) {
        Page<VideoCollect> videoCollects = videoCollectService.findByUserId(userId, new PageRequest(page, size));
        UserInfo userInfo = userInfoService.findUserInfo(userId);
        long videoNum = videoService.countByUserId(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("videoCollects", videoCollects);
        result.put("zanNum", userInfo.getZanNum());
        result.put("fanNum", userInfo.getFanNum());
        result.put("followNum", userInfo.getFollowNum());
        result.put("videoNum", videoNum);
        result.put("likeNum", videoCollects.getTotalElements());
        return new ControllerResult().setRet_code(0).setRet_values(result).setMessage("");
    }

    /**
     * 我的作品
     *
     * @param version
     * @param userId
     * @return
     */
    @ApiOperation(value = "我的作品")
    @RequestMapping(value = "myVideo/{userId}/{version}", method = RequestMethod.GET)
    public ControllerResult myVideo(@PathVariable String version, @PathVariable String userId, int page, int size) {
        Page<VideoRecord> videoRecords = videoService.findByUser(userId, new PageRequest(page, size));
        long likeNum = videoCollectService.countByUserId(userId);
        UserInfo userInfo = userInfoService.findUserInfo(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("videoRecords", videoRecords);
        result.put("zanNum", userInfo.getZanNum());
        result.put("fanNum", userInfo.getFanNum());
        result.put("followNum", userInfo.getFollowNum());
        result.put("videoNum", videoRecords.getTotalElements());
        result.put("likeNum", likeNum);
        return new ControllerResult().setRet_code(0).setRet_values(result).setMessage("");
    }

}