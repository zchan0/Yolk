/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.ustc.yolk.utils.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 集合工具类
 *
 * @author Administrator
 * @version $Id: CollectionUtil.java, v 0.1 2016年7月6日 上午2:16:23 Administrator Exp $
 */
public class CollectionUtil extends CollectionUtils {

    public static <T> List<T> cast(List<? extends T> source) {
        List<T> result = Lists.newArrayList();
        for (T t : source) {
            result.add(t);
        }
        return result;
    }

    /**
     * 数组转换为ArrayList
     *
     * @param ts 源数组
     * @return 集合
     */
    public static <T> List<T> arraysToList(@SuppressWarnings("unchecked") T... ts) {

        List<T> lTs = new ArrayList<T>();
        if (ts == null) {
            return lTs;
        }
        for (T t : ts) {
            lTs.add(t);
        }
        return lTs;
    }

    public static String[] listToArray(List<String> t) {
        String[] ss = new String[t.size()];
        for (int i = 0; i < t.size(); i++) {
            ss[i] = t.get(i);
        }
        return ss;
    }

    /**
     * 将数组转换为Map
     *
     * @param sourceMap
     * @return
     */
    public static <T> Map<T, T> arrayToMap(@SuppressWarnings("unchecked") T... sourceMap) {
        Map<T, T> map = Maps.newHashMap();
        if (sourceMap == null || (sourceMap.length % 2 != 0)) {
            return map;
        }
        T key = null;
        for (int i = 0; i < sourceMap.length; i++) {
            if (i % 2 == 0) {
                key = sourceMap[i];
            } else {
                map.put(key, sourceMap[i]);
            }
        }
        return map;
    }
}
