package com.yk.example.dao;

import com.yk.example.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yk on 2018/4/4.
 */
public interface UserFollowDao extends CrudRepository<UserFollow, String>, JpaSpecificationExecutor {

    @Query(" from UserFollow u where u.userId = ?1 and u.followId = ?2 ")
    UserFollow existsFollow(String userId, String followId);

    List<UserFollow> findByFollowIdAndStatus(String userId, boolean status);

    @Query(" select distinct u.followId from UserFollow u where u.userId = ?1 and u.status = ?2 ")
    List<String> findByUserId(String userId, boolean status);


    void deleteByUserIdAndFollowId(String userId, String followId);


    UserFollow findByUserIdAndFollowId(String userId, String followId);
}
