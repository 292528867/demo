package com.yk.example.service;

import com.yk.example.dao.VideoTagDao;
import com.yk.example.entity.User;
import com.yk.example.entity.VideoTag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yk on 2018/4/3.
 */
@Service
public class VideoTagService {

    @Autowired
    private VideoTagDao videoTagDao;

    public List<VideoTag> findAll(VideoTag videoTag) {
        Specification<VideoTag> specification = new Specification<VideoTag>() {
            @Override
            public Predicate toPredicate(Root<VideoTag> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>(); //所有的断言
                if (StringUtils.isNotBlank(videoTag.getName())) {
                    predicates.add(criteriaBuilder.like(root.get("name").as(String.class), videoTag.getName() + "%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return videoTagDao.findAll(specification);
    }
}
