package servlet_ready_lession.servlet2021_12_21.response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname ResponseDemo1
 * @Description TODO
 * @Date 2021/12/20 21:04
 * @Created by DELL
 */
@WebServlet("/reps/demo")
public class ResponseDemo1 extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException {
        //方式1：设置重定向
        //1.1设置状态码
        response.setStatus(302);
        //1.2设置响应头location
        response.setHeader("location", "/Response/servletDemo2");
        //方式2：设置重定向（简单）
        response.sendRedirect("/servlet/response/response.jsp");
    }

}
