package servlet_ready_lession.servlet2021_12_20.request;

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
@WebServlet("/forward/demo7")
public class ForwardDemo7 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       req.setAttribute("name","Lily");
        req.getRequestDispatcher("/WEB-INF/forward01.jsp").forward(req,resp);
    }
}
