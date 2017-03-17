package com.vz.share.utils;

import com.google.gson.Gson;

/**
 * Created by VVz on 2017/3/16.
 *
 * @des json Json 工具
 */
public class JsonUtil {

    /**
     * 序列化对象为JSON字符串
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }
    /**
     * 转换JSON字符串为Object对象
     * @param jsonStr JSON字符串
     * @param cls 对象类型
     * @return
     */
    public static Object fromJson(String jsonStr, Class<?> cls) {
        return new Gson().fromJson(jsonStr, cls);
    }
}
