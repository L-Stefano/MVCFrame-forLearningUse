package cn.net.stefano.framework;


import cn.net.stefano.annotation.GetMapping;
import cn.net.stefano.controller.UserController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * 只实现了Get相关Controller
 * 仅用于学习
 * by Stefano
 */
@WebServlet(urlPatterns = "/")
public class DispatcherServlet extends HttpServlet
{
    // 存储所有的Controller，通过在init中利用反射扫描所有的Controller类
    private Map<String, Dispatcher> controllers = new HashMap<>();

    // init由Servlet容器调用，先于其他任何方法
    // 用于扫描所有的Controller类
    @Override
    public void init() throws ServletException
    {
        super.init();

        // 获取所有具体Controller类
        // 另外可以通过将所有Controller类放在同一个包下，然后扫描该包下的所有类
        // 这里手动填写
        Class<?>[] controllerClasses = new Class<?>[]{
                UserController.class
        };
        for (Class<?> controllerClass : controllerClasses)
        {
            // 利用反射创建Controller实例
            Object instance;
            try
            {
                instance = controllerClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e)
            {
                throw new RuntimeException(e);
            }

            // 获取Controller类中所有的方法
            Method[] methods = controllerClass.getDeclaredMethods();
            for (Method method : methods)
            {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                if (getMapping != null)
                {
                    // 获取路径
                    String path = getMapping.pathstr();
                    // 创建Dispatcher实例
                    Dispatcher dispatcher = new Dispatcher();
                    dispatcher.setInstance(instance);
                    dispatcher.setMethod(method);
                    // 将路径与Controller实例绑定
                    controllers.put(path, dispatcher);
                }
            }
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        // 获取请求的项目路径
        String fullPath = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = fullPath.substring(contextPath.length());

        // 根据路径，获取负责该路径的Controller
        Dispatcher controller = controllers.get(path);
        if (controller == null)// 没有对应的Controller
        {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "没有对应"+path+"的Controller");

        } else// 有对应的Controller
        {
            try
            {
                ModelAndView mv = controller.handleRequest(req, resp);
                //将model中的数据放入request中
                req.setAttribute("model", mv.getModel());
                //转发至jsp
                req.getRequestDispatcher(mv.getViewName()).forward(req, resp);
            } catch (InvocationTargetException | IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
