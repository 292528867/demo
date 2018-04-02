package com.yk.example.dao;

import com.yk.example.entity.UserCash;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yk on 2018/4/2.
 */
public interface UserCashDao extends CrudRepository<UserCash ,String> ,JpaSpecificationExecutor {
}
