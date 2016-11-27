package com.ustc.yolk.web;

import com.ustc.yolk.model.User;
import com.ustc.yolk.service.UserService;
import com.ustc.yolk.utils.common.ParamChecker;
import com.ustc.yolk.utils.log.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2016/11/27.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {

    //日志
    private final static Logger LOGGER = LoggerFactory.getLogger(PictureController.class);

    /*用户相关的服务*/
    @Autowired
    private UserService userService;

    @RequestMapping(value = "login.json")
    @ResponseBody
    public String login(HttpServletRequest req,
                        @RequestParam(value = "username", required = false) String username, @RequestParam(value = "password", required = false) String password) {
        try {
            //参数校验
            ParamChecker.notBlank("username", username);
            ParamChecker.notBlank("password", password);

            //获取用户信息并且校验密码是否正确
            User user = userService.getUser(username);
            if (user == null || !user.checkPassword(password)) {
                throw new RuntimeException("username or password is wrong!");
            }
            //将用户登录信息放到session中
            super.login(user, req);
            return wrapResult(true, null);
        } catch (Exception e) {
            LoggerUtils.error(LOGGER, e, "login error!");
            return wrapResult(false, e.getMessage());
        }
    }

    @RequestMapping(value = "register.json")
    @ResponseBody
    public String register(HttpServletRequest req,
                           @RequestParam(value = "username", required = false) String username, @RequestParam(value = "password", required = false) String password) {
        try {
            //参数校验
            ParamChecker.notBlank("username", username);
            ParamChecker.notBlank("password", password);

            //获取用户信息并且校验密码是否正确
            User user = new User();
            user.setUsername(username);
            user.setPasswdWithEncrypt(password);
            userService.register(user);
            //注册后直接将用户登录信息放到session中
            login(user, req);
            return wrapResult(true, null);
        } catch (Exception e) {
            LoggerUtils.error(LOGGER, e, "register error!");
            return wrapResult(false, e.getMessage());
        }
    }

    @RequestMapping(value = "logout.json")
    @ResponseBody
    public String logout0(HttpServletRequest req) {
        try {
            super.logout(req);
            return wrapResult(true, null);
        } catch (Exception e) {
            LoggerUtils.error(LOGGER, e, "logout error!");
            return wrapResult(false, e.getMessage());
        }
    }
}
