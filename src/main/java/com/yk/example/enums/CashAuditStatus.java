package com.yk.example.enums;

/**
 *  提现状态
 * Created by yk on 2018/4/2.
 */
public enum  CashAuditStatus {

    // 待审批
     waitAgree,
     // 审批通过
     agree,
    // 审批拒绝
     notAgree,
    // 支付完成
    done,
    // 退款失败
     payFail

}
