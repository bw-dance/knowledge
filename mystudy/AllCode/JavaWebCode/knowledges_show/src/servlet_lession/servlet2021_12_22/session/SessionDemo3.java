package servlet_lession.servlet2021_12_22.session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * @Classname SessionDemo1
 * @Description TODO
 * @Date 2021/12/21 21:06
 * @Created by DELL
 */
@WebServlet("/session/demo3")
public class SessionDemo3 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //使用session共享数据
        //1.获取session对象
        HttpSession session = req.getSession();
        session.setAttribute("name","Lily");
        //打印session
        System.out.println(session);
        //2. 设置cookie过期时间。
        Cookie cookie =new Cookie("JSESSIONID",session.getId());
        cookie.setMaxAge(60*60);
        resp.addCookie(cookie);
    }
}
