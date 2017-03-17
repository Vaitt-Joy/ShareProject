package com.vz.share.controller;

import com.vz.share.entity.UserInfo;
import com.vz.share.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by VVz on 2017/3/15.
 *
 * @des 用户的操作类
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@Validated UserInfo userInfo, BindingResult result, Model model, HttpRequestHandlerServlet request) {
        ModelAndView mv = new ModelAndView();

    }

    public List<UserInfo> getUserList() {
        return null;
    }

    public void addUser(UserInfo userInfo) {

    }

    public void update(UserInfo userInfo) {

    }

    public void deleteUser(UserInfo userInfo) {

    }
}
