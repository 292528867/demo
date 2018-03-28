package com.yk.example.dao;

import com.yk.example.entity.RechargeRecord;
import com.yk.example.enums.PayStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yk on 2018/3/28.
 */
public interface RechargeDao  extends CrudRepository<RechargeRecord, String>, JpaSpecificationExecutor {


    @Modifying
    @Query(" update RechargeRecord  r set r.payStatus = ?2 where r.id = ?1 ")
    int updatePayStatus(String out_trade_no, PayStatus success);
}
