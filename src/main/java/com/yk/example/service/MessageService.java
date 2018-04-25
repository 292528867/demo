package com.yk.example.service;

import com.yk.example.dao.MessageDao;
import com.yk.example.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yk on 2018/4/25.
 */
@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;

    public Message sendMessage(Message message) {
        return messageDao.save(message);
    }
}
