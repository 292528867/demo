package com.yk.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.File;

/**
 * apk版本管理表
 * Created by yk on 2018/4/24.
 */
@Entity
@Table(name = "t_app_version")
@JsonIgnoreProperties({"file"})
public class AppVersion extends BaseEntity {

    @Column(name = "version", columnDefinition = "varchar(3) comment '版本号'")
    private String version;

    @Column(name = "apk_name", columnDefinition = "varchar(255) comment 'apk版本名称'")
    private String apkName;

    @Column(name = "update_content", columnDefinition = "varchar(255) comment '版本更新内容'")
    private String updateContent;

    @Column(name = "down_url", columnDefinition = "varchar(255) comment '服务器现在地址'")
    private String downUrl;


    @Transient
    private MultipartFile file;
    
    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }
}
