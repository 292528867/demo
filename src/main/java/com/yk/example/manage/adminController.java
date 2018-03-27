package com.yk.example.manage;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by yk on 2018/3/27.
 */
@ApiIgnore
@Controller
public class adminController {

    /**
     * 登录跳转
     *
     * @param model
     * @return
     */
    @GetMapping("/admin/login")
    public String loginGet(Model model) {
        return "login";
    }

    /**
     * 登录
     *
     * @param
     * @param model
     * @return
     */
    @PostMapping("/admin/login")
    public String loginPost( Model model) {
        return "redirect:dashboard";
    }

    /**
     * 仪表板页面
     *
     * @param model
     * @return
     */
    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        return "dashboard";
    }

}
