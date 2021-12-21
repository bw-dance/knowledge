package servlet_ready_lession.servlet2021_12_21.cookie;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname CookieDemo4
 * @Description TODO
 * @Date 2021/12/21 8:56
 * @Created by DELL
 */
@WebServlet("/cookie/demo4")
public class CookieDemo4 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        //什么都没写，但是request的请求头里面保存着之前cookie返回的数据
    }
}
