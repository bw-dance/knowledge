package servlet_lession.servlet2021_12_21.response;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Classname ResponseDemo3
 * @Description TODO
 * @Date 2021/12/20 22:08
 * @Created by DELL
 */
@WebServlet("/reqs/demo3")
public class ResponseDemo3 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //方式2 设置编码格式
        resp.setContentType("text/html;charset=utf-8");
        ServletOutputStream outputStream = resp.getOutputStream();
        outputStream.write("hello world".getBytes());
    }
}
