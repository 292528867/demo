package com.yk.example.controller;

import com.yk.example.dao.BankDao;
import com.yk.example.dao.UserCashDao;
import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.UserBankCard;
import com.yk.example.entity.UserCash;
import com.yk.example.entity.UserZfb;
import com.yk.example.service.UserZfbService;
import io.swagger.annotations.ApiOperation;
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

    @Autowired
    private UserZfbService userZfbService;

    /**
     * 绑定银行卡
     *
     * @param cardInfo
     * @return
     */
    @ApiOperation(value="绑定银行卡")
    @RequestMapping(value = "add/{version}", method = RequestMethod.POST)
    public ControllerResult add(@RequestBody UserBankCard cardInfo,@PathVariable String version) {
        UserBankCard bankCard = bankDao.save(cardInfo);
        return new ControllerResult().setRet_code(0).setRet_values(bankCard).setMessage("");
    }

    /**
     * 查询银行卡列表
     *
     * @param userId
     * @return
     */
    @ApiOperation(value="查询银行卡列表")
    @RequestMapping(value = "list/{userId}/{version}", method = RequestMethod.GET)
    public ControllerResult list(@PathVariable String userId,@PathVariable String version) {
        List<UserBankCard> bankCards = bankDao.findByUser(userId);
        return new ControllerResult().setRet_code(0).setRet_values(bankCards).setMessage("");
    }

    /**
     * 绑定支付宝
     * @param userZfb
     * @param version
     * @return
     */
    @ApiOperation(value="绑定支付宝")
    @RequestMapping(value = "addZfb/{version}", method = RequestMethod.POST)
    public ControllerResult addZfb(@RequestBody UserZfb userZfb, @PathVariable String version) {
        userZfb = userZfbService.addZfb(userZfb);
        return new ControllerResult().setRet_code(0).setRet_values(userZfb).setMessage("");
    }

    /**
     *  支付宝列表
     * @param version
     * @param userId
     * @return
     */
    @ApiOperation(value="支付宝列表")
    @RequestMapping(value = "zfbList/{userId}/{version}", method = RequestMethod.GET)
    public ControllerResult zfbList(@PathVariable String version,@PathVariable String userId){
        List<UserZfb> userZfbs = userZfbService.findByUser(userId);
        return new ControllerResult().setRet_code(0).setRet_values(userZfbs).setMessage("");
    }

    /**
     * 用户提现
     *
     * @param userCash
     * @return
     */
    @ApiOperation(value="用户提现")
    @RequestMapping(value = "cash/{version}", method = RequestMethod.POST)
    public ControllerResult cash(@RequestBody UserCash userCash,@PathVariable String version) {
        UserCash cash = usercashDao.save(userCash);
        return new ControllerResult().setRet_code(0).setRet_values(cash).setMessage("");
    }
}
