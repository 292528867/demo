package com.yk.example.controller;

import com.yk.example.service.UserFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yk on 2018/4/8.
 */
@RestController
@RequestMapping("friend")
public class UserFriendController {

    @Autowired
    private UserFriendService userFriendService;


}
