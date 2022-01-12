package web.servlet;

import web.domain.User;
import web.service.UserService;
import web.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/cookieJudgeServlet")
public class CookieJudgeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //存放cookie信息
        Map<String,String> cookieMap = new HashMap<>();
        UserService userService = new UserServiceImpl();
          Cookie[] cookies = request.getCookies();

          //获取cookie
          if (cookies!=null&&cookies.length!=0){
              System.out.println("有cookie信息，判断该用户");
              for (Cookie cookie:cookies){
                  if (cookie.getName().contains("username")||cookie.getName().contains("password")){
                      cookieMap.put(cookie.getName(),cookie.getValue());
                  }

              }
              String username = cookieMap.get("username");
              String password = cookieMap.get("password");
              User user = userService.loginUser(username, password);

              if (user!=null){
                  //验证成功，直接登陆查看blog
                  System.out.println("用户信息已经存在，可以直接登陆了");
                  response.sendRedirect("/aishangboke/pageSearchServlet");
              }
              if (user==null){
                  //验证失败，重新登陆查看blog
                  response.sendRedirect("/aishangboke/view/login.jsp");
              }
          } else{
              System.out.println("没有cookie信息，进行登陆");
              response.sendRedirect("/aishangboke/view/login.jsp");
          }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
this.doPost(request,response);
    }
}
