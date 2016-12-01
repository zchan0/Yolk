package com.ustc.yolk.utils.log;


import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Created by Administrator on 2016/11/30.
 */
public class EnhancedLogFilter extends Filter {

    @Override
    public int decide(LoggingEvent event) {
        String className = event.getLogger().getName();
        if (className.startsWith("com.ustc") || className.startsWith("java")) {
            return ACCEPT;
        }
        return DENY;
    }

}
