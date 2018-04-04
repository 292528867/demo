package com.yk.example.dao;

import com.yk.example.entity.VideoComment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yk on 2018/4/4.
 */
public interface VideoCommentDao extends CrudRepository<VideoComment, String>, JpaSpecificationExecutor {

    @Query(" from VideoComment vc where vc.videoRecord.id  = ?1 ")
    List<VideoComment> queryVideoId(String videoId);
}
