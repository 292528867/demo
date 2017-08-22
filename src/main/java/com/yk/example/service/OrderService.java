package com.yk.example.service;

import com.yk.example.dao.OrderDao;
import com.yk.example.entity.CrbtOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/8/22.
 */
@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    public CrbtOrder save(CrbtOrder order) {
        return orderDao.save(order);
    }

    public CrbtOrder findOne(long id) {
        return orderDao.findOne(id);
    }
}
