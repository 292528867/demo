package com.yk.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * objectMapper 单例模式
 * Created by yk on 2018/3/26.
 */
public class ObjectMapperUtils {

    private static ObjectMapper objectMapper;

    private ObjectMapperUtils() {

    }

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }
}
