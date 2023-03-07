package com.ibm.webdev.app.jersey.start.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
/**
 * 自定义 servlet 过滤器全局设置跨域请求，与 {@link InfoController#postInfo} 完全同理，只是提升为全局设置
 * * 标准 Servlet 过滤器，实现 javax.servlet.Filter 接口，并重现它的 3 个方法
 * * filterName：表示过滤器名称，可以不写
 * * value：配置请求过滤的规则，如 "/*" 表示过滤所有请求，包括静态资源，如 "/user/*" 表示 /user 开头的所有请求
 */

@WebFilter(filterName = "MyCorsFilter", value = {"/*"})
public class MyCorsFilter implements Filter {
 
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("com.ibm.webdev.app.jersey.start.filter.MyCorsFilter -- 系统启动...");
    }
 
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        //转为 HttpServletRequest 输出请求路径
        HttpServletRequest request = (HttpServletRequest) req;
        
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
 
        filterChain.doFilter(req, res);
    }
 
    @Override
    public void destroy() {
        System.out.println("com.ibm.webdev.app.jersey.start.filter.MyCorsFilter -- 系统关闭...");
    }
}
