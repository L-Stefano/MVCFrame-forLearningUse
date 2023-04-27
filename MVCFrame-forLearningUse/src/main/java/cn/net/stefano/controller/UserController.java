package cn.net.stefano.controller;

import cn.net.stefano.annotation.GetMapping;
import cn.net.stefano.bean.User;
import cn.net.stefano.framework.ModelAndView;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


public class UserController
{
    @GetMapping(pathstr = "/hello")
    public ModelAndView testHello()
    {
        User u=new User("testname");
        return new ModelAndView(u, "/WEB-INF/test.jsp");
    }
}
