package filter;


import entity.Student;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Classname FilterDemo
 * @Description TODO
 * @Date 2021/12/21 22:51
 * @Created by DELL
 */
@WebFilter("/*")
public class FilterDemo extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        req.setCharacterEncoding("utf-8");
        res.setContentType("text/html;charset=utf-8");
        //不拦截的资源
        String[] urls = {
                "/login/loginSec.jsp",
                "/login/loginThird.jsp",
                "/login/login.jsp",
                "/login/fail.jsp",
                "/css/*",
                "/loginThird"
        };
        String url = req.getRequestURL().toString();
        System.out.println(url);
        for (int i = 0; i < urls.length; i++) {
            if (url.contains(urls[i])){
                //放行不进行拦截的资源
                chain.doFilter(req,res);
                //如果不return，代码执行完后，还进入过滤器继续执行。
                return;
            }
        }
        //通过session判断用户是否已登录
        HttpSession session = req.getSession();
        Student user = (Student) session.getAttribute("user");
        if (user != null) {
            System.out.println("登陆未过期，免登陆");
            chain.doFilter(req,res);
        }else {
            System.out.println("登陆过期，请重新登陆");
            req.setAttribute("message", "请重新登陆");
            req.getRequestDispatcher("/login/loginThird.jsp").forward(req, res);
        }

    }
}
