package servlet_ready_lession.servlet2021_12_21.cookie;

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
@WebServlet("/cookie/demo8")
public class CookieDemo8 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        //3.ªÒ»°cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                String name = c.getName();
                String value = c.getValue();
                System.out.println(name + ":" + value);   //msg:hello
            }
        }
    }
}
