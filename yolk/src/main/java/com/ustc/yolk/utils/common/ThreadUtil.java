package com.ustc.yolk.utils.common;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/27.
 */
public class ThreadUtil {

    private final static ThreadLocal<Map<String, Object>> threadLocalCache = new ThreadLocal<Map<String, Object>>() {
        @Override
        protected Map<String, Object> initialValue() {
            return Maps.newHashMap();
        }
    };

    /**
     * Quiet sleep.
     *
     * @param time the time
     */
    public static void SleepQuiet(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            //ignore
        }
    }

    /**
     * Gets local object.
     *
     * @param key the key
     * @return the local object
     */
    public static Object getLocalObject(String key) {
        return threadLocalCache.get().get(key);
    }

    /**
     * Put local object object.
     *
     * @param key the key
     * @param obj the obj
     * @return the object
     */
    public static Object putLocalObject(String key, Object obj) {
        return threadLocalCache.get().put(key, obj);
    }
}
