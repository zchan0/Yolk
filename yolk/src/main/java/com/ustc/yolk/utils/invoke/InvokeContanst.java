package com.ustc.yolk.utils.invoke;

/**
 * Created by Administrator on 2016/11/28.
 */
public interface InvokeContanst {
    String LEFT_SQUARE = "[";
    String RIGHT_SQUARE = "]";
    String LEFT_ROUND = "(";
    String RIGHT_ROUND = ")";
    String COMMA = ",";
    String SPLITE_ARROW = "->";

    /*一些用于配置打印信息的key 可用于配置需要输出的日志信息*/
    public static enum LOGMSG {
        TRACE_ID, INVOKE_TYPE, INPUTS, OUTPUTS, EXCEPTION, MONITOR_RESOURCE
    }
}
