package com.yk.example.service;

import com.yk.example.dao.MusicDao;
import com.yk.example.entity.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yk on 2018/4/4.
 */
@Service
public class MusicService {

    @Autowired
    private MusicDao musicDao;

    public List<Music> findAll() {
        List<Music> music = new ArrayList<>();
        Iterable<Music> all = musicDao.findAll();
        all.forEach(m -> {
            music.add(m);
        });
        return music;
    }
}
