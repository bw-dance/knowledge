package servlet_ready_lession.servlet2021_12_21.cookie;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname CookieDemo7
 * @Description TODO
 * @Date 2021/12/21 13:27
 * @Created by DELL
 */
@WebServlet("/cookie/demo7")
public class CookieDemo7 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        //1.创建cookie对象
        Cookie c1 = new Cookie("name","Tom");
        Cookie c2 = new Cookie("age","18");
        Cookie c3 = new Cookie("sex","man");
        c1.setPath("/servlet");
        c2.setPath("/servlet");
        c3.setPath("/servlet");
//2.发送cookie
        response.addCookie(c1);
        response.addCookie(c2);
        response.addCookie(c3);
    }
}
