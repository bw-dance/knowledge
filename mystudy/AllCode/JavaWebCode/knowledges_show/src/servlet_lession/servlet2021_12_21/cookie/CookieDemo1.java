package servlet_lession.servlet2021_12_21.cookie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname CookieDemo1
 * @Description TODO
 * @Date 2021/12/20 23:51
 * @Created by DELL
 */
@WebServlet("/cookie/demo1")
public class CookieDemo1 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        //1.创建cookie对象
        Cookie c = new Cookie("msg", "hello");
        //2.发送cookie
        response.addCookie(c);
    }
}
