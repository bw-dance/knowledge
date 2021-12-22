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
        String[] urls = {
              "/login/loginSec.jsp",
                "/login/loginThird.jsp",
                "/login/login.jsp",
                "/login/fail.jsp",
                "/css/*",
                "/loginThird"
        };
        String url = req.getRequestURL().toString();
        for (int i = 0; i < urls.length; i++) {
            if (urls[i].contains(url)){
                chain.doFilter(req,res);
                return;
            }
        }
        HttpSession session = req.getSession();
        Student user = (Student) session.getAttribute("user");
        if (user != null) {
            System.out.println("µÇÂ½Î´¹ýÆÚ£¬ÃâµÇÂ½");
            chain.doFilter(req,res);
        }else {
            System.out.println("µÇÂ½¹ýÆÚ£¬ÇëÖØÐÂµÇÂ½");
            req.setAttribute("message", "ÇëÖØÐÂµÇÂ½");
            req.getRequestDispatcher("/login/loginThird.jsp").forward(req, res);
        }

    }
}
