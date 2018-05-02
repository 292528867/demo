package com.yk.example.dao;

import com.yk.example.entity.ThirdUser;
import com.yk.example.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yk on 2018/5/2.
 */
public interface ThirduserDao extends CrudRepository<ThirdUser, String>, JpaSpecificationExecutor<ThirdUser> {

    ThirdUser findByThirdUserIdAndStatus(String thirdUserId, String s);

    List<ThirdUser> findByUser(User user);
}
