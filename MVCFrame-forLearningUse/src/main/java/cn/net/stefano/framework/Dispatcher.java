package cn.net.stefano.framework;

import jakarta.servlet.http.*;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Dispatcher
{
    private Object instance;// Controller实例
    private Method method;// Controller方法

    public Object getInstance()
    {
        return instance;
    }

    public void setInstance(Object instance)
    {
        this.instance = instance;
    }

    public Method getMethod()
    {
        return method;
    }

    public void setMethod(Method method)
    {
        this.method = method;
    }

    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws InvocationTargetException, IllegalAccessException
    {
        Object[] args = parseArgs(req, resp);//解析参数
        ModelAndView result = (ModelAndView) method.invoke(instance, args);//调用Controller方法

        return result;
    }

    public Object[] parseArgs(HttpServletRequest req, HttpServletResponse resp)
    {
        Class<?>[] parameterTypes = method.getParameterTypes();//所有的参数类型
        Object[] args = new Object[parameterTypes.length];//所有的参数值
        //遍历所有参数，根据参数类型，从req中获取对应的值，并填充到args中
        for (int i = 0; i < args.length; i++)
        {
            Class<?> parameterType = parameterTypes[i];
            if (parameterType == HttpServletRequest.class)
            {
                args[i] = req;
            } else if (parameterType == HttpServletResponse.class)
            {
                args[i] = resp;
            } else if (parameterType == HttpSession.class)
            {
                args[i] = req.getSession();
            } else
            {
                throw new RuntimeException("Missing handler for type: " + parameterType);
            }
        }
        return args;
    }
}
