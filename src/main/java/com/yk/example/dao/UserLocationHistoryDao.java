package com.yk.example.dao;

import com.yk.example.entity.UserLocationHistory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yk on 2018/4/3.
 */
public interface UserLocationHistoryDao extends CrudRepository<UserLocationHistory,String> ,JpaSpecificationExecutor{
}
