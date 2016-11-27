package com.ustc.yolk.utils.digest;

import com.ustc.yolk.model.Constants;
import com.ustc.yolk.utils.log.LoggerUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 运行摘要日志格式为(类名,函数名,执行时间,执行结果,入参,出参)
 *
 * @author Administrator
 */
public class DigestInterceptor implements MethodInterceptor, Constants {

    private static final Logger logger = LoggerFactory.getLogger(DigestInterceptor.class);

    /**
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {

        // 优先用类的注解，方法注解可以覆盖类注解
        Digest methodDigest = invocation.getMethod().getAnnotation(Digest.class);
        Digest classDigest = invocation.getMethod().getDeclaringClass().getAnnotation(Digest.class);
        if (methodDigest == null && classDigest == null) {
            return invocation.proceed();
        }
        Digest digest = (methodDigest == null ? classDigest : methodDigest);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LEFT_KUOHAO);
        stringBuilder.append(invocation.getMethod().getDeclaringClass().getSimpleName());
        stringBuilder.append(DOU_HAO);
        stringBuilder.append(invocation.getMethod().getName());
        stringBuilder.append(DOU_HAO);
        // inTime.set(new Date());
        long inDate = System.currentTimeMillis();
        long outDate = 0;
        Object result = null;
        try {
            result = invocation.proceed();
            outDate = System.currentTimeMillis();
            stringBuilder.append((outDate - inDate)).append("ms").append(DOU_HAO);
            stringBuilder.append(NORMAL).append(DOU_HAO);
            return result;
        } catch (Throwable e) {
            outDate = System.currentTimeMillis();
            stringBuilder.append((outDate - inDate)).append("ms").append(DOU_HAO);
            stringBuilder.append("Exception:").append(e.getMessage()).append(DOU_HAO);
            throw e;
        } finally {
            fillInOutParams(digest, stringBuilder, invocation, result);
            if (digest.print()) {
                LoggerUtils.info(logger, stringBuilder.toString());
            }
        }
    }

    /**
     * 填充入参和出参
     *
     * @param digest
     * @param stringBuilder
     * @param invocation
     * @param result
     */
    private void fillInOutParams(Digest digest, StringBuilder stringBuilder,
                                 MethodInvocation invocation, Object result) {
        int limit = digest.printLenLimit();
        if (digest.printInParams()) {
            stringBuilder.append(limit == 0 ? getInParams(invocation)
                    : StringUtils.substring(getInParams(invocation), 0, limit));
        } else {
            stringBuilder.append("not print");
        }
        stringBuilder.append(DOU_HAO);
        if (digest.printOutParams()) {
            stringBuilder.append(limit == 0 ? getOutParams(result)
                    : StringUtils.substring(getOutParams(result), 0, limit));
        } else {
            stringBuilder.append("not print");
        }
        stringBuilder.append(RIGHT_KUOHAO);
    }

    /**
     * 获取入参
     *
     * @param invocation
     * @return
     */
    private String getInParams(MethodInvocation invocation) {
        Object[] params = invocation.getArguments();
        if (params == null) {
            return KONG;
        }
        if (params.length == 0) {
            return VOID;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            Object o = params[i];
            if (o != null) {
                stringBuilder.append(o.toString());
            } else {
                stringBuilder.append(KONG);
            }
            if (i != params.length - 1) {
                stringBuilder.append(JIAN_KUOHAO);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 获取出参
     *
     * @param invocation
     * @return
     */
    private String getOutParams(Object result) {
        if (result == null) {
            return KONG;
        }
        return result.toString();
    }
}
