package com.ustc.yolk.web;

import com.ustc.yolk.model.ShareContent;
import com.ustc.yolk.model.SingleContent;
import com.ustc.yolk.model.User;
import com.ustc.yolk.service.ShareContentService;
import com.ustc.yolk.utils.RSAUtil;
import com.ustc.yolk.utils.common.ParamChecker;
import com.ustc.yolk.utils.log.LoggerUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/11/28.
 */
@Controller
@RequestMapping(value = "/content")
public class ShareController extends BaseController {
    //日志
    private final static Logger LOGGER = LoggerFactory.getLogger(PictureController.class);

    @Autowired
    private ShareContentService shareContentService;

    @RequestMapping(value = "upload.json")
    @ResponseBody
    public String upload() {
        return wrapSuccessResult();
    }

    @RequestMapping(value = "share.json")
    @ResponseBody
    public String toShare(HttpServletRequest servletRequest, @RequestParam(value = "id", required = false) String id) {
        try {
            User user = getUserFromRequest(servletRequest);
            shareContentService.share(Long.valueOf(id), user);
            //将加密后的id返回给前端 前端组成访问地址
            return wrapSuccessResult("id", RSAUtil.encrypt(id));
        } catch (Exception e) {
            LoggerUtils.error(LOGGER, e, "share content error!");
            return wrapResult(false, e.getMessage());
        }
    }

    /**
     * 查询分享的内容
     *
     * @param id 加密后的id
     */
    @RequestMapping(value = "query.json")
    @ResponseBody
    public String querysharedContent(HttpServletRequest servletRequest, @RequestParam(value = "id", required = false) String id) {
        try {
            ParamChecker.notBlank("id", id);
            //查询
            ShareContent shareContent = shareContentService.queryById(Long.valueOf(RSAUtil.decrypt(id)));
            if (!shareContent.isPublic0()) {
                //非公开的 抛异常
                throw new RuntimeException("illegal shared content status!");
            }
            return wrapSuccessResult("shareContent", hidePicName(shareContent));
        } catch (Exception e) {
            LoggerUtils.error(LOGGER, e, "share content error!");
            return wrapResult(false, e.getMessage());
        }
    }

    /*对图片名做加密*/
    private ShareContent hidePicName(ShareContent shareContent) throws UnsupportedEncodingException {
        for (SingleContent singleContent : shareContent.getContents()) {
            if (StringUtils.isNotBlank(singleContent.getPicName())) {
                singleContent.setPicName(URLEncoder.encode(RSAUtil.encrypt(singleContent.getPicName()), "utf-8"));
            }
        }
        return shareContent;
    }
}
