package com.ustc.yolk.serialize;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

/**
 * 采用fastjson实现的序列化工具
 * Created by Administrator on 2016/8/23 0023.
 */
public class DefaultObjectSerializer implements ObjectSerializer {

    @Override
    public String serialize(Object o) {
        return JSON.toJSONString(o);
    }

    @Override
    public <T> T deSerialize(String originString, Class<T> classType) {
        if (StringUtils.isBlank(originString)) {
            return null;
        }
        return JSON.parseObject(originString, classType);
    }

    @Override
    public Object deSerialize(String originString) {
        if (StringUtils.isBlank(originString)) {
            return null;
        }
        return JSON.parse(originString);
    }
}
