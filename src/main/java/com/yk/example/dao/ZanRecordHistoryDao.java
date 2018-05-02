package com.yk.example.dao;

import com.yk.example.entity.ZanRecordHistory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yk on 2018/4/9.
 */
public interface ZanRecordHistoryDao extends CrudRepository<ZanRecordHistory, String>, JpaSpecificationExecutor {
    List<ZanRecordHistory> findByFromUserId(String userId);

    List<ZanRecordHistory> findByToUserId(String userId);
}
