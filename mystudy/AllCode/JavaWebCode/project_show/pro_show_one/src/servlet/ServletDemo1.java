package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname ServletDemo1
 * @Description TODO
 * @Date 2021/12/21 20:15
 * @Created by DELL
 */
@WebServlet("/one/demo1")
public class ServletDemo1 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("cookie1");
        Cookie[] cookies = req.getCookies();
        if (cookies!=null){
            for (int i = 0; i <cookies.length ; i++) {
                System.out.println("cookie1:"+cookies[i].getValue());
            }
        }
        Cookie cookie = new Cookie("name","cookieOne");
        cookie.setPath("/");
        resp.addCookie(cookie);
    }
}
