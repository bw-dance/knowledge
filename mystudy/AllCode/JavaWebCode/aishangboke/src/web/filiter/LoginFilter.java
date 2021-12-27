//package web.filiter;
//
//import com.sun.deploy.net.HttpRequest;
//import web.domain.MessageState;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//
//
////登陆验证的过滤器
//@WebFilter("/*")
//public class LoginFilter implements Filter {
//    @Override
//    public void destroy() {
//    }
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
//        req.setCharacterEncoding("utf-8");        // HttpServlet 实现了Request的接口    我们要通过request获取资源的路径，需要用到request，所以强转
//        resp.setContentType("text/html;charset=utf-8");
//        HttpServletRequest request = (HttpServletRequest) req;
//
////1.获取资源的请求路径
//        String requestURI = request.getRequestURI();
//        System.out.println(requestURI);
//        System.out.println(request.getContextPath());
//        //如果包含login.jsp或者loginUserServlet等与登录有关的资源。放行。  当然，还要排除样式，js等资源
//        String[] source ={"/login.jsp","/loginUserServlet","/checkCodeServlet",
//                "/css/","/js/","/frame/","/view/register.jsp","/registerUserServlet",
//        "/cookieJudgeServlet"};
//        boolean judgeSource=false;
//        for (int i = 0; i < source.length; i++) {
//            if (requestURI.contains(source[i])){
//                judgeSource=true;
//            }
//        }
//        if(judgeSource){
//            chain.doFilter(req, resp);
//        }else{
//            HttpSession session = request.getSession();
//            Object user = session.getAttribute("user");
//            Cookie[] cookies = request.getCookies();
//            System.out.println("cookie的长度为");
//            System.out.println(cookies.length);
//
//            if (user!=null||cookies.length>=2){
//                chain.doFilter(req, resp);
//            }else {
//                MessageState messageState = new MessageState("您尚未登陆","","");
//                request.setAttribute("messageStatePass",messageState);
//                request.getRequestDispatcher("/view/login.jsp").forward(request,resp);
//            }
//        }
//
//
//
//    }
//
//    public void init(FilterConfig config) throws ServletException {
//
//    }
//
//}
