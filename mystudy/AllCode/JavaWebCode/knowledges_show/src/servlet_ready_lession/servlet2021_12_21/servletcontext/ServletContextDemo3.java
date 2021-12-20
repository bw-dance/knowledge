package servlet_ready_lession.servlet2021_12_21.servletcontext;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @Classname ServletContext
 * @Description TODO
 * @Date 2021/12/20 22:26
 * @Created by DELL
 */
@WebServlet("/context/demo3")
public class ServletContextDemo3 extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object username = request.getServletContext().getAttribute("username");
        System.out.println(username);
        PrintWriter printWriter = response.getWriter();
        printWriter.println("hello i am"+username);
    }
}
