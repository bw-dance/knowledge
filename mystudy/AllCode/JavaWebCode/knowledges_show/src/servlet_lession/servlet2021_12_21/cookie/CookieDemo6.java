package servlet_lession.servlet2021_12_21.cookie;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @Classname Cook
 * @Description TODO
 * @Date 2021/12/21 13:17
 * @Created by DELL
 */
@WebServlet("/cookie/demo6")
public class CookieDemo6 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            System.out.println(URLDecoder.decode(cookies[i].getValue(),"utf-8"));
        }
        Cookie cookie = new Cookie("name", URLEncoder.encode("ÕÅ Èý ","utf-8"));
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
    }
}
