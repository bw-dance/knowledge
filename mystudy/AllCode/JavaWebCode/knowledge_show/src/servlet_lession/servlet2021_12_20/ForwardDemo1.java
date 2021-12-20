package servlet_lession.servlet2021_12_20;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname Forward
 * @Description TODO
 * @Date 2021/12/19 20:02
 * @Created by DELL
 */
@WebServlet("/forward/demo1")
public class ForwardDemo1 extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //方法一：创建对象进行转发
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/forward/demo2");
        requestDispatcher.forward(request,response);
        System.out.println("hello i am demo1");//转发后，当demo2执行完，后面的代码继续执行。
        //方法二：直接进行转发  注意：在同一个servlet中不能进行两次转发
        //request.getRequestDispatcher("/forward/demo3").forward(request,response);
    }
}
