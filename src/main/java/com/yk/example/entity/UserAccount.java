package com.yk.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户账号
 * Created by yk on 2018/4/2.
 */
public class UserAccount extends BaseEntity {

    @Column(name = "balance", columnDefinition = " decimal(10,2) comment '账户余额'")
    private String balance;

    @Column(name = "total_income", columnDefinition = " decimal(10,2) comment '总收入'")
    private String totalIncome;

    @Column(name = "total_cost", columnDefinition = " decimal(10,2) comment '总支出'")
    private String totalCost;

    @Column(name = "miao_peas", columnDefinition = " decimal(10,2) comment '秒豆'")
    private String miaoPeas;

    @Column(name = "order_income", columnDefinition = " decimal(10,2) comment '订单收入'")
    private String orderIncome;

    @Column(name = "new_income", columnDefinition = " decimal(10,2) comment '纳新收入'")
    private String newIncome;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getMiaoPeas() {
        return miaoPeas;
    }

    public void setMiaoPeas(String miaoPeas) {
        this.miaoPeas = miaoPeas;
    }

    public String getOrderIncome() {
        return orderIncome;
    }

    public void setOrderIncome(String orderIncome) {
        this.orderIncome = orderIncome;
    }

    public String getNewIncome() {
        return newIncome;
    }

    public void setNewIncome(String newIncome) {
        this.newIncome = newIncome;
    }
}
