package web.servlet;

import web.domain.Blog;
import web.domain.MessageState;
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
import java.util.Date;

@WebServlet("/addBlogServlet")
public class AddBlogServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        Date date = new Date();
        Blog blog =new Blog(null,title,user.getIdcard(),date,content);
        UserService userService = new UserServiceImpl();
       int  i = userService.addBlog(blog);
        if (i>0){
            //更新成功
            //重定向到查看blog页面
            MessageState messageState = new MessageState("发布成功","/aishangboke/lookBlogServlet?id="+i+"&state=look","查看博客");
            request.setAttribute("messageState",messageState);
            request.getRequestDispatcher("/view/success.jsp").forward(request,response);

        }else {
            request.setAttribute("result","发布失败，请重新发布");

        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
this.doPost(request,response);
    }
}
