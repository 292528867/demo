package com.yk.example.controller;

import com.yk.example.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by Administrator on 2017/9/11.
 */
@ApiIgnore
@Controller
public class LoginController {

    @RequestMapping("")
    public String index(){
        return "login";
    }

    @RequestMapping(value = "hello",method = RequestMethod.GET)
    public String hello(Model model) {
        model.addAttribute("hello", "welcome yk");
        return "hello";
    }

    @RequestMapping(value = "/admin",method = RequestMethod.POST)
    public String hello(Model model, User user) {
        model.addAttribute("name", user.getNickName());
        return "dashboard";
    }
}
