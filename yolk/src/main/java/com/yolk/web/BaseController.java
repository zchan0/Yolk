/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.yolk.web;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.yolk.utils.common.BaseResult;

/**
 * controller公用方法
 * @author Administrator
 * @version $Id: BaseController.java, v 0.1 2016年11月27日 下午12:55:12 Administrator Exp $
 */
public class BaseController {

    private final static String filePath = "/root/yolkfiles/";

    /**
     * 构建json格式的返回结果
     * @param success 是否成功
     * @param msg 错误信息 success=true时为null
     * @return 返回给前端的JSON字符串
     */
    protected String wrapResult(boolean success, String msg) {
        return JSON.toJSONString(new BaseResult(success, null, msg));
    }

    protected void writeFile(MultipartFile multipartFile,
                             String username) throws IllegalStateException, IOException {
        if (multipartFile != null) {
            createFolder(username);
            //取得当前上传文件的文件名称  
            String myFileName = multipartFile.getOriginalFilename();
            //如果名称不为“”,说明该文件存在，否则说明该文件不存在  
            if (myFileName.trim() != "") {
                System.out.println(myFileName);
                //重命名上传后的文件名  
                String fileName = filePath + username + "/" + multipartFile.getOriginalFilename();
                //定义上传路径  
                String path = "H:/" + fileName;
                File localFile = new File(path);
                multipartFile.transferTo(localFile);
            }
        }
    }

    /**
     * 创建文件夹 文件夹名称为当前日期
     */
    private void createFolder(String username) {
        File file = new File(filePath + username);
        if (file.exists() && file.isDirectory()) {
            return;
        }
        if (file.mkdir()) {
            return;
        }
        throw new RuntimeException("创建文件夹失败");
    }
}
