package com.yk.example.dao;

import com.yk.example.entity.User;
import com.yk.example.entity.VideoRecord;
import com.yk.example.entity.VideoTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yk on 2018/4/2.
 */
public interface VideoDao extends CrudRepository<VideoRecord, String>, JpaSpecificationExecutor {

    @Query(" from  VideoRecord vr where vr.user.userId = ?1 and vr.createTime = " +
            " ( select max(v.createTime) from VideoRecord  v where v.user.userId = ?1 )")
    VideoRecord findLastVideoByUser(String userId);

    @Modifying
    @Query(" update VideoRecord  vr set vr.commentNum = ?1 where vr.id = ?2 ")
    void updateCommentNum(int num, String videoId);

    @Modifying
    @Query(" update VideoRecord  vr set vr.zanNum = ?1 where vr.id = ?2 ")
    void updateZanNum(int zanNum, String videoId);

    @Query(" from VideoRecord vr where vr.user.id = ?1")
    List<VideoRecord> findByUserId(String userId);

    Page<VideoRecord> findByUser(User user, Pageable pageable);

    long countByUser(User user);

    Page<VideoRecord> findByTag(VideoTag one, Pageable pageable);

    Page<VideoRecord> findByTagIn(List<VideoTag> tags, Pageable pageable);
}
