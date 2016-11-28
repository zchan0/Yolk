package com.ustc.yolk.web;

import com.ustc.yolk.model.ShareContent;
import com.ustc.yolk.model.User;
import com.ustc.yolk.service.ShareContentService;
import com.ustc.yolk.utils.common.ParamChecker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2016/11/28.
 */
@Controller
@RequestMapping(value = "/content")
public class ShareController extends BaseController {

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
        User user = getUserFromRequest(servletRequest);
        ShareContent shareContent = shareContentService.queryById(Long.valueOf(id));
        ParamChecker.notNull(shareContent, "illegal content id!");
        //比较分享内容和当前登录用户是否一致
        ParamChecker.assertCondition(StringUtils.equals(shareContent.getSharedByUsername(), user.getUsername()), "illegal user!");
        return wrapSuccessResult();
    }

}
