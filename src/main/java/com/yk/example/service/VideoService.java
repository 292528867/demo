package com.yk.example.service;

import com.yk.example.dao.*;
import com.yk.example.entity.*;
import com.yk.example.enums.ZanStatus;
import com.yk.example.utils.Distance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yk on 2018/4/2.
 */
@Service
public class VideoService {

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private VideoCommentDao videoCommentDao;

    @Autowired
    private VideoZanDao videoZanDao;

    @Autowired
    private UserFollowDao userFollowDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private VideoTagDao videoTagDao;


    public List<VideoRecord> nearby(double longitude, double latitude, int page, int size) {
        //先计算查询点的经纬度范围
        //地球半径千米
        double r = 6371;
        // 30 千米距离
        double dis = 30;
        double dlng = 2 * Math.asin(Math.sin(dis / (2 * r)) / Math.cos(latitude * Math.PI / 180));
        //角度转为弧度
        dlng = dlng * 180 / Math.PI;
        double dlat = dis / r;
        dlat = dlat * 180 / Math.PI;
        double minlat = latitude - dlat;
        double maxlat = latitude + dlat;
        double minlng = longitude - dlng;
        double maxlng = longitude + dlng;
        // 查询附近 30 km的用户
        List<UserInfo> userInfos = userInfoDao.findNearbyUser(minlng, maxlng, minlat, maxlat);
        List<VideoRecord> videoRecords = new ArrayList<>();
        if (userInfos != null && userInfos.size() > 0) {
            for (UserInfo userInfo : userInfos) {
                // 查询用户的最后一个视频
                VideoRecord videoRecord = videoDao.findLastVideoByUser(userInfo.getUser().getUserId());
                if(videoRecord != null){
                    videoRecord.setDistance(Distance.getDistance(latitude,longitude,userInfo.getLastLatitude(),userInfo.getLastLongitude()));
                    videoRecords.add(videoRecord);
                }
            }
        }
        return videoRecords;
    }


    public List<VideoRecord> recommend(String userId) {
        List<VideoRecord> videoRecords = new ArrayList<>();
        // 登录了 按偏好推荐
        if (StringUtils.isNotBlank(userId)) {
            User user = new User();
            user.setUserId(userId);
            videoRecords =  videoDao.findByUserNotAndFlag(user,"1" );
            Collections.shuffle(videoRecords);
        } else {
            videoRecords  = videoDao.findByFlag("1");
            Collections.shuffle(videoRecords);
        }
        return videoRecords.size() > 10 ? videoRecords.subList(0,10) : videoRecords.subList(0,videoRecords.size());
    }

    public Page<VideoRecord> recommend(String userId,Pageable pageable){
        Specification<VideoRecord> specification = new Specification<VideoRecord>() {
            @Override
            public Predicate toPredicate(Root<VideoRecord> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //所有的断言
                List<Predicate> predicates = new ArrayList<>();
                if(StringUtils.isNotBlank(userId)){
                    User user = new User();
                    user.setUserId(userId);
                    predicates.add(criteriaBuilder.notEqual(root.get("user").as(User.class),user));
                }
                predicates.add(criteriaBuilder.equal(root.get("flag").as(String.class),"1"));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return videoDao.findAll(specification, pageable);
    }

    public VideoRecord findOne(String videoId, String userId) {
        VideoRecord videoRecord = videoDao.findOne(videoId);
        if (StringUtils.isNotBlank(userId)) {
            // 判断是否点赞
            VideoZan videoZan = videoZanDao.isZan(videoId, userId);
            if (videoZan == null) {
                videoRecord.setZan(false);
            } else {
                if (videoZan.getZanStatus().equals(ZanStatus.zan)) {
                    videoRecord.setZan(true);
                } else {
                    videoRecord.setZan(false);
                }
            }
            // 判断 是否关注
            UserFollow userFollow = userFollowDao.existsFollow(userId, videoRecord.getUser().getUserId());
            if (userFollow == null) {
                videoRecord.setFollow(false);
            } else {
                videoRecord.setFollow(true);
            }
        } else {
            videoRecord.setZan(false);
            videoRecord.setFollow(false);
        }
        return videoRecord;
    }

    public Page<VideoRecord> findByUser(String userId, Pageable pageable) {
        Page<VideoRecord> videoRecords = videoDao.findByUserAndFlag(userDao.findOne(userId), pageable,"1");
        return videoRecords;
    }

    public VideoRecord save(VideoRecord videoRecord) {
        // 发布短视频
        videoRecord.setFlag("0");
        return videoDao.save(videoRecord);
    }

    public long countByUserId(String userId) {
        return videoDao.countByUserAndFlag(userDao.findOne(userId), "1");
    }

    public Page<VideoRecord> findByTag(String tagName, Pageable pageable) {
        List<VideoTag> tags = videoTagDao.findByNameLike(tagName);
        Page<VideoRecord> videoRecords = null;
        if (tags != null && tags.size() > 0) {
            videoRecords = videoDao.findByTagInAndFlag(tags, pageable,"1" );
        }
        return videoRecords;
    }

    public boolean existVideo(VideoRecord videoRecord) {
        VideoRecord record = videoDao.findOne(videoRecord.getId());
        if(record != null && record.getId().equals(videoRecord.getId())){
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteVideo(VideoRecord videoRecord) {
        videoDao.updateFlag(videoRecord.getId(),"3");
    }

    public Page<VideoRecord> findAllPage(VideoRecord videoRecord, Pageable pageable) {
        Specification<VideoRecord> specification = new Specification<VideoRecord>() {
            @Override
            public Predicate toPredicate(Root<VideoRecord> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //所有的断言
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNoneBlank(videoRecord.getVideoUrl())) {
                    predicates.add(criteriaBuilder.like(root.get("videoUrl").as(String.class), videoRecord.getVideoUrl() + "%"));
                }
                predicates.add(criteriaBuilder.equal(root.get("flag").as(String.class),"0"));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return videoDao.findAll(specification, pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    public void agreeOrShieldVideo(String id,String flag) {
        videoDao.updateFlag(id,flag);
    }
}
