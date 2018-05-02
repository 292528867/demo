package com.yk.example.service;

import com.yk.example.dao.PushSetDao;
import com.yk.example.entity.PushSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yk on 2018/5/2.
 */
@Service
public class PushSetService {

    @Autowired
    private PushSetDao pushSetDao;

    public PushSet save(PushSet pushSet) {
        return pushSetDao.save(pushSet);
    }

    public PushSet findPushSet(String userId) {
        return pushSetDao.findByUserId(userId);
    }
}
