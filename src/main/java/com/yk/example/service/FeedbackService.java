package com.yk.example.service;

import com.yk.example.dao.FeedbackDao;
import com.yk.example.entity.Feedback;
import com.yk.example.entity.VideoTag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yk
 * @date 2018/5/311:28
 */
@Service
public class FeedbackService {

    @Autowired
    private FeedbackDao feedbackDao;

    public void save(Feedback feedback) {
        feedbackDao.save(feedback);
    }

    public Page<Feedback> findAllPage(Feedback feedback, Pageable pageable) {
        Specification<Feedback> specification = new Specification<Feedback>() {
            @Override
            public Predicate toPredicate(Root<Feedback> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>(); //所有的断言
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return feedbackDao.findAll(specification,pageable);
    }
}