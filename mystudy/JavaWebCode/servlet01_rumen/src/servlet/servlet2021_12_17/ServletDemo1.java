package servlet.servlet2021_12_17;

import javax.servlet.*;
import java.io.IOException;

/**
 * @Classname Servlet01
 * @Description TODO
 * @Date 2021/12/17 14:47
 * @Created by DELL
 */
public class ServletDemo1 implements Servlet {



    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("init............");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        String contextPath = servletRequest.getServletContext().getContextPath();
        System.out.println(contextPath);

        System.out.println("Hello Servlet");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
        System.out.println("destroy.................");
    }
}
