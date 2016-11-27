package com.yolk.utils.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/11/5.
 */
public class PatternUtil {

    private final static Map<String, Pattern> patternCache = new ConcurrentHashMap<>();

    /**
     * Fast match boolean.
     *
     */
    public static boolean fastMatch(String regex, String name) {
        Pattern pattern = patternCache.get(regex);
        if (pattern == null) {
            pattern = Pattern.compile(regex);
            patternCache.put(regex, pattern);
        }
        return pattern.matcher(name).matches();
    }
}
