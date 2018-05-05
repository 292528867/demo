package com.yk.example.controller;

import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.VideoComment;
import com.yk.example.entity.VideoCommentZan;
import com.yk.example.service.VideoCommentService;
import com.yk.example.service.VideoCommentZanService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.Controller;

import java.util.List;

/**
 * Created by yk on 2018/4/4.
 */
@RestController
@RequestMapping("comment")
public class VideoCommentController {

    @Autowired
    private VideoCommentService videoCommentService;


    @Autowired
    private VideoCommentZanService videoCommentZanService;


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
    @ApiOperation(value = "查询视频的评论列表")
    @RequestMapping(value = "findComment/{videoId}/{version}", method = RequestMethod.GET)
    public ControllerResult findComment(@PathVariable String videoId, @PathVariable String version,int page ,int size,String userId) {
        Page<VideoComment> videoCommentPage = videoCommentService.findAllByVideoId(videoId,new PageRequest(page,size),userId);
        return new ControllerResult().setRet_code(0).setRet_values(videoCommentPage).setMessage("");
    }

    /**
     * 对评论进行点赞或者取消点赞
     *
     * @param videoCommentZan
     * @param version
     * @return
     */
    @ApiOperation(value = "对评论进行点赞或者取消点赞")
    @RequestMapping(value = "commentZan/{version}",method = RequestMethod.POST)
    public ControllerResult commentZan(@RequestBody VideoCommentZan videoCommentZan, @PathVariable String version) {
        if (StringUtils.isNotBlank(videoCommentZan.getUser().getUserId())) {
            videoCommentZanService.zan(videoCommentZan);
            return new ControllerResult().setRet_code(0).setRet_values("").setMessage("");
        }
        return new ControllerResult().setRet_code(99).setRet_values("").setMessage("请先登录");
    }

    /**
     *  评论的接口
     * @param version
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "评论的接口")
    @RequestMapping(value = "allComment/{userId}/{version}",method = RequestMethod.GET)
    public ControllerResult allComment(@PathVariable String version,@PathVariable String userId,int page ,int size){
        Page<VideoComment> commentPage = videoCommentService.findAllCommentByUser(userId,new PageRequest(page,size));
        return new ControllerResult().setRet_code(0).setRet_values(commentPage).setMessage("");
    }
}
