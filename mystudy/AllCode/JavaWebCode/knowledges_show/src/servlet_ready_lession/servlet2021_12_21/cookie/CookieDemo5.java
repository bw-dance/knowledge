package servlet_ready_lession.servlet2021_12_21.cookie;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Classname CookieDemo4
 * @Description TODO
 * @Date 2021/12/21 8:56
 * @Created by DELL
 */
@WebServlet("/cookie/demo5")
public class CookieDemo5 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("name","TOM");
        cookie.setMaxAge(0);
        System.out.println(cookie.getPath());
        response.addCookie(cookie);
    }
}
