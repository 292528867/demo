package com.yk.example.manage;

import com.yk.example.entity.Feedback;
import com.yk.example.service.FeedbackService;
import com.yk.example.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 */
@ApiIgnore
@Controller
public class FeedbackManageController {

    @Autowired
    private FeedbackService feedbackService;

    /**
     * @param model
     * @param user
     * @param pageCurrent 当前页
     * @param pageSize    每页个数
     * @param pageCount   总共多少页
     * @return
     */
    @RequestMapping(value = "/admin/feedbackList_{pageCurrent}_{pageSize}_{pageCount}", method = RequestMethod.GET)
    public String index(Model model, Feedback feedback, @PathVariable Integer pageCurrent, @PathVariable Integer pageSize, @PathVariable Integer pageCount) {
        if (pageCurrent == 0) {
            pageCurrent = 1;
        }
        if (pageSize == 0) {
            pageSize = 10;
        }
        Page<Feedback> tagPage = feedbackService.findAllPage(feedback, new PageRequest(pageCurrent - 1, pageSize));
        model.addAttribute("feedbackList", tagPage.getContent());
        String pageHTML = PageUtils.getPageContent("videoTagList_{pageCurrent}_{pageSize}_{pageCount}" , pageCurrent, pageSize, tagPage.getTotalPages());
        model.addAttribute("pageHTML", pageHTML);
        model.addAttribute("feedback", feedback);
        return "feedback/feedbackManage";
    }

}