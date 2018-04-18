package com.yk.example.dao;

import com.yk.example.entity.VideoTag;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yk on 2018/4/3.
 */
public interface VideoTagDao extends CrudRepository<VideoTag,String> ,JpaSpecificationExecutor {


    List<VideoTag> findByNameLike(String tagName);
}
