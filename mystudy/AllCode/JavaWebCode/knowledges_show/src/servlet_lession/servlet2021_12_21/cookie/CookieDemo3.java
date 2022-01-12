package servlet_lession.servlet2021_12_21.cookie;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname CookieDemo2
 * @Description TODO
 * @Date 2021/12/20 23:52
 * @Created by DELL
 */
@WebServlet("/cookie/demo3")
public class CookieDemo3 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        //1.创建cookie对象
        Cookie c1 = new Cookie("msg","hello");
        Cookie c2 = new Cookie("asd","hsdlo");
        Cookie c3 = new Cookie("csa","heffo");
        //2.发送cookie
        response.addCookie(c1);
        response.addCookie(c2);
        response.addCookie(c3);
    }
}
