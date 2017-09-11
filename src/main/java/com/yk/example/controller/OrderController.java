package com.yk.example.controller;

import com.yk.example.entity.CrbtOrder;
import com.yk.example.service.OrderService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by Administrator on 2017/8/22.
 */
@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value="生成订单", notes="用户点击购买生成订单(目前还没有接入微信支付所以没有返回微信sign，返回的是订单实体)")
    @ApiImplicitParam(name = "order", value = "订单实体", required = true, dataType = "CrbtOrder")
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    @ResponseBody
    private Object insert(@RequestBody CrbtOrder order) {
        return orderService.save(order);
    }

    @ApiIgnore
    @RequestMapping(value = "findOne/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Object findOne(@PathVariable long id) {
        return orderService.findOne(id);
    }
}
