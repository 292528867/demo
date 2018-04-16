package com.yk.example.controller;

import com.yk.example.dto.ControllerResult;
import com.yk.example.dto.InviteRecordDto;
import com.yk.example.dto.UserInfoDto;
import com.yk.example.entity.BillDetail;
import com.yk.example.entity.UserInfo;
import com.yk.example.service.BillDetailService;
import com.yk.example.service.UserInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by yk on 2018/4/12.
 */
@RestController
@RequestMapping("bill")
public class BillController {


    @Autowired
    private BillDetailService billDetailService;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 我的账户
     *
     * @param userId
     * @param version
     * @return
     */
    @ApiOperation("我的账户")
    @RequestMapping(value = "account/{userId}/{version}", method = RequestMethod.GET)
    public ControllerResult account(@PathVariable String userId, @PathVariable String version) {
        UserInfoDto userInfoDto = userInfoService.personInfo(userId);
        return new ControllerResult().setRet_code(0).setRet_values(userInfoDto).setMessage("");
    }

    /**
     * 账单明细
     *
     * @param userId
     * @param version
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("账单明细")
    @RequestMapping(value = "detail/{userId}/{version}", method = RequestMethod.GET)
    public ControllerResult billDetail(@PathVariable String userId, @PathVariable String version, int page, int size) {
        Page<BillDetail> billDetailPage = billDetailService.findByUserId(userId, new PageRequest(page, size));
        return new ControllerResult().setRet_code(0).setRet_values(billDetailPage).setMessage("");
    }

    /**
     * 查询邀请记录
     *
     * @param userId
     * @param version
     * @return
     */
    @ApiOperation("查询邀请记录")
    @RequestMapping(value = "inviteRecord/{userId}/{version}", method = RequestMethod.GET)
    public ControllerResult inviteRecord(@PathVariable String userId, @PathVariable String version) {
       Map<String,Object> resultMap =  billDetailService.inviteRecord(userId);
        return new ControllerResult().setRet_code(0).setRet_values(resultMap).setMessage("");
    }
}
