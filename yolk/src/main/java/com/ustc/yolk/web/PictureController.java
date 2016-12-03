package com.ustc.yolk.web;

import com.google.common.collect.Maps;
import com.ustc.yolk.model.User;
import com.ustc.yolk.utils.PicUploadUtil;
import com.ustc.yolk.utils.RSAUtil;
import com.ustc.yolk.utils.common.ParamChecker;
import com.ustc.yolk.utils.log.LoggerUtils;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/5.
 */
@Controller
@RequestMapping(value = "/pic")
public class PictureController extends BaseController {

    //日志
    private final static Logger LOGGER = LoggerFactory.getLogger(PictureController.class);
    //TODO 对图片做缓存

    @RequestMapping(value = "upload.json")
    @ResponseBody
    public String addPic(HttpServletRequest req) {
        try {
            User user = getUserFromRequest(req);
            Map<String, String> pics = uploadPic(req, user);
            ParamChecker.assertCondition(pics.size() == 1, "illegal pic content!");
            String picName = null;
            for (Map.Entry<String, String> entry : pics.entrySet()) {
                picName = entry.getValue();
            }
            return wrapSuccessResult("picName", picName);
        } catch (Exception e) {
            LoggerUtils.error(LOGGER, e, "upload picture error!");
            return wrapResult(false, e.getMessage());
        }
    }

    /*处理上传的图片*/
    private Map<String, String> uploadPic(HttpServletRequest request, User user) throws IOException, FileUploadException {
        Map<String, String> result = Maps.newHashMap();
        ParamChecker.assertCondition(ServletFileUpload.isMultipartContent(request), SYSTEM_ERROR);
        //开始处理上传请求
        PicUploadUtil.ensureFileFolderIsNotNull(user.getUsername());
        DiskFileItemFactory dff = new DiskFileItemFactory();
        dff.setRepository(new File(PicUploadUtil.getFileDir(user.getUsername())));
        dff.setSizeThreshold(1024000);
        ServletFileUpload sfu = new ServletFileUpload(dff);
        FileItemIterator fii = null;
        fii = sfu.getItemIterator(request);
        while (fii.hasNext()) {
            FileItemStream fis = fii.next();
            if (!fis.isFormField() && fis.getName().length() > 0) {
                String fileName = fis.getName();
                if (!PicUploadUtil.isValidPicType(fileName)) {
                    throw new RuntimeException("unsupported file type!");
                }
                String realFileName = PicUploadUtil.parseFileName(user.getUsername(), fileName);
                String url = PicUploadUtil.getFilePath(user.getUsername(), realFileName);

                BufferedInputStream in = new BufferedInputStream(fis.openStream());//获得文件输入流
                FileOutputStream a = new FileOutputStream(new File(url));
                BufferedOutputStream output = new BufferedOutputStream(a);
                Streams.copy(in, output, true);//开始把文件写到你指定的上传文件夹
                LoggerUtils.info(LOGGER, "write file:", realFileName, " success");
                result.put(fis.getFieldName(), realFileName);
            }
        }
        return result;
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
            ParamChecker.assertCondition(PicUploadUtil.isValidPicType(temp[1]), "unsupported file type!");
            response.setContentType("image/" + temp[1]);
            OutputStream out = response.getOutputStream();
            fis = new FileInputStream(new File(PicUploadUtil.getFilePath(username, fileName)));
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

}
