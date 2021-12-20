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
@WebServlet("/forward/demo6")
public class ForwardDemo6 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object name = req.getAttribute("name");
        System.out.println(String.valueOf(name));
    }
}
