package servlet_lession.servlet2021_12_21.servletcontext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Classname ServletContextDemo4
 * @Description TODO
 * @Date 2021/12/20 22:37
 * @Created by DELL
 */
@WebServlet("/context/demo4")
public class ServletContextDemo4 extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext servletContext = request.getServletContext();
        //获取当前路径
        String realPath1 = servletContext.getRealPath("/");
        System.out.println(realPath1);
        //获取类路径下文件
        String realPathA = servletContext.getRealPath("/WEB-INF/classes/ConfigurationA");
        System.out.println(realPathA);
        //获取WEB-INF下文件
        String realPathB = servletContext.getRealPath("/WEB-INF/ConfigurationB");
        System.out.println(realPathB);
        //获取web下文件
        String realPathC= servletContext.getRealPath("/ConfigurationC");
        System.out.println(realPathC);
    }
}