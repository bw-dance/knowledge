package web.servlet;

import com.mysql.cj.Session;
import com.mysql.cj.xdevapi.SessionImpl;
import web.domain.Blog;
import web.domain.MessageState;
import web.domain.PageSearch;
import web.domain.User;
import web.service.UserService;
import web.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Date;

@WebServlet("/loginUserServlet")
public class LoginUserServlet  extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       request.setCharacterEncoding("utf-8");
        //设置响应消息体的编码格式
       response.setContentType("Text/html;charset=utf-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String checkCode = request.getParameter("checkCode");//验证码
        System.out.println("获取的验证码"+checkCode);
        HttpSession session = request.getSession();
        //账户名密码的临时存储，主要用于验证码或者密码输入错误时，表单数据的临时保存
        session.setAttribute("username",username);
        session.setAttribute("password",password);
        //获取验证码
        String checkCode1 = (String)session.getAttribute("checkCode");
//        System.out.println("服务器生成的验证码"+checkCode1);
//        System.out.println(checkCode.equalsIgnoreCase(checkCode1));
        //先判断验证码是否填写错误
         if (checkCode==null||checkCode.equals("")||checkCode1.equalsIgnoreCase(checkCode)==false){
             //验证码为空或不正确
             MessageState messageState = new MessageState("验证码错误，请重新输入","","");
             request.setAttribute("messageState", messageState);

             //验证码出错误，跳转到登陆页面
             request.getRequestDispatcher("/view/login.jsp").forward(request,response);
         }else{
             //访问数据库，判断是否有该用户的信息
             UserService userService = new UserServiceImpl();
             //获取登录的用户的信息
             User user = userService.loginUser(username, password);
             //判断结果
             if (user==null){
                 //登陆失败
                 MessageState messageState = new MessageState("账户名或密码错误","","");
                 request.setAttribute("messageStatePass",messageState);
                 request.getRequestDispatcher("view/login.jsp").forward(request,response);

             }else{
                 System.out.println("登录成功");
                 //登录成功，
                 // 1.将登陆者存入session，便于之后的判断使用
                 session.removeAttribute("username");
                 session.removeAttribute("password");
                 session.removeAttribute("checkCode");
                 session.setAttribute("user",user);
                 //设置cookie  用于再次登陆的判断。
                 //登陆账户的用户名密码
                 Cookie usernameCookie= new Cookie("username",user.getUsername());
                 Cookie passwordCookie = new Cookie("password",user.getPassword());


                 //登录账户的session  session是服务器储存，但是当浏览器关闭，服务器不关闭时，两次的session不是同一个。所以需要通过cookie保留原来的session数据
                 Cookie userCookie = new Cookie("JSESSIONID",session.getId());
                 //存活一小时
                 usernameCookie.setMaxAge(60*60);
                 passwordCookie.setMaxAge(60*60);
                 userCookie.setMaxAge(60*60);
                 userCookie.setPath("/");

                 response.addCookie(usernameCookie);
                 response.addCookie(passwordCookie);
                 response.addCookie(userCookie);

                 //2.重定向，跳转到分页查询接口
                 Date date = new Date();
                 //插入时间参数的原因：假如我对页面的数据进行增删改之后返回上一级，如果没有参数或者参数一样，那么就会返回增删改之前的页面
                 //date是变化的，所以每次返回都会重新刷新页面

                 response.sendRedirect("/aishangboke/pageSearchServlet?date="+date.getTime()+"");

             }

         }

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
