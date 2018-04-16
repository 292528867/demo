package com.yk.example.entity;

import javax.persistence.*;

/**
 * Created by yk on 2018/4/16.
 */
@Entity
@Table(name = "t_user_zfb")
public class UserZfb extends BaseEntity {

    @Column(name = "zfb_account", columnDefinition = " varchar(255) comment '支付宝账号'")
        private String zfbAccount;

    @Column(name = "zfb_account_name", columnDefinition = " varchar(255) comment '支付宝账号名称'")
    private String zfbAccountName;

    /**
     *  用户
     */
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public String getZfbAccount() {
        return zfbAccount;
    }

    public void setZfbAccount(String zfbAccount) {
        this.zfbAccount = zfbAccount;
    }

    public String getZfbAccountName() {
        return zfbAccountName;
    }

    public void setZfbAccountName(String zfbAccountName) {
        this.zfbAccountName = zfbAccountName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
