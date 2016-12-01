package com.ustc.yolk.utils;

import com.ustc.yolk.utils.common.ParamChecker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2016/12/1.
 */
public class PicUploadUtil {
    private final static AtomicLong picCounts = new AtomicLong(0);
    //    private final static String FILE_PATH = "/root/yolkfiles/";
    private final static String FILE_PATH = "C://";
    private final static String[] validPicType = {"jpg", "png", "ico"};

    /*写图片 会校验文件名*/
    public void writeFile(MultipartFile multipartFile, String username)
            throws IllegalStateException,
            IOException {
        if (multipartFile != null) {
            ensureFileFolderIsNotNull(username);
            //取得当前上传文件的文件名称
            String myFileName = multipartFile.getOriginalFilename().trim();
            //如果名称不为“”,说明该文件存在，否则说明该文件不存在
            ParamChecker.notBlank("file name", myFileName);
            ParamChecker.assertCondition(isValidPicType(myFileName), "unsupported file type");
            File localFile = new File(getFilePath(username, parseFileName(username, myFileName)));
            multipartFile.transferTo(localFile);
        }
    }

    /*获取文件所在的文件夹*/
    public static String getFileDir(String username) {
        return FILE_PATH + username;
    }

    /*生成保存在系统的文件绝对地址*/
    public static String getFilePath(String username, String fileName) {
        return FILE_PATH + username + File.separator + fileName;
    }

    /*生成保存在文件系统的文件名*/
    public static String parseFileName(String username, String originalName) {
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return username + "_" + time + picCounts.addAndGet(1) + "_" + originalName;
    }

    /**
     * 创建文件夹 文件夹名称为当前日期
     */
    public static void ensureFileFolderIsNotNull(String username) {
        File file = new File(FILE_PATH + username);
        if (file.exists() && file.isDirectory()) {
            return;
        }
        if (file.mkdir()) {
            return;
        }
        throw new RuntimeException("create folder error!");
    }

    /*上传的图片类型是否是正确的*/
    public static boolean isValidPicType(String fileName) {
        for (String s : validPicType) {
            if (StringUtils.endsWith(fileName, s)) {
                return true;
            }
        }
        return false;
    }

}
