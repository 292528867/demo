package com.yk.example.service;

import com.yk.example.dao.UserfriendDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yk on 2018/4/8.
 */
@Service
public class UserFriendService {

    @Autowired
    private UserfriendDao userfriendDao;
}
