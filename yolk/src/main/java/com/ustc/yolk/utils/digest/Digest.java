package com.ustc.yolk.utils.digest;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 摘要日志注解
 *
 * @author Administrator
 * @version $Id: Digest.java, v 0.1 2016年7月4日 下午2:31:11 Administrator Exp $
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Digest {

    /**
     * 是否打印
     *
     * @return
     */
    public boolean print() default true;

    /**
     * 是否打印入参
     *
     * @return
     */
    public boolean printInParams() default true;

    /**
     * 是否打印出参
     *
     * @return
     */
    public boolean printOutParams() default true;

    /**
     * 输出参数时是否做大小限制
     *
     * @return print==true时0:默认不做限制,print==false时该参数不生效
     */
    public int printLenLimit() default 0;
}
