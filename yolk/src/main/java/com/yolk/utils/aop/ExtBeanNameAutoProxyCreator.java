package com.yolk.utils.aop;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.util.PatternMatchUtils;

import com.google.common.collect.Lists;
import com.yolk.utils.log.LoggerUtils;

/**
 * 对spring现有的
 */
public class ExtBeanNameAutoProxyCreator extends BeanNameAutoProxyCreator {

    /**
     * logger
     */
    protected final static Logger     logger           = LoggerFactory
        .getLogger(ExtBeanNameAutoProxyCreator.class);

    private final static List<String> ignoredBeanNames = Lists.newArrayList();

    static {
        //跳过springmvc的一些bean
        setIgnoredBeanNames("org.springframework.web.*", "(inner bean)*");
    }

    //    @Override
    //    public void setBeanNames(String... beanNames) {
    //        List<String> validNames = Lists.newArrayList();
    //        for (String beanName : beanNames) {
    //            for (String mappedName : ignoredBeanNames) {
    //                if (PatternMatchUtils.simpleMatch(mappedName, beanName)) {
    //                    LoggerUtils.info(logger, "setBeanName跳过Bean:", beanName);
    //                    break;
    //                }
    //            }
    //            validNames.add(beanName);
    //        }
    //        super.setBeanNames(CollectionUtil.listToArray(validNames));
    //    }

    @Override
    protected boolean isMatch(String beanName, String mappedName) {
        for (String ignoreBean : ignoredBeanNames) {
            if (PatternMatchUtils.simpleMatch(ignoreBean, beanName)) {
                LoggerUtils.info(logger, "跳过Bean:", beanName);
                return false;
            }
        }
        return super.isMatch(beanName, mappedName);
    }

    /**
     * 需要在setBeanNames之前执行
     */
    public static void setIgnoredBeanNames(String... ignoredBeanNames) {
        for (String mappedName : ignoredBeanNames) {
            ExtBeanNameAutoProxyCreator.ignoredBeanNames.add(mappedName.trim());
        }
    }
}
