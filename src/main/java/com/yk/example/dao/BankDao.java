package com.yk.example.dao;

import com.yk.example.entity.UserBankCard;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yk on 2018/4/2.
 */
public interface BankDao extends CrudRepository<UserBankCard, String>, JpaSpecificationExecutor {

    @Query("from UserBankCard  u where u.user.userId = ?1")
    List<UserBankCard> findByUser(String userId);
}
