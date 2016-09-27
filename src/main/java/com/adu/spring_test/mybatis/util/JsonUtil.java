package com.adu.spring_test.mybatis.util;

import java.io.InputStream;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    // Local variables
    /**
     * logger
     */
    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    /**
     * json
     */
    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    // Logic

    /**
     * json转化为java对象
     *
     * @param is
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T toObject(InputStream is, Class<T> c) {
        try {
            return objectMapper.readValue(is, c);
        } catch (Exception e) {
            logger.error("toObject({}, {})", is, c, e);
            return null;
        }
    }

    /**
     * json转化为java对象
     *
     * @param json
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T toObject(String json, Class<T> c) {
        try {
            return objectMapper.readValue(json, c);
        } catch (Exception e) {
            logger.error("toObject({}, {})", json, c, e);
            return null;
        }
    }

    /**
     * json转化为java对象
     *
     * @param json
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T toObject(String json, TypeReference c, String dateFormat) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
        try {
            return objectMapper.readValue(json, c);
        } catch (Exception e) {
            logger.error("toObject({}, {}, {})", json, c, dateFormat, e);
            return null;
        }
    }

    /**
     * json转化为java对象
     *
     * @param o
     * @return
     */
    public static String toString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            logger.error("toString({})", o, e);
            return null;
        }
    }

    /**
     * json转化为java对象,指定日期格式
     *
     * @param o
     * @param dateFormat
     * @return
     */
    public static String toString(Object o, String dateFormat) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            logger.error("toString({}, {})", o, dateFormat, e);
            return null;
        }
    }

}
