package com.yk.example.manage;

import com.yk.example.entity.AppVersion;
import com.yk.example.service.AppVersionService;
import com.yk.example.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by yk on 2018/4/24.
 */
@Controller
public class AppVersionController {

    @Autowired
    private AppVersionService appVersionService;

    @Value("${apk.path}")
    private String apkPath;

    @Value("${apk.url}")
    private String apkUrl;

    /**
     * @param model
     * @param pageCurrent 当前页
     * @param pageSize    每页个数
     * @param pageCount   总共多少页
     * @return
     */
    @RequestMapping(value = "/admin/versionList_{pageCurrent}_{pageSize}_{pageCount}", method = RequestMethod.GET)
    public String index(Model model, AppVersion appVersion, @PathVariable Integer pageCurrent, @PathVariable Integer pageSize, @PathVariable Integer pageCount) {
        if (pageCurrent == 0) {
            pageCurrent = 1;
        }
        if (pageSize == 0) {
            pageSize = 10;
        }
        Page<AppVersion> appVersionPage = appVersionService.findAllPage(appVersion, new PageRequest(pageCurrent - 1, pageSize));
        model.addAttribute("versionList", appVersionPage.getContent());
        String pageHTML = PageUtils.getPageContent("versionList_{pageCurrent}_{pageSize}_{pageCount}", pageCurrent, pageSize, appVersionPage.getTotalPages());
        model.addAttribute("pageHTML", pageHTML);
        model.addAttribute("appVersion", appVersion);
        return "version/versionManage";
    }

    @RequestMapping(value = "/admin/versionEdit", method = RequestMethod.GET)
    public String versionEdit(Model model, AppVersion appVersion) {
        return "version/versionEdit";
    }

    /**
     * 上传apk包
     *
     * @param model
     * @param appVersion
     * @return
     */
    @RequestMapping(value = "/admin/versionSave", method = RequestMethod.POST)
    public String versionSave(Model model, AppVersion appVersion) {
        MultipartFile file = appVersion.getFile();
        if (file != null) {
            String fileName = file.getOriginalFilename();
            try {
                FileOutputStream outputStream = new FileOutputStream(apkPath + "/" + fileName);
                outputStream.write(file.getBytes());
                outputStream.flush();
                outputStream.close();
                appVersion.setDownUrl(apkUrl + fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        appVersionService.save(appVersion);
        return "redirect:versionList_0_0_0";
    }
}
