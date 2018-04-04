package com.yk.example.dao;

import com.yk.example.entity.Music;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yk on 2018/4/4.
 */
public interface MusicDao extends CrudRepository<Music,String>,JpaSpecificationExecutor {
}
