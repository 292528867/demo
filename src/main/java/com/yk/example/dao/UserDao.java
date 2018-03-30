package com.yk.example.dao;

import com.yk.example.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Administrator on 2017/8/8.
 */
public interface UserDao extends CrudRepository<User, String>, JpaSpecificationExecutor {


    @Modifying
    @Query(" update User u set u.isValid = ?2 where u.userId = ?1")
    int updateIsValidByUserId(String userId, String isValid);

    User findByDirectRecommendUser(String directRecommendUserPhone);

    User findByPhone(String phone);
}
