package com.fly.controller;

import com.fly.pojo.UserInfo;
import com.fly.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    UserInfoService userInfoService;
    /**
     * @param name
     * @param password
     * @return request
     */
    @RequestMapping("/login")
    public String login(String name, String password, HttpServletRequest request) {
        UserInfo userInfo=userInfoService.findByUsername(name);
        if (userInfo.getName().equals(name) && userInfo.getPassword().equals(password)) {
            //登录成功
            System.out.println("登录成功");
           //将登录信息存储到session域中
            request.getSession().setAttribute("name", name);
            return "redirect:/account/findAll";
        } else {
            System.out.println("登录失败");
            //进入登录页面
            return "redirect:/login.jsp";
        }
    }
}