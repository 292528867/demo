package com.yk.example.manage;

import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.User;
import com.yk.example.entity.VideoRecord;
import com.yk.example.service.UserService;
import com.yk.example.service.VideoService;
import com.yk.example.utils.ImportExcelUtils;
import com.yk.example.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 视频后台管理
 * Created by yk on 2018/4/23.
 */
@Controller
public class VideoManageController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * @param model
     * @param pageCurrent 当前页
     * @param pageSize    每页个数
     * @param pageCount   总共多少页
     * @return
     */
    @RequestMapping(value = "/admin/videoList_{pageCurrent}_{pageSize}_{pageCount}", method = RequestMethod.GET)
    public String index(Model model, VideoRecord videoRecord, @PathVariable Integer pageCurrent, @PathVariable Integer pageSize, @PathVariable Integer pageCount, String phone) {
        if (pageCurrent == 0) {
            pageCurrent = 1;
        }
        if (pageSize == 0) {
            pageSize = 30;
        }
        if (StringUtils.isNotBlank(phone)) {
            User user = userService.findByPhone(phone);
            videoRecord.setUser(user);
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Page<VideoRecord> videoRecordPage = videoService.findAllPage(videoRecord, new PageRequest(pageCurrent - 1, pageSize, sort));
        model.addAttribute("videoList", videoRecordPage.getContent());
        String pageHTML = PageUtils.getPageContent("videoList_{pageCurrent}_{pageSize}_{pageCount}?phone=" + phone + "&title=" + videoRecord.getVideoUrl(), pageCurrent, pageSize, videoRecordPage.getTotalPages());
        model.addAttribute("pageHTML", pageHTML);
        model.addAttribute("video", videoRecord);
        model.addAttribute("phone", phone);
        model.addAttribute("status", redisTemplate.opsForValue().get("video_first_check_or_publish"));
        return "video/videoManage";
    }

    /**
     * 通过或屏蔽视频
     *
     * @param videoRecord
     * @return
     */
    @RequestMapping(value = "/admin/agreeOrShieldVideo", method = RequestMethod.POST)
    @ResponseBody
    public ControllerResult agreeOrShieldVideo(VideoRecord videoRecord) {
        videoService.agreeOrShieldVideo(videoRecord.getId(), videoRecord.getFlag());
        return new ControllerResult().setRet_code(0).setMessage("屏蔽成功");
    }

    @RequestMapping(value = "/admin/toVideoImport", method = RequestMethod.GET)
    public String toVideoImport(Model model) {
        return "video/videoImport";
    }

    /**
     * 导入视频excel
     *
     * @param videoFile
     * @return
     */
    @RequestMapping(value = "/admin/importVideo", method = RequestMethod.POST)
    @ResponseBody
    public ControllerResult importVideo(MultipartFile videoFile) {
        if (videoFile != null) {
            ImportExcelUtils poi = new ImportExcelUtils();
            try {
                List<List<String>> list = poi.read(videoFile.getInputStream(), false);
                videoService.importVideo(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ControllerResult().setRet_code(0).setRet_values("").setMessage("'");
    }

    @RequestMapping(value = "updateVideoImage")
    @ResponseBody
    public ControllerResult updateVideoImage() {
        videoService.updateVideoImage();
        return new ControllerResult().setRet_code(0).setRet_values("").setMessage("");
    }

    /**
     * 设置全局的先审后发还是先发后审
     *
     * @param value
     * @return
     */
    @RequestMapping(value = "admin/setFirstCheckOrPublish",method = RequestMethod.POST)
    @ResponseBody
    public ControllerResult setFirstCheckOrPublish(String value) {
        redisTemplate.opsForValue().set("video_first_check_or_publish", value);
        return new ControllerResult().setRet_code(0).setRet_values("").setMessage("");
    }
}
