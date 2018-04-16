package com.yk.example.entity;

import com.yk.example.enums.CashAuditStatus;

import javax.persistence.*;

/**
 * 用户提现记录
 * Created by yk on 2018/4/2.
 */
@Entity
@Table(name = "t_user_cash")
public class UserCash extends BaseEntity {

    @Column(name = "money", columnDefinition = " varchar(20) comment '提现金额'")
    private String money;

    @Column(name = "description", columnDefinition = " varchar(255) comment '描述'")
    private String description;

    @Column(name = "audit_status", columnDefinition = " varchar(2) comment '0审核中，1已同意，2拒绝，3已支付，4支付失败'")
    private CashAuditStatus auditStatus;

    @OneToOne
    @JoinColumn(name = "bank_id", columnDefinition = " varchar(50) comment '用户银行卡id ' ")
    private UserBankCard userBankCard;

    @Column(name = "zfb_account", columnDefinition = " varchar(255) comment '支付宝账号'")
    private String zfbAccount;

    @Column(name = "zfb_account_name", columnDefinition = " varchar(255) comment '支付宝账号名称'")
    private String zfbAccountName;

    @ManyToOne()
    @JoinColumn(name = "user_id" ,columnDefinition = " varchar(50) comment '用户id ' ")
    private User user;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CashAuditStatus getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(CashAuditStatus auditStatus) {
        this.auditStatus = auditStatus;
    }

    public UserBankCard getUserBankCard() {
        return userBankCard;
    }

    public void setUserBankCard(UserBankCard userBankCard) {
        this.userBankCard = userBankCard;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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
}
