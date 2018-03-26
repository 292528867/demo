package com.yk.example.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Created by yk on 2018/3/26.
 */
public class QiniuUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(QiniuUtils.class);

    private static final String ACCESS_KEY;
    private static final String SECRET_KEY;
    private static final Auth AUTH;

    private static final UploadManager uploadManager = new UploadManager(new Configuration(Zone.zone0()));

    static {
        Properties props = new Properties();
        try (InputStream stream = QiniuUtils.class.getResourceAsStream("/application.properties")) {
            if (stream == null) {
                throw new RuntimeException("找不到相应的配置文件！[classpath:/application.properties]");
            }
            props.load(stream);
            ACCESS_KEY = props.getProperty("qiniu.access_key");
            SECRET_KEY = props.getProperty("qiniu.secret_key");
            AUTH = Auth.create(ACCESS_KEY, SECRET_KEY);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }


    private QiniuUtils() {

    }

    public static boolean upload(File file, String bucket, String key) {
        try {
            Response res = uploadManager.put(file, key, AUTH.uploadToken(bucket, key));
            return res.isOK();
        } catch (QiniuException e) {
            try {
                LOGGER.error(e.response.bodyString());
            } catch (QiniuException e1) {
            }
            return false;
        }
    }

    public static boolean upload(byte[] data, String bucket, String key) {
        try {
            Response res = uploadManager.put(data, key, AUTH.uploadToken(bucket, key));
            return res.isOK();
        } catch (QiniuException e) {
            try {
                LOGGER.error(e.response.bodyString());
            } catch (QiniuException e1) {
            }
            return false;
        }
    }
}
