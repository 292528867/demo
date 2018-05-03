package com.yk.example.entity;

import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name = "t_feedback")
public class Feedback extends  BaseEntity{

    @Column(name = "content",columnDefinition = " varchar(255) not null comment '反馈内容'")
    private String content;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}