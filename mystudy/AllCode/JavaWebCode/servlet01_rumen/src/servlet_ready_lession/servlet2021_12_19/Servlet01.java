package servlet_ready_lession.servlet2021_12_19;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname Servlet01
 * @Description TODO
 * @Date 2021/12/17 21:06
 * @Created by DELL
 */
@WebServlet("/ready/demo01")
public class Servlet01 extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String msg = "hello Servlet";
        request.setAttribute("msg",msg);
        request.getRequestDispatcher("/hello.jsp").forward(request,response);
    }
}
