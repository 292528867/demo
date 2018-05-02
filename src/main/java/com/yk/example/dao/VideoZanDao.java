package com.yk.example.dao;

import com.yk.example.entity.User;
import com.yk.example.entity.VideoRecord;
import com.yk.example.entity.VideoZan;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yk on 2018/4/4.
 */
public interface VideoZanDao extends CrudRepository<VideoZan, String>, JpaSpecificationExecutor {

    @Query(" from VideoZan v  where not exists (select 1 from VideoZan where  v.videoRecord.id= ?1  and v.user.id = ?2 and createTime > v.createTime)")
    VideoZan isZan(String videoId, String userId);

    VideoZan findByUserAndVideoRecord(User user, VideoRecord videoRecord);

    void deleteByUserAndVideoRecord(User user, VideoRecord videoRecord);

}
