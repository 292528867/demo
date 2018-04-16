package com.yk.example.dao;

import com.yk.example.entity.User;
import com.yk.example.enums.PlatForm;
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

    @Modifying
    @Query(" update User u set u.password = ?1 where u.phone = ?2 ")
    int updatePassword(String password, String phone);

    @Modifying
    @Query(" update User u set u.deviceToken = ?2 ,u.platform = ?3 where u.userId = ?1")
    void updateDeviceToken(String userId, String deviceToken, PlatForm platform);

    User findByInviteCode(String inviteCode);

    @Modifying
    @Query("update User u set u.headImgUrl = ?2 where u.userId = ?1")
    void updateHeadImgUrl(String userId, String headImageUrl);

    @Query(" select count(1) from User u where u.directRecommendUser = ?1 or u.spaceRecommendUser = ?1 ")
    int countInviteUser(String userId);
}
