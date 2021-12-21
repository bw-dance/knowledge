package servlet_ready_lession.servlet2021_12_21.cookie;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname CookieDemo8
 * @Description TODO
 * @Date 2021/12/21 13:45
 * @Created by DELL
 */

@WebServlet("/cookie-v/demo9")
public class CookieDemo9 extends HttpServlet {
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

