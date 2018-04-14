package com.yk.example.dao;

import com.yk.example.entity.User;
import com.yk.example.entity.VideoCollect;
import com.yk.example.entity.VideoRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yk on 2018/4/8.
 */
public interface VideoCollectDao extends CrudRepository<VideoCollect, String>, JpaSpecificationExecutor {
    @Query(" from VideoCollect vc where vc.videoRecord.id = ?1 and vc.user.userId = ?2 ")
    VideoCollect existCollect(String id, String userId);

    Page<VideoCollect> findByUser(User one, Pageable pageable);

    long countByUser(User one);
}
