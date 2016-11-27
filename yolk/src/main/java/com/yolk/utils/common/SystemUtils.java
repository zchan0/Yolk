package com.yolk.utils.common;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * 系统相关的工具类
 *
 * @author Administrator
 * @version $Id : SystemUtils.java, v 0.1 2016年9月18日 下午2:19:00 Administrator Exp $
 */
public class SystemUtils {

    private static final String     osName           = "os.name";
    private static final String     fileSeparator    = "file.separator";
    private static final String     osType           = "sun.desktop";
    private static final String     cpuType          = "sun.cpu.isalist";
    private static final String     hostName;
    private static final Properties systemProperties = System.getProperties();

    static {
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets os name.
     *
     * @return the os name
     */
    public static String getOsName() {
        return systemProperties.get(osName) + StringUtils.EMPTY;
    }

    /**
     * Gets file separator.
     *
     * @return the file separator
     */
    public static String getFileSeparator() {
        return systemProperties.get(fileSeparator) + StringUtils.EMPTY;
    }

    /**
     * Gets os type.
     *
     * @return the os type
     */
    public static String getOsType() {
        return systemProperties.getProperty(osType) + StringUtils.EMPTY;
    }

    /**
     * Gets cpu type.
     *
     * @return the cpu type
     */
    public static String getCpuType() {
        return systemProperties.getProperty(cpuType) + StringUtils.EMPTY;
    }

    /**
     * Gets host name.
     *
     * @return the host name
     */
    public static String getHostName() {
        return hostName;
    }

    /**
     * 将path从win转换为对应平台的链接
     *
     * @param originPath the origin path
     * @return the string
     */
    public static String getPathFromWin(String originPath) {
        return StringUtils.replace(originPath, "\\", getFileSeparator());
    }

    /**
     * Gets system load.
     *
     * @return the system load
     */
    public static double getSystemLoad() {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
            .getOperatingSystemMXBean();
        return osmxb.getSystemLoadAverage() / osmxb.getAvailableProcessors();
    }

}
