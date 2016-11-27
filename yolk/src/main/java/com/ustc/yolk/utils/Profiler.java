package com.ustc.yolk.utils;

import com.google.common.collect.Lists;
import com.ustc.yolk.utils.log.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 性能相关的调试工具,支持,线程正式场景,做运行时间的profile,运行性能监控(不建议线上使用,因为需要开启监控线程)
 * 该工具不会抛出任何异常
 *
 * @author Administrator
 * @version $Id: ProfileUtils.java, v 0.1 2016年9月5日 下午11:02:45 Administrator Exp $
 */
public class Profiler {

    /**
     * debug的时候用到的logger
     */
    private final static Logger DEBUG_LOGGER = LoggerFactory
            .getLogger(Profiler.class);

    private final static String LOG_TEMPLATE = "[os=%s][messag=%s][startTime=%s][endTime=%s][durationTime=%sms][processors=%s][monitorResources=%s]";
    private final static String SIMPLE_LOG_TEMPLATE = "[durationTime=%sms][message=%s]";
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss");

    /**
     * profile日志,建议运行中别做修改,否则有些配置会导致残留线程
     */
    private static ThreadLocal<ProfileConfig> configHolder = new ThreadLocal<ProfileConfig>() {
        protected ProfileConfig initialValue() {
            return new ProfileConfig(
                    false, false,
                    0, false);
        }

        ;
    };

    /**
     * 开始monitor的时间
     */
    private static ThreadLocal<Stack<MonitorResource>> resStackHolder = new ThreadLocal<Stack<MonitorResource>>() {
        protected java.util.Stack<MonitorResource> initialValue() {
            return new Stack<MonitorResource>();
        }

        ;
    };

    /**
     * 监控线程
     */
    private static ThreadLocal<MonitorThread> monitorThreadHolder = new ThreadLocal<MonitorThread>();

    /**
     * 开始monitor
     */
    public static void enter(Object msgObj) {
        try {
            Stack<MonitorResource> monitorResStack = resStackHolder.get();
            monitorResStack.push(new MonitorResource(msgObj, System.currentTimeMillis()));
            ProfileConfig config = configHolder.get();
            //开启监控线程
            if (config.useMonitorThread) {
                if (monitorThreadHolder.get() != null) {
                    killThread();
                }
                MonitorThread monitorThread = new MonitorThread(getCurrentMonitorRes(), config);
                monitorThreadHolder.set(monitorThread);
                monitorThread.start();
            }
        } catch (Throwable e) {
            if (configHolder.get().debugMode) {
                LoggerUtils.error(DEBUG_LOGGER, e, "进入Profiler异常");
            }
        }
    }

    /**
     * 开始monitor
     */
    public static void enter() {
        enter(Thread.currentThread().getName());
    }

    /**
     * 结束monitor
     */
    public static MonitorResource release() {
        try {
            Stack<MonitorResource> monitorResStack = resStackHolder.get();
            MonitorResource monitorResource = getCurrentMonitorRes();
            monitorResource.setEndTime(System.currentTimeMillis());
            ProfileConfig config = configHolder.get();
            //监控线程关闭
            if (config.useMonitorThread) {
                killThread();
            }
            return monitorResStack.pop();
        } catch (Throwable e) {
            if (configHolder.get().debugMode) {
                LoggerUtils.error(DEBUG_LOGGER, e, "释放Profiler异常");
            }
            return new MonitorResource(e.getMessage(), 0);
        }
    }

    /**
     * 使用新的messageObj替换原来的
     */
    public static MonitorResource release(Object messageObj) {
        MonitorResource monitorResource = release();
        monitorResource.setMessageObj(messageObj);
        return monitorResource;
    }

    /**
     * 结束monitor并且打印日志
     */
    public static MonitorResource releaseAndLog(Logger logger, Object messageObj) {
        MonitorResource resource = release(messageObj);
        LoggerUtils.info(logger, resource);
        return resource;
    }

    /**
     * 结束monitor并且打印日志
     */
    public static MonitorResource releaseAndLog(Logger logger) {
        MonitorResource resource = release();
        LoggerUtils.info(logger, resource);
        return resource;
    }

    /**********************************Profiler的一些配置***********************************************/

    /**
     * 开启监控线程
     */
    public static void useMonitorThread(boolean use, int monitorCollectDurTime) {
        ProfileConfig config = configHolder.get();
        config.useMonitorThread = use;
        config.monitorCollectDurTime = monitorCollectDurTime;
    }

    /**
     * 开启debug模式
     */
    public static void debugMode(boolean isDebug) {
        ProfileConfig config = configHolder.get();
        config.debugMode = isDebug;
    }

    /**
     * 设置profile配置
     *
     * @param config
     */
    public static void setProfileConfig(ProfileConfig config) {
        configHolder.set(config);
    }

    /**
     * 移除监控线程
     */
    private static void killThread() {
        try {
            MonitorThread futureTask = monitorThreadHolder.get();
            monitorThreadHolder.remove();
            futureTask.interrupt();
        } catch (Throwable e) {
            // ignore
            if (configHolder.get().debugMode) {
                LoggerUtils.error(DEBUG_LOGGER, e, "进入Profiler异常");
            }
        }
    }

