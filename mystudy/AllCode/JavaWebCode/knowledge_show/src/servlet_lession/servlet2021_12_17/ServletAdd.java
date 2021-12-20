package servlet_lession.servlet2021_12_17;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;


/**
 * @Classname ServletAdd
 * @Description TODO
 * @Date 2021/12/17 15:24
 * @Created by DELL
 */
@WebServlet("/blog/add")
public class ServletAdd implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse)  {
        System.out.println("增加一条数据");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
