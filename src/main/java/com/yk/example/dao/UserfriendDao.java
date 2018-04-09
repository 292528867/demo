package com.yk.example.dao;

import com.yk.example.entity.UserFriend;
import com.yk.example.enums.FriendStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yk on 2018/4/8.
 */
public interface UserfriendDao extends CrudRepository<UserFriend, String>, JpaSpecificationExecutor {

    @Modifying
    @Query("update UserFriend  uf set uf.friendStatus =?2 where uf.id = ?1")
    void agreeApply(String id, FriendStatus agree);

    List<UserFriend> findByToUser(String userId);
}
