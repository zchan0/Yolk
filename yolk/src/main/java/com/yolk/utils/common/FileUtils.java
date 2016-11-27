package com.yolk.utils.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Maps;

/**
 * 文件工具
 *
 * @author Administrator
 * @version $Id : FileUtils.java, v 0.1 2016年7月4日 下午2:33:41 Administrator Exp $
 */
public class FileUtils {

    /**
     * Map to properties.
     *
     * @param map      the map
     * @param pathname the pathname
     */
    public static void mapToProperties(Map<String, String> map, String pathname) {
        if (CollectionUtil.sizeIsEmpty(map)) {
            return;
        }
        Properties properties = new Properties();
        properties.putAll(map);
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(pathname)));
            properties.store(writer, "");
        } catch (IOException e) {
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    /**
     * properties文件转换为map
     */
    public static Map<String, String> propertiesToMap(File file) {

        Map<String, String> resultMap = Maps.newHashMap();
        Reader reader = null;
        try {
            Properties properties = new Properties();
            reader = new BufferedReader(new FileReader(file));
            properties.load(reader);
            Set<String> keys = properties.stringPropertyNames();
            for (String key : keys) {
                resultMap.put(key, properties.getProperty(key));
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            IOUtils.closeQuietly(reader);
        }

        return resultMap;
    }

    /**
     * read a file to String
     *
     * @param path the path
     * @return string string
     */
    public static String readFile(String path) {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            throw new RuntimeException("File not exist or is not a file");
        }
        BufferedReader reader = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            reader = new BufferedReader(new FileReader(file));
            int content = 0;
            while ((content = reader.read()) != -1) {
                stringBuilder.append((char) content);
            }
            reader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Write file.
     *
     * @param path    the path
     * @param content the content
     * @throws IOException the io exception
     */
    public static void writeFile(String path, String content) throws IOException {
        OutputStream fOutputStream = null;
        try {
            fOutputStream = new FileOutputStream(new File(path));
            fOutputStream.write(content.getBytes(Charset.forName("UTF-8")));
            fOutputStream.close();
        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(fOutputStream);
        }
    }

    /**
     * Del file.
     *
     * @param path the path
     */
    public static void delFile(String path) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
    }
}
