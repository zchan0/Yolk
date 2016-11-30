package com.ustc.yolk.web;

import com.ustc.yolk.model.User;
import com.ustc.yolk.utils.RSAUtil;
import com.ustc.yolk.utils.common.ParamChecker;
import com.ustc.yolk.utils.log.LoggerUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2016/11/5.
 */
@Controller
@RequestMapping(value = "/pic")
public class PictureController extends BaseController {

    //日志
    private final static Logger LOGGER = LoggerFactory.getLogger(PictureController.class);
    private final static AtomicLong picCounts = new AtomicLong(0);
    //    private final static String FILE_PATH = "/root/yolkfiles/";
    private final static String FILE_PATH = "C://";
    private final static String[] validPicType = {"jpg", "png", "ico"};


    @RequestMapping(value = "upload.json")
    @ResponseBody
    public String addPic(HttpServletRequest req) {
        try {
            User user = getUserFromRequest(req);
            ParamChecker.assertCondition(req instanceof MultipartHttpServletRequest, SYSTEM_ERROR);
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
            MultipartFile imgFile1 = multipartRequest.getFile("img");
            writeFile(imgFile1, user.getUsername());
            return wrapSuccessResult();
        } catch (Exception e) {
            LoggerUtils.error(LOGGER, e, "upload picture error!");
            return wrapResult(false, e.getMessage());
        }
    }


    @RequestMapping(value = "download.json")
    public void getImage(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "fileName", required = false) String fileName,
                         @RequestParam(value = "username", required = false) String username) {

        FileInputStream fis = null;
        try {
            ParamChecker.notBlank("fileName", fileName);
            fileName = RSAUtil.decrypt(fileName);
            String[] temp = StringUtils.split(fileName, ".");
            ParamChecker.assertCondition(temp.length == 2, "illegal fileName!");
            response.setContentType("image/" + temp[1]);
//            User user = getUserFromRequest(request);
            OutputStream out = response.getOutputStream();
            File file = new File(getFilePath(username, fileName));
            fis = new FileInputStream(file);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            out.write(b);
            out.flush();
        } catch (Exception e) {
            LoggerUtils.error(LOGGER, e, "download pic error!");
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }

    /*写图片*/
    private void writeFile(MultipartFile multipartFile, String username)
            throws IllegalStateException,
            IOException {
        if (multipartFile != null) {
            createFolder(username);
            //取得当前上传文件的文件名称
            String myFileName = multipartFile.getOriginalFilename().trim();
            //如果名称不为“”,说明该文件存在，否则说明该文件不存在
            ParamChecker.notBlank("file name", myFileName);
            File localFile = new File(getFilePath(username, parseFileName(username, myFileName)));
            multipartFile.transferTo(localFile);
        }
    }

    /*生成保存在系统的文件绝对地址*/
    private String getFilePath(String username, String fileName) {
        return FILE_PATH + username + File.separator + fileName;
    }

    /*生成保存在文件系统的文件名*/
    private String parseFileName(String username, String originalName) {
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return username + "_" + time + picCounts.addAndGet(1) + "_" + originalName;
    }

    /**
     * 创建文件夹 文件夹名称为当前日期
     */
    private void createFolder(String username) {
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
    private boolean isValidPicType(String subFix) {
        for (String s : validPicType) {
            if (StringUtils.equals(subFix, s)) {
                return true;
            }
        }
        return false;
    }
}
