package servlet_ready_lession.servlet2021_12_21.response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Classname redirect1
 * @Description TODO
 * @Date 2021/12/20 21:38
 * @Created by DELL
 */
@WebServlet("/redirect/demo2")
public class RedirectDemo2 extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //重定向不支持数据共享
        Object msg = req.getAttribute("message");
        System.out.println(msg);
        String message = "hello i am demo2";
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }
}
