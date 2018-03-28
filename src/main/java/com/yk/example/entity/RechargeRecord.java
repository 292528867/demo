package com.yk.example.entity;

import com.yk.example.enums.PayStatus;
import com.yk.example.enums.PayType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * 充值记录表
 * Created by yk on 2018/3/28.
 */
@Entity
@Table(name = "t_recharge_record")
public class RechargeRecord {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id" ,columnDefinition = " varchar(50) comment '用户id ' ")
    private User user;

    @Column(name = "money",columnDefinition = " DECIMAL(9,2) comment '充值金额 '")
    private float money;

    @Column(name = "pay_type",columnDefinition = " varchar(2) comment '支付类型 0 支付宝 1 微信 2 银联 '")
    private PayType payType;

    @Column(name = "pay_status",columnDefinition = " varchar(2) comment '支付状态 0 待支付 1 支付成功 2 支付失败 3 取消交易 '")
    private PayStatus payStatus;

    @CreationTimestamp
    private Date createTime;

    @UpdateTimestamp
    private Date updateTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public PayStatus getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PayStatus payStatus) {
        this.payStatus = payStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
