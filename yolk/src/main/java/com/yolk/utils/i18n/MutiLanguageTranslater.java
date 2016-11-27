package com.yolk.utils.i18n;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.yolk.utils.common.FileUtils;
import com.yolk.utils.log.LoggerUtils;

/**
 * 多国语言翻译
 */
public class MutiLanguageTranslater {

    private static final Logger                               logger              = LoggerFactory
        .getLogger(MutiLanguageTranslater.class);
    private static final String                               filePath            = "i18n";
    private final static LanguageTypeEnum                     defaultLanguageType = LanguageTypeEnum.ZH_CN;
    private static Map<LanguageTypeEnum, Map<String, String>> cache               = Maps
        .newHashMap();

    static {
        cache = loadFromFile();
    }

    private MutiLanguageTranslater() {
        //只能通过静态方法调用
    }

    /**
     * 将key翻译为对应的语言描述
     */
    public static String translate(String key,
                                   LanguageTypeEnum languageType) throws TranslateExcption {
        Map<String, String> languageMap = cache.get(languageType);
        if (CollectionUtils.isEmpty(languageMap)) {
            throw new TranslateExcption("请配置多国语言文件");
        }
        String result = languageMap.get(key);
        if (StringUtils.isBlank(result)) {
            throw new TranslateExcption("未找到" + key + "的" + languageType.name() + "的配置");
        }
        return result;
    }

    /**
     * 将key翻译为默认的描述(中文)
     */
    public static String translate(String key) throws TranslateExcption {
        return translate(key, defaultLanguageType);
    }

    /* 从线程变量中获取多国语言信息 */
    //    public static String translateWithConextSupport(String key) throws TranslateExcption {
    //        Object typeObj = InvokeContextUtil.getParam(InvokeConstant.LANGUAGE_TYPE_IN_CONTEXT);
    //
    //        if (typeObj == null) {
    //            throw new TranslateExcption("从上下文中获取语言配置文件为空");
    //        }
    //        LanguageTypeEnum languageTypeEnum = LanguageTypeEnum.valueOf(typeObj + StringUtils.EMPTY);
    //        return translate(key, languageTypeEnum);
    //    }

    private static Map<LanguageTypeEnum, Map<String, String>> loadFromFile() {
        Map<LanguageTypeEnum, Map<String, String>> cache = Maps.newHashMap();
        ClassPathResource classPathResource = new ClassPathResource(filePath);
        try {
            File file = classPathResource.getFile();
            File[] languageFiles = file.listFiles();
            if (languageFiles != null) {
                for (File languageFile : languageFiles) {
                    LanguageTypeEnum typeEnum = getLanguageType(languageFile.getName());
                    if (typeEnum != null) {
                        Map<String, String> language = FileUtils.propertiesToMap(languageFile);
                        cache.put(typeEnum, language);
                    }
                }
            }
        } catch (IOException e) {
            //ignore
        }
        return cache;
    }

    private static LanguageTypeEnum getLanguageType(String fileName) {
        for (LanguageTypeEnum typeEnum : LanguageTypeEnum.values()) {
            if (StringUtils.containsIgnoreCase(fileName, typeEnum.name())) {
                return typeEnum;
            }
        }
        return null;
    }

    /**
     * Print content.
     */
    public synchronized static void printContent() {
        LoggerUtils.info(logger, "map内容=", JSON.toJSONString(cache));
    }
}
