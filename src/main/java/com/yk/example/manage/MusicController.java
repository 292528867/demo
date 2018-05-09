package com.yk.example.manage;

import com.yk.example.entity.Music;
import com.yk.example.service.MusicService;
import com.yk.example.utils.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
 * @author yk
 * @date 2018/5/911:51
 */
@Controller
public class MusicController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MusicController.class);

    @Autowired
    private MusicService musicService;

    @Value("${music.path}")
    private String musicPath;

    @Value("${music.url}")
    private String musicUrl;

    @RequestMapping(value = "/admin/musicList_{pageCurrent}_{pageSize}_{pageCount}", method = RequestMethod.GET)
    public String index(Model model, Music music, @PathVariable Integer pageCurrent, @PathVariable Integer pageSize, @PathVariable Integer pageCount) {
        if (pageCurrent == 0) {
            pageCurrent = 1;
        }
        if (pageSize == 0) {
            pageSize = 30;
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Page<Music> musicPage = musicService.findAllPage(music, new PageRequest(pageCurrent - 1, pageSize, sort));
        model.addAttribute("musicList", musicPage.getContent());
        String pageHTML = PageUtils.getPageContent("musicList_{pageCurrent}_{pageSize}_{pageCount}?musicName=" + music.getMusicName(), pageCurrent, pageSize, musicPage.getTotalPages());
        model.addAttribute("pageHTML", pageHTML);
        model.addAttribute("music", music);
        return "music/musicManage";
    }

    @RequestMapping(value = "admin/toMusicImport", method = RequestMethod.GET)
    public String toMusicImport() {
        return "music/musicEdit";
    }

    /**
     * 上传apk包
     *
     * @param model
     * @param music
     * @return
     */
    @RequestMapping(value = "/admin/musicSave", method = RequestMethod.POST)
    public String musicSave(Model model, Music music) {
        MultipartFile file = music.getFile();
        if (file != null) {
            String fileName = file.getOriginalFilename();
            try {
                LOGGER.info("开始上传apk包，上传路径为：" + musicPath + "/" + fileName);
                FileOutputStream outputStream = new FileOutputStream(musicPath + "/" + fileName);
                outputStream.write(file.getBytes());
                outputStream.flush();
                outputStream.close();
                LOGGER.info("apk上传完成");
                music.setMusicUrl(musicUrl + fileName);
                musicService.save(music);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:musicList_0_0_0";
    }

}