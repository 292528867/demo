package com.yk.example.service;

import com.yk.example.dao.ZanRecordHistoryDao;
import com.yk.example.entity.ZanRecordHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yk on 2018/4/9.
 */
@Service
public class ZanRecordHistoryService {

    @Autowired
    private ZanRecordHistoryDao zanRecordHistoryDao;

    public List<ZanRecordHistory> zanList(String userId) {
        return zanRecordHistoryDao.findByToUserId(userId);
    }
}

