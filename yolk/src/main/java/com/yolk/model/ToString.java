package com.yolk.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.alibaba.fastjson.JSON;
import com.yolk.serialize.ObjectSerializer;
import com.yolk.serialize.SerializeFactory;

/**
 * 对象ToString方法以及clone方法(深度clone)
 * @author Administrator
 * @version $Id: ToString.java, v 0.1 2016年7月4日 下午9:57:47 Administrator Exp $
 */
public abstract class ToString implements Serializable, Cloneable {

    /**  */
    private static final long  serialVersionUID     = 6505082999570528672L;
    /** json格式 */
    protected final int        JSON_TYPE            = 1;
    /** commons-lang的格式 */
    protected final int        COMMONS_LANG_TYPE    = 0;
    protected ObjectSerializer defaultObjSerializer = SerializeFactory.getDefaultSerializer();

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        int type = toStringType();
        if (type == COMMONS_LANG_TYPE) {
            return ReflectionToStringBuilder.toString(this);
        }
        return JSON.toJSONString(this);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return getSerializeClass().deSerialize(getSerializeClass().serialize(this), getClass());
    }

    protected ObjectSerializer getSerializeClass() {
        return defaultObjSerializer;
    }

    /**
     * toString使用哪种类型,0代表commons-lang,1代表fastjson
     * @return
     */
    protected int toStringType() {
        return JSON_TYPE;
    }
}
