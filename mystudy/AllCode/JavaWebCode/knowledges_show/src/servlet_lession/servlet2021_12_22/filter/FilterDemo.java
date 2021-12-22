//package servlet_lession.servlet2021_12_22.filter;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import java.io.IOException;
//
//@WebFilter("/*")
//public class FilterDemo implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//
//        //放行前，通常执行request的相关代码
//        System.out.println("请求前");
//        filterChain.doFilter(servletRequest,servletResponse);
//        //放行后，通常执行response的相关代码
//        System.out.println("请求后");
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}