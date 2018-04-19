package com.yk.example.dao;

import com.yk.example.entity.VideoRate;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yk on 2018/4/19.
 */
public interface VideoRateDao extends CrudRepository<VideoRate, String>, JpaSpecificationExecutor<VideoRate> {

    @Query(" from VideoRate vr where vr.user.userId= ?1 and vr.videoRecord.id = ?2")
    VideoRate findByUserAndVideo(String userId, String id);
}
