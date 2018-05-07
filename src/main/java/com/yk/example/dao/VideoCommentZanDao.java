package com.yk.example.dao;

import com.yk.example.entity.User;
import com.yk.example.entity.VideoComment;
import com.yk.example.entity.VideoCommentZan;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yk on 2018/4/8.
 */
public interface VideoCommentZanDao extends CrudRepository<VideoCommentZan, String>, JpaSpecificationExecutor {
    List<VideoCommentZan> findByUser(User user);

    void deleteByUserAndComment(User user, VideoComment comment);

    VideoCommentZan findByUserAndComment(User user, VideoComment comment);
}
