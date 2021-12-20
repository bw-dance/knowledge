package servlet_lession.servlet2021_12_20;

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
@WebServlet("/forward/demo5")
public class ForwardDemo5 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("name","Tom");
        //转发给servlet
        req.getRequestDispatcher("/forward/demo6").forward(req,resp);
        //转发给jsp页面
//        req.getRequestDispatcher("/forward/forward01.jsp").forward(req,resp);
    }
}
