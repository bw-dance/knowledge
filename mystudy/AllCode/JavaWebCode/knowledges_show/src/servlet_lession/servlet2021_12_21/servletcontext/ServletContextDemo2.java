package servlet_lession.servlet2021_12_21.servletcontext;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @Classname ServletContext
 * @Description TODO
 * @Date 2021/12/20 22:26
 * @Created by DELL
 */
@WebServlet("/context/demo2")
public class ServletContextDemo2 extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      request.getServletContext().setAttribute("username","tom");
      response.sendRedirect(request.getContextPath()+"/context/demo3");
    }
}
