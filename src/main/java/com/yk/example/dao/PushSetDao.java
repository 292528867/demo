package com.yk.example.dao;

import com.yk.example.entity.PushSet;
import com.yk.example.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yk on 2018/5/2.
 */
public interface PushSetDao extends CrudRepository<PushSet, String>, JpaSpecificationExecutor<PushSet> {
    PushSet findByUserId(String userId);

}
