package com.yk.example.manage;

import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.User;
import com.yk.example.entity.VideoTag;
import com.yk.example.service.VideoTagService;
import com.yk.example.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by yk on 2018/4/17.
 */
@ApiIgnore
@Controller
public class VideoTagController {

    @Autowired
    private VideoTagService videoTagService;

    /**
     * @param model
     * @param user
     * @param pageCurrent 当前页
     * @param pageSize    每页个数
     * @param pageCount   总共多少页
     * @return
     */
    @RequestMapping(value = "/admin/videoTagList_{pageCurrent}_{pageSize}_{pageCount}", method = RequestMethod.GET)
    public String index(Model model, VideoTag tag, @PathVariable Integer pageCurrent, @PathVariable Integer pageSize, @PathVariable Integer pageCount) {
        if (pageCurrent == 0) {
            pageCurrent = 1;
        }
        if (pageSize == 0) {
            pageSize = 10;
        }
        Page<VideoTag> tagPage = videoTagService.findAllPage(tag, new PageRequest(pageCurrent - 1, pageSize));
        model.addAttribute("tagList", tagPage.getContent());
        String pageHTML = PageUtils.getPageContent("videoTagList_{pageCurrent}_{pageSize}_{pageCount}?name=" + tag.getName(), pageCurrent, pageSize, tagPage.getTotalPages());
        model.addAttribute("pageHTML", pageHTML);
        model.addAttribute("tag", tag);
        return "video/videoTagManage";
    }

    /**
     * 视频标签编辑
     *
     * @param
     * @param model
     * @return
     */
    @RequestMapping(value = "/admin/tagEdit")
    public String userEdit(VideoTag tag, Model model) {
        if (tag != null && StringUtils.isNotBlank(tag.getId())) {
            tag = videoTagService.findOne(tag.getId());
        }
        model.addAttribute("tag", tag);
        return "video/videoTagEdit";
    }

    /**
     * 视频标签保存
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/admin/tagSave")
    public String save(VideoTag tag) {
        videoTagService.save(tag);
        return "redirect:videoTagList_0_0_0";
    }

    /**
     * 删除标签
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/admin/delTag")
    @ResponseBody
    public ControllerResult updateUserState(String id) {
        videoTagService.delTage(id);
        return new ControllerResult().setRet_code(0).setMessage("删除成功");
    }
}
