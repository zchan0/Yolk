/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */

import com.alibaba.fastjson.JSON;
import com.ustc.yolk.utils.log.LoggerUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Administrator
 * @version $Id: BaseTestSupport.java, v 0.1 2016年10月2日 下午10:48:15 Administrator Exp $
 */
public class BaseTestSupport extends Assert {

    /**
     * logger
     */
    protected final static Logger logger = LoggerFactory.getLogger(BaseTestSupport.class);
    protected static ApplicationContext springContext = null;

    static {
        /** 上下文 */
        springContext = new ClassPathXmlApplicationContext("spring.xml");
    }

    protected void print(Object... objects) {
        LoggerUtils.info(logger, JSON.toJSONString(objects));
    }
}
