package com.ustc.yolk.web;

import com.ustc.yolk.model.User;
import com.ustc.yolk.utils.PicUploadUtil;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

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
