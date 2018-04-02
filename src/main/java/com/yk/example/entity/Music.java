package com.yk.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by yk on 2018/4/2.
 */
@Entity
@Table(name = "t_music")
public class Music extends BaseEntity {

    @Column(name = "music_name",columnDefinition = "varchar(50) comment '音乐名称'")
    private String musicName;

    @Column(name = "music_url", columnDefinition = " varchar(255) comment '音乐url' ")
    private String musicUrl;


    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }
}
