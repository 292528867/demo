package com.yk.example.dao;

import com.yk.example.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yk on 2018/4/4.
 */
public interface UserFollowDao extends CrudRepository<UserFollow,String>,JpaSpecificationExecutor {

    @Query(" from UserFollow u where u.userId = ?1 and u.followId = ?2 ")
    UserFollow existsFollow(String userId, String followId);
}
