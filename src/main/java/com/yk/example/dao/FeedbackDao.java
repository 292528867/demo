package com.yk.example.dao;

import com.yk.example.entity.Feedback;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author yk
 * @date 2018/5/311:30
 */
public interface FeedbackDao extends CrudRepository<Feedback, String>, JpaSpecificationExecutor<Feedback> {
}