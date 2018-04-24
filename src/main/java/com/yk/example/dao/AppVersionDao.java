package com.yk.example.dao;

import com.yk.example.entity.AppVersion;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yk on 2018/4/24.
 */
public interface AppVersionDao extends CrudRepository<AppVersion, String>, JpaSpecificationExecutor<AppVersion> {

    @Query(" from  AppVersion av where av.createTime = ( select max(v.createTime) from AppVersion  v  )")
    AppVersion getLastVersion();

}
