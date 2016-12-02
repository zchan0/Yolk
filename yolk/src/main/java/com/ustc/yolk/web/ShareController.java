package com.ustc.yolk.web;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ustc.yolk.model.ShareContent;
import com.ustc.yolk.model.SingleContent;
import com.ustc.yolk.model.User;
import com.ustc.yolk.service.ShareContentService;
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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/28.
 */
@Controller
@RequestMapping(value = "/content")
public class ShareController extends BaseController {
    //日志
    private final static Logger LOGGER = LoggerFactory.getLogger(ShareController.class);

    @Autowired
    private ShareContentService shareContentService;

    /*在列表页分享自己发布的内容*/
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

    /*发布内容 默认都是非public的*/
    @RequestMapping(value = "publish.json")
    @ResponseBody
    public String publish(HttpServletRequest servletRequest, @RequestParam(value = "textContent", required = false) String textContent) {
        try {
            User user = getUserFromRequest(servletRequest);
            Map<String, String> textContents = null;
            try {
                textContents = JSON.parseObject(textContent, HashMap.class);
            } catch (Exception e) {
                throw new RuntimeException("illegal textContent!");
            }
            if (textContent == null) {
                textContents = Maps.newHashMap();
            }
            //上传图片
            Map<String, String> pics = uploadPic(servletRequest, user);
            ParamChecker.assertCondition(textContents.size() != 0 || pics.size() != 0, "empty content!");
            LoggerUtils.debug(LOGGER, "发布了文字:", textContents);
            LoggerUtils.debug(LOGGER, "发布了图片", pics);
            //对图片和文字做merge
            List<SingleContent> contents = Lists.newArrayList();
            for (Map.Entry<String, String> entry : textContents.entrySet()) {
                String picName = pics.get(entry.getKey());
                String text = entry.getValue();
                ParamChecker.notBlank(text, "empty text content!");
                pics.remove(entry.getKey());
                contents.add(new SingleContent(picName, text));
            }
            //未被merge图片的单独处理
            for (Map.Entry<String, String> entry : pics.entrySet()) {
                String picName = pics.get(entry.getKey());
                contents.add(new SingleContent(picName, null));
            }

            ShareContent shareContent = new ShareContent();
            shareContent.setContents(contents);
            shareContent.setSharedByUsername(user.getUsername());
            shareContent.setPublic0(false);
            shareContentService.add(shareContent);
            return wrapSuccessResult();
        } catch (Exception e) {
            LoggerUtils.error(LOGGER, e, "share content error!");
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

    /**
     * 查询自己上传的内容
     */
    @RequestMapping(value = "batchquery.json")
    @ResponseBody
    public String queryMyContent(HttpServletRequest servletRequest, @RequestParam(value = "start", required = false) String start,
                                 @RequestParam(value = "pagesize", required = false) String pageSize) {
        try {
            User user = getUserFromRequest(servletRequest);
            ParamChecker.notBlank("start", start);
            ParamChecker.notBlank("pagesize", pageSize);
            List<ShareContent> contents = shareContentService.queryMyRecentContents(user.getUsername(), Integer.valueOf(start), Integer.valueOf(pageSize));
            for (ShareContent content : contents) {
                hidePicName(content);
            }
            return wrapSuccessResult("myContents", contents);
        } catch (Exception e) {
            LoggerUtils.error(LOGGER, e, "query my content error!");
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
