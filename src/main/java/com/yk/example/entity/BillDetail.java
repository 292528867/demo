package com.yk.example.entity;

import com.yk.example.enums.BillType;

import javax.persistence.*;

/**
 * Created by yk on 2018/4/12.
 */
@Entity
@Table(name = "t_bill_detail")
public class BillDetail extends BaseEntity {

    @Column(name = "bill_type", columnDefinition = " varchar(2) comment '账单类型'")
    private BillType billType;

    @Column(name = "money", columnDefinition = " DECIMAL(9,2) comment '账单类型'")
    private float money;


    /**
     * 推荐人(拿奖励的)
     */
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public BillType getBillType() {
        return billType;
    }

    public void setBillType(BillType billType) {
        this.billType = billType;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
