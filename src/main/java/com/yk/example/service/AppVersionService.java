package com.yk.example.service;

import com.yk.example.dao.AppVersionDao;
import com.yk.example.entity.AppVersion;
import com.yk.example.entity.VideoRecord;
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
 * Created by yk on 2018/4/24.
 */
@Service
public class AppVersionService {

    @Autowired
    private AppVersionDao appVersionDao;


    public Page<AppVersion> findAllPage(AppVersion appVersion, Pageable pageable) {
        Specification<AppVersion> specification = new Specification<AppVersion>() {
            @Override
            public Predicate toPredicate(Root<AppVersion> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //所有的断言
                List<Predicate> predicates = new ArrayList<>();
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return appVersionDao.findAll(specification, pageable);
    }

    public void save(AppVersion appVersion) {
        appVersionDao.save(appVersion);
    }

    public AppVersion getLastVersion() {
        return appVersionDao.getLastVersion();
    }
}
