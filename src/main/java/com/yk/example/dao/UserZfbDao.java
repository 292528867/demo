package com.yk.example.dao;

import com.yk.example.entity.User;
import com.yk.example.entity.UserZfb;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yk on 2018/4/16.
 */
public interface UserZfbDao extends CrudRepository<UserZfb, String>, JpaSpecificationExecutor<UserZfb> {
    List<UserZfb> findByUser(User user);
}
