package servlet_lession.servlet2021_12_17;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname ServletDemo2
 * @Description TODO
 * @Date 2021/12/17 16:23
 * @Created by DELL
 */
@WebServlet(urlPatterns = "/demo2")
public class ServletDemo2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get«Î«Û");
        System.out.println(req.getParameter("username"));
        System.out.println(req.getParameter("password"));
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post«Î«Û");
        System.out.println(req.getParameter("username"));
        System.out.println(req.getParameter("password"));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        String contextPath = req.getContextPath();
        System.out.println(servletPath);
        System.out.println(contextPath);
    }

}
