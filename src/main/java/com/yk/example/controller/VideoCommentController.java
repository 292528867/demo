package com.yk.example.controller;

import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.VideoComment;
import com.yk.example.service.VideoCommentService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yk on 2018/4/4.
 */
@RestController
@RequestMapping("comment")
public class VideoCommentController {

    @Autowired
    private VideoCommentService videoCommentService;


    /**
     * 提交视频评论
     *
     * @param videoComment
     * @param version
     * @return
     */
    @ApiOperation(value = "提交视频评论")
    @RequestMapping(value = "submit/{version}", method = RequestMethod.POST)
    public ControllerResult submit(@RequestBody VideoComment videoComment, @PathVariable String version) {
        if (StringUtils.isNotBlank(videoComment.getUser().getUserId())) {
             videoCommentService.save(videoComment);
            return new ControllerResult().setRet_code(0).setRet_values("").setMessage("");
        }
        return new ControllerResult().setRet_code(99).setRet_values("").setMessage("用户未登录,请登录");
    }

    /**
     * 查询视频的评论
     *
     * @param videoId
     * @param version
     * @return
     */
    @RequestMapping(value = "findComment/{videoId}/{version}", method = RequestMethod.GET)
    public ControllerResult findComment(@PathVariable String videoId, @PathVariable String version) {
        List<VideoComment> videoComments = videoCommentService.findAllByVideoId(videoId);
        return new ControllerResult().setRet_code(0).setRet_values(videoComments).setMessage("");
    }
}