    /**
     * 获取当前的monitorRes
     */
    public static MonitorResource getCurrentMonitorRes() {
        try {
            Stack<MonitorResource> resStack = resStackHolder.get();
            return resStack.get(resStack.size() - 1);
        } catch (Exception e) {
            if (configHolder.get().debugMode) {
                LoggerUtils.error(DEBUG_LOGGER, e, "获取监控资源异常");
            }
            return new MonitorResource(e.getMessage(), 0);
        }
    }

    /**
     * 资源使用情况，比如cpu最大使用量等。
     *
     * @author Administrator
     * @version $Id: Profile.java, v 0.1 2016年9月5日 下午11:38:39 Administrator Exp $
     */
    public static class MonitorResource {

        /**
         * 系统名
         */
        private final static String osName = System.getProperty("os.name");

        /**
         * 当前资源的标志
         */
        private Object messageObj = null;

        private long startTime = 0;

        private long endTime = 0;

        private int processorNums = 0;

        private List<Long> memUse = Lists.newArrayList();

        private List<Integer> cpuUse = Lists.newArrayList();

        public MonitorResource(Object messageObj, long startTime) {
            super();
            this.messageObj = messageObj;
            this.startTime = startTime;
        }

        public String getMemUse() {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < memUse.size(); i++) {
                stringBuilder.append(memUse.get(i) / 1024L + "K");
                if (i != memUse.size() - 1) {
                    stringBuilder.append(",");
                }
            }
            return stringBuilder.toString();
        }

        public String getCpuUse() {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < cpuUse.size(); i++) {
                stringBuilder.append(cpuUse.get(i) + "K");
                if (i != cpuUse.size() - 1) {
                    stringBuilder.append(",");
                }
            }
            return stringBuilder.toString();
        }

        /**
         * 获取整个profile堆栈
         */
        public Stack<MonitorResource> getMonitorResStack() {
            return resStackHolder.get();
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return configHolder.get().useSimpleLogTemplate
                    ? (String.format(SIMPLE_LOG_TEMPLATE, endTime - startTime, messageObj))
                    : (String.format(LOG_TEMPLATE, osName, messageObj,
                    DATE_FORMAT.format(new Date(startTime)), DATE_FORMAT.format(new Date(endTime)),
                    endTime - startTime, processorNums, getMemUse()));
        }

        /**
         * 获取运行时间
         */
        public long getDurTime() {
            return endTime - startTime;
        }

        public void putMemUse(long l) {
            memUse.add(l);
        }

        public void putcpuUse(int cpuUse) {
            this.cpuUse.add(cpuUse);
        }

        /**
         * Setter method for property <tt>endTime</tt>.
         *
         * @param endTime value to be assigned to property endTime
         */
        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        /**
         * Getter method for property <tt>messageObj</tt>.
         *
         * @return property value of messageObj
         */
        public Object getMessageObj() {
            return messageObj;
        }

        /**
         * Setter method for property <tt>messageObj</tt>.
         *
         * @param messageObj value to be assigned to property messageObj
         */
        public void setMessageObj(Object messageObj) {
            this.messageObj = messageObj;
        }

        /**
         * Setter method for property <tt>processorNums</tt>.
         *
         * @param processorNums value to be assigned to property processorNums
         */
        public void setProcessorNums(int processorNums) {
            this.processorNums = processorNums;
        }

    }

    public static class ProfileConfig {

        private boolean debugMode = false;
        private boolean useSimpleLogTemplate = false;
        private boolean useMonitorThread = false;
        private int monitorCollectDurTime = 500;

        public ProfileConfig(boolean useSimpleLogTemplate, boolean useMonitorThread,
                             int monitorCollectDurTime, boolean debugMode) {
            super();
            this.useSimpleLogTemplate = useSimpleLogTemplate;
            this.useMonitorThread = useMonitorThread;
            this.monitorCollectDurTime = monitorCollectDurTime;
            this.debugMode = debugMode;
        }

    }

    private static class MonitorThread extends Thread {

        private static final AtomicLong threadCount = new AtomicLong();
        private final ProfileConfig config;
        private MonitorResource monitorResource;

        public MonitorThread(MonitorResource resource, ProfileConfig config) {
            monitorResource = resource;
            setName("monitor-thread-" + threadCount.getAndIncrement());
            setDaemon(true);
            this.config = config;
        }

        /**
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {
            monitorResource.setProcessorNums(Runtime.getRuntime().availableProcessors());
            while (true) {
                monitorResource.putMemUse(
                        Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory());

                try {
                    Thread.sleep(config.monitorCollectDurTime);
                } catch (InterruptedException e) {
                    if (configHolder.get().debugMode) {
                        LoggerUtils.error(DEBUG_LOGGER, e, "monitor线程被interrupt");
                    }
                    return;
                }
            }
        }

    }
}
