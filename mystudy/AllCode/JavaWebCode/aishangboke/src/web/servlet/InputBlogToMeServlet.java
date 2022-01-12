package web.servlet;

import web.domain.User;
import web.service.UserService;
import web.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/inputBlogToMeServlet")
public class InputBlogToMeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        //进行数据库访问，判断此博客是否是登陆者自己的博客
        UserService userService = new UserServiceImpl();
        HttpSession session = request.getSession();
        User user =(User)session.getAttribute("user");
//       boolean success= userService.inputBlog(id,user.getIdcard());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
this.doPost(request,response);
    }
}
