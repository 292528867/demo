package com.yk.example.service;

import com.yk.example.dao.*;
import com.yk.example.entity.*;
import com.yk.example.enums.ViewAuth;
import com.yk.example.enums.ZanStatus;
import com.yk.example.utils.Distance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Value("${video.url}")
    private String videoUrl;

    @Autowired
    private StringRedisTemplate redisTemplate;


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
                List<VideoRecord> videoRecordList = videoDao.findLastVideoByUser(userInfo.getUser().getUserId());
                if (videoRecordList != null && videoRecordList.size() > 0) {
                    VideoRecord videoRecord = videoRecordList.get(0);
                    videoRecord.setDistance(Distance.getDistance(latitude, longitude, userInfo.getLastLatitude(), userInfo.getLastLongitude()));
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
            videoRecords = videoDao.findByUserNotAndFlag(user, "1");
            Collections.shuffle(videoRecords);
        } else {
            videoRecords = videoDao.findByFlag("1");
            Collections.shuffle(videoRecords);
        }
        return videoRecords.size() > 10 ? videoRecords.subList(0, 10) : videoRecords.subList(0, videoRecords.size());
    }

    public Page<VideoRecord> recommend(String userId, Pageable pageable) {
        Specification<VideoRecord> specification = new Specification<VideoRecord>() {
            @Override
            public Predicate toPredicate(Root<VideoRecord> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //所有的断言
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNotBlank(userId)) {
                    User user = new User();
                    user.setUserId(userId);
                    predicates.add(criteriaBuilder.notEqual(root.get("user").as(User.class), user));
                }
                predicates.add(criteriaBuilder.equal(root.get("flag").as(String.class), "1"));
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createTime")));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Page<VideoRecord> recordPage = videoDao.findAll(specification, pageable);
        // 判断是否关注和是否点赞
        List<VideoRecord> records = recordPage.getContent();
        if (StringUtils.isNoneBlank(userId)) {
            List<String> followUser = userFollowDao.findByUserId(userId, true);
            User user = new User();
            user.setUserId(userId);
            List<VideoZan> videoZanList = videoZanDao.findByUser(user);

            for (VideoRecord videoRecord : records) {
                boolean followFlag = false;
                boolean zanFlag = false;
                if (followUser != null && followUser.size() > 0) {
                    for (String followUserId : followUser) {
                        if (videoRecord.getUser().getUserId().equals(followUserId)) {
                            videoRecord.setFollow(true);
                            followFlag = true;
                        }
                    }
                }
                if (!followFlag) {
                    videoRecord.setFollow(false);
                }
                if (videoZanList != null && videoZanList.size() > 0) {
                    for (VideoZan videoZan : videoZanList) {
                        if (videoZan.getVideoRecord().getId().equals(videoRecord.getId())) {
                            videoRecord.setZan(true);
                            zanFlag = true;
                        }
                    }
                }
                if (!zanFlag) {
                    videoRecord.setZan(false);
                }
            }
        }
        return new PageImpl(records, pageable, recordPage.getTotalElements());
    }

    public VideoRecord findOne(String videoId, String userId) {
        VideoRecord videoRecord = videoDao.findOne(videoId);
        if (StringUtils.isNotBlank(userId)) {
            // 判断是否点赞
//            VideoZan videoZan = videoZanDao.isZan(videoId, userId);
            User user = new User();
            user.setUserId(userId);
            VideoZan videoZan = videoZanDao.findByUserAndVideoRecord(user, videoRecord);
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
        Page<VideoRecord> videoRecords = videoDao.findByUserAndFlag(userDao.findOne(userId), pageable, "1");
        return videoRecords;
    }

    public VideoRecord save(VideoRecord videoRecord) {
        // 发布短视频
        String flag = redisTemplate.opsForValue().get("video_first_check_or_publish");
        if(StringUtils.isNoneBlank(flag)){
             videoRecord.setFlag(flag);
        }else {
            videoRecord.setFlag("1");
        }
        return videoDao.save(videoRecord);
    }

    public long countByUserId(String userId) {
        return videoDao.countByUserAndFlag(userDao.findOne(userId), "1");
    }

    public Page<VideoRecord> findByTag(String tagName, Pageable pageable) {
        List<VideoTag> tags = videoTagDao.findByNameLike(tagName);
        Page<VideoRecord> videoRecords = null;
        if (tags != null && tags.size() > 0) {
            videoRecords = videoDao.findByTagInAndFlag(tags, pageable, "1");
        }
        return videoRecords;
    }

    public boolean existVideo(VideoRecord videoRecord) {
        VideoRecord record = videoDao.findOne(videoRecord.getId());
        if (record != null && record.getId().equals(videoRecord.getId())) {
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteVideo(VideoRecord videoRecord) {
        videoDao.updateFlag(videoRecord.getId(), "3");
    }

    public Page<VideoRecord> findAllPage(VideoRecord videoRecord, Pageable pageable) {
        Specification<VideoRecord> specification = new Specification<VideoRecord>() {
            @Override
            public Predicate toPredicate(Root<VideoRecord> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //所有的断言
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNoneBlank(videoRecord.getVideoUrl())) {
                    predicates.add(criteriaBuilder.like(root.get("videoUrl").as(String.class), "%" + videoRecord.getVideoUrl() + "%"));
                }
                if (StringUtils.isNoneBlank(videoRecord.getTitle())) {
                    predicates.add(criteriaBuilder.like(root.get("title").as(String.class), "%" + videoRecord.getTitle() + "%"));
                }
                if (videoRecord.getUser() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("user").as(User.class), videoRecord.getUser()));
//                    SetJoin<VideoRecord, User> userJoin = root.getModel().getSingularAttribute("user", User.class);
                }
//                predicates.add(criteriaBuilder.equal(root.get("flag").as(String.class), "0"));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return videoDao.findAll(specification, pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    public void agreeOrShieldVideo(String id, String flag) {
        videoDao.updateFlag(id, flag);
    }

    public void importVideo(List<List<String>> list) {
        List<VideoRecord> videoRecords = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                     /*   System.out.print("第" + (i) + "行");
                        List<String> cellList = list.get(i);
                        for (int j = 0; j < cellList.size(); j++) {
                            // System.out.print("    第" + (j + 1) + "列值：");
                            System.out.print("    " + cellList.get(j));
                        }
                        System.out.println();*/
                // 从第二行开始导入
                if (i > 1) {
                    List<String> cellList = list.get(i);
                    VideoRecord videoRecord = new VideoRecord();
                    videoRecord.setVideoUrl(cellList.get(0));
                    videoRecord.setTitle(cellList.get(1));
                    videoRecord.setVideoImgUrl(videoUrl + "/" + cellList.get(0) + ".jpg");
                    videoRecord.setMusicName(cellList.get(3));
                    videoRecord.setUser(userDao.findByPhone(cellList.get(4)));
                    videoRecord.setLatitude("121.508269");
                    videoRecord.setLongitude("31.246255");
                    videoRecord.setTag(videoTagDao.findByName(cellList.get(7)));
                    videoRecord.setFlag("1");
                    videoRecord.setViewAuth(ViewAuth.all);
                    videoRecords.add(videoRecord);
                }
            }
        }
        videoDao.save(videoRecords);
    }

    public void updateVideoImage() {
        List<VideoRecord> video = videoDao.findVideo();
        for (VideoRecord record : video) {
            record.setVideoImgUrl("http://www.miaoou.cc/video/" + record.getVideoUrl() + ".jpg");
            videoDao.save(record);
        }
    }
}
