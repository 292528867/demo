package com.yk.example.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 用户银行卡
 * Created by yk on 2018/4/2.
 */
@Entity
@Table(name = "t_user_bank_card")
public class UserBankCard {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" ,columnDefinition = " varchar(50) comment '用户id ' ")
    private User user;

    @JoinColumn(name = "user_name" ,columnDefinition = " varchar(50) comment '持卡人 ' ")
    private String userName;

    @JoinColumn(name = "id_number" ,columnDefinition = " varchar(50) comment '银行卡号 ' ")
    private String idNumber;

    @JoinColumn(name = "deposit_bank" ,columnDefinition = " varchar(50) comment '开户行 ' ")
    private String depositBank;

    @JoinColumn(name = "bank_name" ,columnDefinition = " varchar(50) comment '银行名称 ' ")
    private String bankName;

    @Column(name = "is_valid", columnDefinition = "varchar(2) COMMENT '0-未删除,1-已删除' ")
    private String isValid = "0";

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getDepositBank() {
        return depositBank;
    }

    public void setDepositBank(String depositBank) {
        this.depositBank = depositBank;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
}
