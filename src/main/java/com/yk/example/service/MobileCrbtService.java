package com.yk.example.service;

import com.yk.example.dao.MobileCrbtDao;
import com.yk.example.entity.MobileCrbt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/8/22.
 */
@Service
public class MobileCrbtService {

    @Autowired
    private MobileCrbtDao mobileCrbtDao;

    public Object findAll() {
        return mobileCrbtDao.findAll();
    }

    public void insert(MobileCrbt mobileCrbt) {
        mobileCrbtDao.save(mobileCrbt);
    }

    public MobileCrbt findById(long id) {
        return mobileCrbtDao.findOne(id);
    }
}
