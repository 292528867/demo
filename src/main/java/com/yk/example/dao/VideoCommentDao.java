package com.yk.example.dao;

import com.yk.example.entity.VideoComment;
import com.yk.example.entity.VideoRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yk on 2018/4/4.
 */
public interface VideoCommentDao extends CrudRepository<VideoComment, String>, JpaSpecificationExecutor {

    Page<VideoComment> findByVideoRecord(VideoRecord record, Pageable pageable);

    @Modifying
    @Query(" update VideoComment vc set vc.zanNum = ?1 where vc.id = ?2 ")
    int updateZanNum(int zanNum, String commentId);

    Page<VideoComment> findByVideoRecordIn(List<VideoRecord> videoRecords, Pageable pageable);
}
