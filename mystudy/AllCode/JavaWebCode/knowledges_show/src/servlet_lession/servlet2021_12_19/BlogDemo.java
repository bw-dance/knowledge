package servlet_lession.servlet2021_12_19;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @Classname BlogDemo
 * @Description TODO
 * @Date 2021/12/19 13:41
 * @Created by DELL
 */
@WebServlet("/blog")
public class BlogDemo extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ªÒ»°blog");
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ÃÌº”blog");
    }
}
