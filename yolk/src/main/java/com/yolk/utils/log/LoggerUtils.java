package com.yolk.utils.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具
 *
 * @author Administrator
 * @version $Id: LoggerUtils.java, v 0.1 2016年7月4日 下午2:32:36 Administrator Exp $
 */
public class LoggerUtils {

    private final static Logger defaultLogger = LoggerFactory.getLogger("DEFAULT-LOGGER");

    /*使用默认的logger*/
    public static void info(Object... content) {
        info(defaultLogger, content);
    }

    /**
     * INFO־
     *
     * @param logger
     * @param content
     */
    public static void info(Logger logger, Object... content) {
        if (!logger.isInfoEnabled()) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Object eachContent : content) {
            stringBuilder.append(eachContent);
        }
        logger.info(stringBuilder.toString());
    }

    /**
     * error־
     *
     * @param logger
     * @param e
     * @param content
     */
    public static void error(Logger logger, Throwable e, Object... content) {

        if (!logger.isErrorEnabled()) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Object eachContent : content) {
            stringBuilder.append(eachContent);
        }
        logger.error(stringBuilder.toString(), e);
    }

    /**
     * debug־
     *
     * @param logger
     * @param content
     */
    public static void debug(Logger logger, Object... content) {
        if (!logger.isDebugEnabled()) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Object eachContent : content) {
            stringBuilder.append(eachContent);
        }
        logger.debug(stringBuilder.toString());
    }

    /**
     * warn־
     *
     * @param logger
     * @param content
     */
    public static void warn(Logger logger, Object... content) {
        if (!logger.isWarnEnabled()) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Object eachContent : content) {
            stringBuilder.append(eachContent);
        }
        logger.warn(stringBuilder.toString());
    }
}
