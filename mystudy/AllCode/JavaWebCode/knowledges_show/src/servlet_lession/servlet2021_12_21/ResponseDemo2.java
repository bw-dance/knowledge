package servlet_lession.servlet2021_12_21;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Classname ResponseDemo2
 * @Description TODO
 * @Date 2021/12/20 22:02
 * @Created by DELL
 */

@WebServlet("/reqs/demo2")
public class ResponseDemo2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //解决乱码
        //方式一：
        resp.setContentType("text/html;charset=utf-8");
        //方式二：
        //1.1获取流对象之前，把流的默认编码“ISO-8859-1”设置为：utf-8
//        resp.setCharacterEncoding("utf-8");
        //1.2告诉浏览器，服务器发送的数据的编码，建议浏览器使用此编码
//        resp.setHeader("content-type","text/html;charset=utf-8");
        //获取字符输出流
        PrintWriter writer = resp.getWriter();
        //输出数据
        writer.write("hello world");
        //输出html格式数据
        writer.write("<h1>hello world<h1>");
        //输出中文数据
        writer.write("你好，我的世界");//中文乱码
    }
}

