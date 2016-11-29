package com.ustc.yolk.utils.invoke;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.ustc.yolk.utils.Profiler;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/28.
 */
public abstract class InvokeContext<T extends InvokeContext> implements Serializable, Cloneable, InvokeContanst {

    private static ThreadLocal<InvokeContext> contextHolder = new ThreadLocal<InvokeContext>();
    /*应用名*/
    private String appName;
    /*监控资源(记录本次调用的资源消耗情况)*/
    private transient Profiler.MonitorResource monitorResource;
    /*会传输到下个系统的参数*/
    private Map<String, String> transferParams = Maps.newHashMap();
    /*不会传输到下个系统的参数*/
    private Map<String, Object> nonTransferParams = Maps.newHashMap();
    /*traceId*/
    private String traceId;
    /*入参*/
    private transient Map<String, String> inputs = Maps.newHashMap();
    /*调用出参*/
    private transient Map<String, String> outputs = Maps.newHashMap();
    /*如果出现抛出的异常 需要设置进来*/
    private Exception exception;

    /*设置调用上下文*/
    public static void set(InvokeContext context) {
        contextHolder.set(context);
    }

    /*获取上下文*/
    public static InvokeContext get() {
        return contextHolder.get();
    }

    /*设置调用traceId*/
    public void setTraceId(String traceId) {
        this.traceId = traceId;
        //把traceId放到传输的参数中
        transferParams.put(TransferParamEnum.TRACE_ID.name(), traceId);
    }

    @Override
    public String toString() {
        //格式为(traceId)[入参][出参][是否异常][消耗时间]
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LEFT_ROUND).append(getTraceId()).append(RIGHT);
        stringBuilder.append(LEFT_SQUARE).append(JSON.toJSONString(inputs)).append(RIGHT_SQUARE);
        stringBuilder.append(LEFT_SQUARE).append(JSON.toJSONString(mapToString(outputs))).append(RIGHT_SQUARE);
        stringBuilder.append(LEFT_SQUARE).append(exception == null ? "no exp" : exception.getMessage()).append(RIGHT_SQUARE);
        stringBuilder.append(LEFT_SQUARE).append(monitorResource == null ? "" : String.valueOf(monitorResource.getDurTime())).append("ms").append(RIGHT_SQUARE);
        return stringBuilder.toString();
    }

    /*将map转换为String输出 对value长度做了截断*/
    protected <V> String mapToString(Map<String, V> map) {
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        for (Map.Entry<String, V> entry : map.entrySet()) {
            stringBuilder.append(entry.getKey()).append(SPLITE_ARROW);
            V value = entry.getValue();
            String valueString = value.toString();
            if (valueString.length() > 512) {
                valueString = StringUtils.substring(valueString, 0, 511);
                stringBuilder.append(valueString).append("....");
            } else {
                stringBuilder.append(valueString);
            }
            if (count < map.size() - 1) {
                stringBuilder.append(COMMA);
            }
            count++;
        }
        return stringBuilder.toString();
    }

    public void setMonitorResource(Profiler.MonitorResource monitorResource) {
        this.monitorResource = monitorResource;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public void putOutputParam(String key, String value) {
        outputs.put(key, value);
    }

    public void putInputParam(String key, String value) {
        inputs.put(key, value);
    }

    public void putTransferParam(String key, String value) {
        transferParams.put(key, value);
    }

    public void putNonTransferParam(String key, String value) {
        nonTransferParams.put(key, value);
    }

    public Map<String, String> getTransferParams() {
        return transferParams;
    }

    public Map<String, Object> getNonTransferParams() {
        return nonTransferParams;
    }

    public String getTraceId() {
        return traceId;
    }

    public Map<String, String> getInputs() {
        return inputs;
    }

    public Map<String, String> getOutputs() {
        return outputs;
    }
}
