package com.yk.example.dao;

import com.yk.example.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Administrator on 2017/8/8.
 */
public interface UserDao extends CrudRepository<User, String>, JpaSpecificationExecutor {

    User save(User user);


}
