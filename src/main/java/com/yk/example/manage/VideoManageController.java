package com.yk.example.manage;

import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.VideoRecord;
import com.yk.example.entity.VideoTag;
import com.yk.example.service.VideoService;
import com.yk.example.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 视频后台管理
 * Created by yk on 2018/4/23.
 */
@Controller
public class VideoManageController {

    @Autowired
    private VideoService videoService;

    /**
     * @param model
     * @param pageCurrent 当前页
     * @param pageSize    每页个数
     * @param pageCount   总共多少页
     * @return
     */
    @RequestMapping(value = "/admin/videoList_{pageCurrent}_{pageSize}_{pageCount}", method = RequestMethod.GET)
    public String index(Model model, VideoRecord videoRecord, @PathVariable Integer pageCurrent, @PathVariable Integer pageSize, @PathVariable Integer pageCount) {
        if (pageCurrent == 0) {
            pageCurrent = 1;
        }
        if (pageSize == 0) {
            pageSize = 10;
        }
        Page<VideoRecord> videoRecordPage = videoService.findAllPage(videoRecord, new PageRequest(pageCurrent - 1, pageSize));
        model.addAttribute("videoList", videoRecordPage.getContent());
        String pageHTML = PageUtils.getPageContent("videoList_{pageCurrent}_{pageSize}_{pageCount}?videoUrl=" + videoRecord.getVideoUrl(), pageCurrent, pageSize, videoRecordPage.getTotalPages());
        model.addAttribute("pageHTML", pageHTML);
        model.addAttribute("video", videoRecord);
        return "video/videoManage";
    }

    /**
     *  屏蔽视频
     * @param videoRecord
     * @return
     */
    @RequestMapping(value = "/admin/shieldVideo", method = RequestMethod.POST)
    @ResponseBody
    public ControllerResult shieldVideo(VideoRecord videoRecord){
        videoService.shieldVideo(videoRecord.getId());
        return new ControllerResult().setRet_code(0).setMessage("屏蔽成功");
    }
}
