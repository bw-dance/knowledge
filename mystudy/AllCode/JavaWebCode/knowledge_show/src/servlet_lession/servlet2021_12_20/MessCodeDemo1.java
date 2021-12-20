package servlet_lession.servlet2021_12_20;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


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
        //出现乱码问题，没有解决。使用req.getParameter("")获取参数设置utf-8可以解决
//        BufferedReader reader = req.getReader();
//        String line = null;
//        while ((line=reader.readLine())!=null){
//            System.out.println(line);
//        }

//        System.out.println("post请求");
        req.setCharacterEncoding("UTF-8");
        System.out.println(req.getParameter("username"));
        System.out.println(req.getParameter("password"));
    }
}
