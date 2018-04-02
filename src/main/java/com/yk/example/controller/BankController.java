package com.yk.example.controller;

import com.yk.example.dao.BankDao;
import com.yk.example.dao.UserCashDao;
import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.UserBankCard;
import com.yk.example.entity.UserCash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yk on 2018/4/2.
 */
@RestController
@RequestMapping("bank")
public class BankController {

    @Autowired
    private BankDao bankDao;

    @Autowired
    private UserCashDao usercashDao;

    /**
     * 绑定银行卡
     *
     * @param cardInfo
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ControllerResult add(@RequestBody UserBankCard cardInfo) {
        UserBankCard bankCard = bankDao.save(cardInfo);
        return new ControllerResult().setRet_code(0).setRet_values(bankCard).setMessage("");
    }

    /**
     * 查询银行卡列表
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "list/{userId}", method = RequestMethod.GET)
    public ControllerResult list(@PathVariable String userId) {
        List<UserBankCard> bankCards = bankDao.findByUser(userId);
        return new ControllerResult().setRet_code(0).setRet_values(bankCards).setMessage("");
    }

    /**
     * 用户提现记录
     *
     * @param userCash
     * @return
     */
    @RequestMapping(value = "cash", method = RequestMethod.GET)
    public ControllerResult cash(@RequestBody UserCash userCash) {
        UserCash cash = usercashDao.save(userCash);
        return new ControllerResult().setRet_code(0).setRet_values(cash).setMessage("");
    }
}
