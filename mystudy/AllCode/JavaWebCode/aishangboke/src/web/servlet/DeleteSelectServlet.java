package web.servlet;

import web.service.UserService;
import web.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/deleteSelectServlet")
public class DeleteSelectServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
request.setCharacterEncoding("utf-8");
        String[] blogId = request.getParameterValues("blogId");
        String source = request.getParameter("source");
        UserService userService = new UserServiceImpl();
        boolean b = userService.deleteSelectBlog(blogId);
        if (b){
            //删除成功，重回博客页面
            System.out.println("那里跳转来的");
            String requestURI = request.getRequestURI();
            System.out.println(requestURI);
            if(source.equals("myBlog")){
                response.sendRedirect("/aishangboke/pageSearchServlet");
            }else {
                response.sendRedirect("/aishangboke/pageSearchAllServlet");
            }


        }else {
            System.out.println("删除失败");
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
this.doPost(request,response);
    }
}
