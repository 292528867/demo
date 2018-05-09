package com.yk.example.service;

import com.yk.example.dao.MusicDao;
import com.yk.example.entity.Music;
import com.yk.example.entity.User;
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
 * Created by yk on 2018/4/4.
 */
@Service
public class MusicService {

    @Autowired
    private MusicDao musicDao;

    public List<Music> findAll() {
        List<Music> music = new ArrayList<>();
        Iterable<Music> all = musicDao.findAll();
        all.forEach(m -> {
            music.add(m);
        });
        return music;
    }

    public Page<Music> findAllPage(Music music, Pageable pageable) {
        Specification<Music> specification = new Specification<Music>() {
            @Override
            public Predicate toPredicate(Root<Music> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //所有的断言
                List<Predicate> predicates = new ArrayList<>();
                if(StringUtils.isNoneBlank(music.getMusicName())){
                    predicates.add(criteriaBuilder.like(root.get("musicName").as(String.class), "%" + music.getMusicName() + "%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return musicDao.findAll(specification, pageable);
    }

    public void save(Music music) {
        musicDao.save(music);
    }
}
