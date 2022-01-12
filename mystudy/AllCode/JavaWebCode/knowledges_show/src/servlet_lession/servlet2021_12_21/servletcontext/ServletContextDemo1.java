package servlet_lession.servlet2021_12_21.servletcontext;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

import java.io.IOException;


/**
 * @Classname ServletContext
 * @Description TODO
 * @Date 2021/12/20 22:26
 * @Created by DELL
 */
@WebServlet("/context/demo1")
public class ServletContextDemo1 extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext servletContext = request.getServletContext();
        ServletContext servletContext1 = this.getServletContext();
        System.out.println(request==this);//false
        System.out.println(servletContext == servletContext1);//true
        System.out.println(servletContext);
        String file = "a.jpg";
        String mimeType = servletContext.getMimeType(file);
        System.out.println(mimeType);
    }
}
