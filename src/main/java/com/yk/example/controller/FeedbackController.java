package com.yk.example.controller;

import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.Feedback;
import com.yk.example.service.FeedbackService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping("feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    /**
     *  提交反馈
     * @param feedback
     * @param version
     * @return
     */
    @ApiOperation("提交反馈")
    @RequestMapping(value = "submit/{version}",method = RequestMethod.POST)
    public ControllerResult submit(@RequestBody Feedback feedback, @PathVariable String version){
        feedbackService.save(feedback);
        return new ControllerResult().setRet_code(0).setRet_values("").setMessage("反馈成功");
    }

}