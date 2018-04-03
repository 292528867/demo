package com.yk.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 视频标签
 * Created by yk on 2018/4/3.
 */
@Entity
@Table(name = "t_video_tag")
public class VideoTag extends BaseEntity {

    @Column(name = "name", columnDefinition = "varchar(50) comment '视频标签名称'")
    private String name;


    @Column(name = "description", columnDefinition = "varchar(50) comment '视频标签描述'")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
