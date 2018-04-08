package com.yk.example.dao;

import com.yk.example.entity.UserFriend;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yk on 2018/4/8.
 */
public interface UserfriendDao extends CrudRepository<UserFriend, String>, JpaSpecificationExecutor {

}
