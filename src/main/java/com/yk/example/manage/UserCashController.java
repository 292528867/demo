package com.yk.example.manage;

import com.yk.example.entity.UserCash;
import com.yk.example.service.UserCashService;
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
 * Created by yk on 2018/4/2.
 */
@ApiIgnore
@Controller
public class UserCashController {

    @Autowired
    private UserCashService userCashService;

    /**
     * @param model
     * @param userCash
     * @param pageCurrent
     * @param pageSize
     * @param pageCount
     * @return
     */
    @RequestMapping(value = "/admin/userCashList_{pageCurrent}_{pageSize}_{pageCount}", method = RequestMethod.GET)
    public String index(Model model, UserCash userCash, @PathVariable Integer pageCurrent, @PathVariable Integer pageSize, @PathVariable Integer pageCount) {
        if (pageCurrent == 0) {
            pageCurrent = 1;
        }
        if (pageSize == 0) {
            pageSize = 10;
        }
        Page<UserCash> userCashPage = userCashService.findAll(userCash, new PageRequest(pageCurrent - 1, pageSize));
        model.addAttribute("userCashList", userCashPage.getContent());
        String pageHTML = PageUtils.getPageContent("userCashList_{pageCurrent}_{pageSize}_{pageCount}?auditStatus=" + userCash.getAuditStatus(), pageCurrent, pageSize, userCashPage.getTotalPages());
        model.addAttribute("pageHTML", pageHTML);
        model.addAttribute("userCash", userCash);
        return "user/userCashManage";
    }
}
