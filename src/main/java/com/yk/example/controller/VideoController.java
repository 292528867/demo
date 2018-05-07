package com.yk.example.controller;

import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.ProtocolType;
import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.*;
import com.yk.example.enums.ZanStatus;
import com.yk.example.service.*;
import com.yk.example.utils.VodUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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

    @Autowired
    private UserService userService;

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
        return new ControllerResult().setRet_code(0).setRet_values(videoRecords == null ? Collections.emptyList() : videoRecords).setMessage("");
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
    public ControllerResult recommend(String userId, @PathVariable String version, String page, String size) {
        if ("1".equals(version)) {
            List<VideoRecord> videoRecords = videoService.recommend(userId);
            return new ControllerResult().setRet_code(0).setRet_values(videoRecords == null ? Collections.emptyList() : videoRecords).setMessage("");
        } else if ("2".equals(version)) {
            Page<VideoRecord> videoRecordPage = videoService.recommend(userId, new PageRequest(Integer.parseInt(page), Integer.parseInt(size)));
            return new ControllerResult().setRet_code(0).setRet_values(videoRecordPage).setMessage("");
        }
        return null;
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
            VideoZan newZan = null;
            if (videoZan.getZanStatus().equals(ZanStatus.zan)) {
                newZan = videoZanService.findByUserAndVideo(videoZan);
            }
            if (newZan == null) {
                videoZanService.save(videoZan);
                return new ControllerResult().setRet_code(0).setRet_values("").setMessage("");
            } else {
                return new ControllerResult().setRet_code(1).setRet_values("").setMessage("已经对该视频点赞过");
            }
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
     * 取消秒转
     *
     * @param version
     * @return
     */
    @ApiOperation(value = "删除秒选的视频")
    @RequestMapping(value = "cancelCollect/{version}", method = RequestMethod.POST)
    public ControllerResult cancelCollect(@PathVariable String version, @RequestBody VideoCollect videoCollect) {
        videoCollectService.deleteCollect(videoCollect.getId());
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
                "      \"Action\": [\"vod:GetPlayInfo\",\"vod:GetVideoPlayAuth\",\"vod:GetVideoPlayInfo\",\"vod:GetVideoInfo\",\"vod:CreateUploadVideo\",\"vod:RefreshUploadVideo\",\"vod:CreateUploadImage\"],\n" +
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
        if (videoRecord.getTag() == null || StringUtils.isBlank(videoRecord.getTag().getId())) {
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("视频标签不能为空");
        }
        VideoRecord newVideo = videoService.save(videoRecord);
        return new ControllerResult().setRet_code(0).setRet_values(newVideo).setMessage("");
    }

    /**
     * 删除用户的视频
     *
     * @param version
     * @param videoRecord
     * @return
     */
    @ApiOperation(value = "删除用户的视频")
    @RequestMapping(value = "deleteVideo/{version}", method = RequestMethod.POST)
    public ControllerResult deleteVideo(@PathVariable String version, @RequestBody VideoRecord videoRecord) {
        boolean existVideo = videoService.existVideo(videoRecord);
        if (existVideo) {
            videoService.deleteVideo(videoRecord);
            return new ControllerResult().setRet_code(1).setRet_values("").setMessage("");
        }
        return new ControllerResult().setRet_code(1).setRet_values("").setMessage("用户没有权限删除该视频");
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
        return new ControllerResult().setRet_code(0).setRet_values(videoRecords == null ? Collections.emptyList() : videoRecords).setMessage("");
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
        // 创建时间排序
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Page<VideoCollect> videoCollects = videoCollectService.findByUserId(userId, new PageRequest(page, size, sort));
        PageImpl<VideoCollect> videoCollectPage = new PageImpl<VideoCollect>(videoCollects.getContent(), new PageRequest(page, size), videoCollects.getTotalElements());
        UserInfo userInfo = userInfoService.findUserInfo(userId);
        long videoNum = videoService.countByUserId(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("videoCollects", videoCollectPage);
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
        // 创建时间排序
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(page, size, sort);
        Page<VideoRecord> videoRecords = videoService.findByUser(userId, pageable);
        PageImpl<VideoRecord> videoRecordPage = new PageImpl<VideoRecord>(videoRecords.getContent(), new PageRequest(page, size), videoRecords.getTotalElements());
        long likeNum = videoCollectService.countByUserId(userId);
        UserInfo userInfo = userInfoService.findUserInfo(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("videoRecords", videoRecordPage);
        result.put("zanNum", userInfo.getZanNum());
        result.put("fanNum", userInfo.getFanNum());
        result.put("followNum", userInfo.getFollowNum());
        result.put("videoNum", videoRecords.getTotalElements());
        result.put("likeNum", likeNum);
        return new ControllerResult().setRet_code(0).setRet_values(result).setMessage("");
    }


    /**
     * 他人主页中我喜欢的视频
     *
     * @param version
     * @param userId
     * @return
     */
    @ApiOperation(value = "他人主页中我喜欢的视频")
    @RequestMapping(value = "otherLike/{version}", method = RequestMethod.GET)
    public ControllerResult otherLike(@PathVariable String version, String userId, int page, int size, String otherUserId) {
        // 创建时间排序
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Page<VideoCollect> videoCollects = videoCollectService.findByUserId(otherUserId, new PageRequest(page, size, sort));
        UserFollow userFollow = userFollowService.existFollow(userId, otherUserId);
        boolean follow = false;
        if (userFollow != null) {
            follow = true;
        }
        List<VideoCollect> VideoCollectList = videoCollects.getContent();
        if (VideoCollectList!= null && VideoCollectList.size() > 0) {
            for(VideoCollect collect : VideoCollectList){
                collect.getVideoRecord().setFollow(true);
            }
        }
        PageImpl<VideoCollect> videoCollectPage = new PageImpl<VideoCollect>(VideoCollectList, new PageRequest(page, size), videoCollects.getTotalElements());
        UserInfo userInfo = userInfoService.findUserInfo(otherUserId);
        User user = userService.findOne(otherUserId);
        long videoNum = videoService.countByUserId(otherUserId);
        Map<String, Object> result = new HashMap<>();
        result.put("videoCollects", videoCollectPage);
        result.put("zanNum", userInfo.getZanNum());
        result.put("inviteCode", user.getInviteCode());
        result.put("rongCloudToken", user.getRongCloudToken());
        result.put("zanNum", userInfo.getZanNum());
        result.put("fanNum", userInfo.getFanNum());
        result.put("followNum", userInfo.getFollowNum());
        result.put("videoNum", videoNum);
        result.put("likeNum", videoCollects.getTotalElements());
        result.put("follow", follow);

        return new ControllerResult().setRet_code(0).setRet_values(result).setMessage("");
    }

    /**
     * 他人主页中我的作品
     *
     * @param version
     * @param userId
     * @return
     */
    @ApiOperation(value = "他人主页中我的作品")
    @RequestMapping(value = "otherVideo/{version}", method = RequestMethod.GET)
    public ControllerResult otherVideo(@PathVariable String version, String userId, int page, int size, String otherUserId) {
        // 创建时间排序
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(page, size, sort);
        Page<VideoRecord> videoRecords = videoService.findByUser(otherUserId, pageable);
        UserFollow userFollow = userFollowService.existFollow(userId, otherUserId);
        Map<String, Object> result = new HashMap<>();
        boolean follow = false;
        if (userFollow != null) {
            follow = true;
        }
        List<VideoRecord> videoRecordList = videoRecords.getContent();
        if (videoRecordList!= null && videoRecordList.size() > 0) {
             for(VideoRecord record : videoRecordList){
                   record.setFollow(follow);
             }
        }
        PageImpl<VideoRecord> videoRecordPage = new PageImpl<VideoRecord>(videoRecordList, new PageRequest(page, size), videoRecords.getTotalElements());
        long likeNum = videoCollectService.countByUserId(otherUserId);
        UserInfo userInfo = userInfoService.findUserInfo(otherUserId);
        User user = userService.findOne(otherUserId);
        result.put("videoRecords", videoRecordPage);
        result.put("zanNum", userInfo.getZanNum());
        result.put("fanNum", userInfo.getFanNum());
        result.put("followNum", userInfo.getFollowNum());
        result.put("videoNum", videoRecords.getTotalElements());
        result.put("likeNum", likeNum);
        result.put("inviteCode", user.getInviteCode());
        result.put("rongCloudToken", user.getRongCloudToken());
        result.put("follow", follow);
        return new ControllerResult().setRet_code(0).setRet_values(result).setMessage("");
    }

    /**
     * 根据视频标签搜索视频
     *
     * @param version
     * @return
     */
    @ApiOperation(value = "根据视频标签搜索视频")
    @RequestMapping(value = "findVideoByTagName/{version}", method = RequestMethod.GET)
    public ControllerResult findVideoByTagName(@PathVariable String version, String tagName, int page, int size) {
        Page<VideoRecord> videoRecords = videoService.findByTag(tagName, new PageRequest(page, size));
        return new ControllerResult().setRet_code(0).setRet_values(videoRecords == null ? Collections.emptyMap() : videoRecords).setMessage("");
    }
}