package com.ustc.yolk.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ustc.yolk.utils.common.ParamChecker;
import com.ustc.yolk.utils.log.LoggerUtils;

/**
 * Created by Administrator on 2016/11/5.
 */
@Controller
@RequestMapping(value = "/pic")
public class PictureController extends BaseController {

    //日志
    private final static Logger LOGGER = LoggerFactory.getLogger(PictureController.class);

    @RequestMapping(value = "add.json")
    @ResponseBody
    public String addServerResource(HttpServletRequest req,
                                    @RequestParam(value = "username", required = false) String username) {
        try {
            ParamChecker.notBlank("username", username);
            ParamChecker.assertCondition(req instanceof MultipartHttpServletRequest, SYSTEM_ERROR);
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
            MultipartFile imgFile1 = multipartRequest.getFile("img");
            writeFile(imgFile1, username);
            return wrapResult(true, null);
        } catch (Exception e) {
            LoggerUtils.error(LOGGER, e, "upload picture error!");
            return wrapResult(false, e.getMessage());
        }
    }

}