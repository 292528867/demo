package com.yk.example.dao;

import com.yk.example.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yk on 2018/4/2.
 */
public interface UserInfoDao extends CrudRepository<UserInfo, String>, JpaSpecificationExecutor {

    @Query(" from UserInfo  u where u.lastLongitude >= ?1 and u.lastLongitude < ?2 and u.lastLatitude >= ?3 and u.lastLatitude < ?4 ")
    List<UserInfo> findNearbyUser(double minlng, double maxlng, double minlat, double maxlat);

    @Modifying
    @Query("update UserInfo u set u.lastLongitude = ?1 ,u.lastLatitude = ?2 where u.user.userId = ?3 ")
    int updateUserLocation(double longitude, double latitude, String userId);
}
