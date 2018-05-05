package com.yk.example.controller;

import com.yk.example.dto.ControllerResult;
import com.yk.example.entity.AppVersion;
import com.yk.example.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * Created by yk on 2018/4/24.
 */
@RestController
public class VersionController {

    @Autowired
    private AppVersionService appVersionService;

    @RequestMapping(value = "checkVersion/{version}", method = RequestMethod.GET)
    public ControllerResult checkVersion(@PathVariable String version, int apkVersion) {
        AppVersion lastVersion = appVersionService.getLastVersion();
        if (lastVersion != null) {
            // 判断版本大小
            if (lastVersion.getVersion() > apkVersion) {
                return new ControllerResult().setRet_code(0).setRet_values(lastVersion).setMessage("");
            }
        }
        return new ControllerResult().setRet_code(1).setRet_values(Collections.emptyMap()).setMessage("不需要更新");
    }
}
