package cn.net.stefano.framework;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class ModelAndView
{
    private Object model;
    private String viewName;

    public ModelAndView(Object model, String viewName)
    {
        this.model = model;
        this.viewName = viewName;
    }

    public ModelAndView()
    {
    }

    public void setViewName(String viewName)
    {
        this.viewName = viewName;
    }

    public String getViewName()
    {
        return viewName;
    }

    public void setModel(Object model)
    {
        this.model = model;
    }

    public Object getModel()
    {
        return model;
    }

}
