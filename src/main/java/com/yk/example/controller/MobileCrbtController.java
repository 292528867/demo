package com.yk.example.controller;

import com.yk.example.entity.MobileCrbt;
import com.yk.example.service.MobileCrbtService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by Administrator on 2017/8/22.
 */
@ApiIgnore
@Controller
@RequestMapping("crbt")
public class MobileCrbtController {

    @Autowired
    private MobileCrbtService mobileCrbtService;

    @ApiOperation(value="获取彩铃列表", notes="")
    @RequestMapping(value = "findAll",method = RequestMethod.GET)
    @ResponseBody
    private Object findAll() {
        return mobileCrbtService.findAll();
    }

    @ApiIgnore
    @RequestMapping(value = "insert",method = RequestMethod.POST)
    @ResponseBody
    private Object insert(@RequestBody MobileCrbt mobileCrbt) {
        mobileCrbtService.insert(mobileCrbt);
        return "添加成功";
    }

    @ApiOperation(value = "获取彩铃详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "彩铃id", required = true, dataType = "Long")
    })
    @RequestMapping(value = "findDetail/{id}",method = RequestMethod.GET)
    @ResponseBody
    private Object findDetail(@PathVariable  long id) {
        MobileCrbt crbt = mobileCrbtService.findById(id);
        return crbt;
    }
}
