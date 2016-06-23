package com.orange.blog.common;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Json转换工具类.
 * Created by luyayong on 15/11/30.
 */
public class JsonUtil {

    /**
     * 对象转Json 字符串
     * @param obj
     * @return json string
     */
    public static String toJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;
        try {
            jsonInString = mapper.writeValueAsString(obj);
        } catch (Exception e) {
        }
        return jsonInString;
    }

    /**
     * Json 字符串转对象
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        T t = null;
        try {
            t = mapper.readValue(json, clazz);
        } catch (Exception e) {
        }
        return t;
    }

    /**
     * Json 字符串转对象
     * @param json
     * @param valueTypeRef
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, TypeReference valueTypeRef) {
        ObjectMapper mapper = new ObjectMapper();
        T t = null;
        try {
            t = mapper.readValue(json, valueTypeRef);
        } catch (Exception e) {
        }
        return t;
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
     }

    public static <T> List<T>  fromJsonArr(String json,Class<T> classs){
        JavaType javaType=getCollectionType(ArrayList.class,classs);

        List<T> list=null;
        try {
            list = new ObjectMapper().readValue(json, javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
