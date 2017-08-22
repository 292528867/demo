package com.yk.example.dao;

import com.yk.example.entity.CrbtOrder;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Administrator on 2017/8/22.
 */
public interface OrderDao extends CrudRepository<CrbtOrder,Long> {

}
