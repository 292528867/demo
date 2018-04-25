package com.yk.example.dao;

import com.yk.example.entity.Message;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yk on 2018/4/25.
 */
public interface MessageDao extends CrudRepository<Message, String>, JpaSpecificationExecutor<Message> {
}
