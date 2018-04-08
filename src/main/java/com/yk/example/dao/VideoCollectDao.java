package com.yk.example.dao;

import com.yk.example.entity.VideoCollect;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yk on 2018/4/8.
 */
public interface VideoCollectDao extends CrudRepository<VideoCollect, String>, JpaSpecificationExecutor {
    @Query(" from VideoCollect vc where vc.videoRecord.id = ?1 and vc.user.userId = ?2 ")
    VideoCollect existCollect(String id, String userId);
}
