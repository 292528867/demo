package com.yk.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

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

    @JsonIgnore
    @Column(name = "flag", columnDefinition = " tinyint(1) NOT NULL  comment '0 下架 1 上架' ")
    private boolean flag = true;

    @JsonIgnore
    @Transient
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

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
