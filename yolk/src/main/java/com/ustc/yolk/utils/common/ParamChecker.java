package com.ustc.yolk.utils.common;

import org.apache.commons.lang3.StringUtils;

/**
 * 参数校验类
 * @author Administrator
 * @version $Id: ParamChecker.java, v 0.1 2016年11月27日 下午3:40:45 Administrator Exp $
 */
public class ParamChecker {

    /**
     * 检查字符串不能we空
     * @param paramName 字符串参数名
     * @param content 要检查的字符串
     */
    public static void notBlank(String paramName, String content) {
        if (StringUtils.isBlank(content)) {
            throw new RuntimeException(paramName + " cannot be null!");
        }
    }

    /**
     * 对象不能为空
     * @param obj 要检查的对象
     * @param msg 为空的时候抛出的异常信息
     */
    public static void notNull(Object obj, String msg) {
        if (obj == null) {
            throw new RuntimeException(msg);
        }
    }

    /**
     * 检查是否满足指定条件
     */
    public static void assertCondition(boolean condition, String msg) {
        if (!condition) {
            throw new RuntimeException(msg);
        }
    }
}
