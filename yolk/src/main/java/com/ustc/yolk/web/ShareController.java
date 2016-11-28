package com.ustc.yolk.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2016/11/28.
 */
@Controller
@RequestMapping(value = "/share")
public class ShareController extends BaseController {

    @RequestMapping(value = "upload.json")
    @ResponseBody
    public String share() {
        return wrapSuccessResult();
    }
}
