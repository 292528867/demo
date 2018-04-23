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
            " ( select max(v.createTime) from VideoRecord  v where v.user.userId = ?1 and v.flag = '0' )")
    VideoRecord findLastVideoByUser(String userId);

    @Modifying
    @Query(" update VideoRecord  vr set vr.commentNum = ?1 where vr.id = ?2 ")
    void updateCommentNum(int num, String videoId);

    @Modifying
    @Query(" update VideoRecord  vr set vr.zanNum = ?1 where vr.id = ?2 ")
    void updateZanNum(int zanNum, String videoId);

    @Query(" from VideoRecord vr where vr.user.id = ?1 and vr.flag = '0' ")
    List<VideoRecord> findByUserId(String userId);

    Page<VideoRecord> findByUserAndFlag(User user, Pageable pageable, String flag);

    long countByUserAndFlag(User user, String flag);

    Page<VideoRecord> findByTag(VideoTag one, Pageable pageable);

    Page<VideoRecord> findByTagInAndFlag(List<VideoTag> tags, Pageable pageable, String flag);
    
    List<VideoRecord> findTop10ByUserNotAndFlag(User user, String flag);

    List<VideoRecord> findTop10ByAndFlag(String flag);

    List<VideoRecord> findByUserAndFlag(User user, String flag);

    @Query(" update VideoRecord vr set vr.flag =?2 where id =?1")
    @Modifying
    void updateFlag(String id,String flag);
}
