package servlet_ready_lession.servlet2021_12_21.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @Classname FilterDemo
 * @Description TODO
 * @Date 2021/12/21 22:51
 * @Created by DELL
 */
@WebFilter("/filter/*")
public class FilterDemo2 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //放行前，通常执行request的相关代码
        System.out.println("i am filter2 begin");
        filterChain.doFilter(servletRequest,servletResponse);
        //放行后，通常执行response的相关代码
        System.out.println("i am filter2 over");
    }

    @Override
    public void destroy() {

    }
}
