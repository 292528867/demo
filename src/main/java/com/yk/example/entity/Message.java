package com.yk.example.entity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 聊天记录
 * Created by yk on 2018/4/25.
 */
public class Message extends BaseEntity {

    @ManyToOne()
    @JoinColumn(name = "from_user", columnDefinition = "varchar(32) not null comment '发送者用户id'")
    private User fromUser;


    @ManyToOne()
    @JoinColumn(name = "to_user", columnDefinition = "varchar(32) not null comment '接受者用户id'")
    private User toUser;

    @Column(name = "content", columnDefinition = "varchar(255) not null comment '聊天内容'")
    private String content;

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
