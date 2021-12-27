package web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import web.domain.Blog;
import web.domain.User;
import web.service.UserService;
import web.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/lookBlogServlet")
public class LookBlogServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String id = request.getParameter("id");
        String state = request.getParameter("state");
        String now  =request.getParameter("now");

        //访问数据库
        UserService userService = new UserServiceImpl();


            if (state.equals("change")){
                Blog blog = userService.lookBlog(id);
                if (blog!=null){
                    //将当前blog的信息和进行的操作添加到request域中
                    request.setAttribute("blog",blog);
                    request.setAttribute("movement","change");
                    request.getRequestDispatcher("/view/writeblog.jsp").forward(request,response);
                }

            } else if (state.equals("look")) {
                Blog blog = userService.lookBlog(id);
                //将当前blog的信息和进行的操作添加到request域中
                if (blog!=null){
                    request.setAttribute("blog",blog);
                    request.setAttribute("movement","look");
                    request.setAttribute("now",now);
                    request.getRequestDispatcher("/view/writeblog.jsp").forward(request,response);
                }
            } else if (state.equals("write")){

                request.setAttribute("movement","write");
                request.getRequestDispatcher("/view/writeblog.jsp").forward(request,response);
            }




    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
this.doPost(request,response);
    }
}
