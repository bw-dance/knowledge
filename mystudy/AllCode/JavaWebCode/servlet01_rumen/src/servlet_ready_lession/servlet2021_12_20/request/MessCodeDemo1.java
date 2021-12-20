package servlet_ready_lession.servlet2021_12_20.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Classname MessCodeDemo
 * @Description TODO
 * @Date 2021/12/19 21:08
 * @Created by DELL
 */
@WebServlet("/mess-code/demo1")
public class MessCodeDemo1 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getParameter("username"));
        System.out.println(req.getParameter("password"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post«Î«Û");
        req.setCharacterEncoding("UTF-8");
        System.out.println(req.getParameter("username"));
        System.out.println(req.getParameter("password"));
    }
}
