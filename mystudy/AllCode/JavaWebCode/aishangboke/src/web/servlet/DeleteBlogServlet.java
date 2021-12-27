package web.servlet;

import org.junit.Test;
import web.domain.MessageState;
import web.service.UserService;
import web.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/deleteBlogServlet")
public class DeleteBlogServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String id = request.getParameter("id");
        String page = request.getParameter("page");
        String now = request.getParameter("now");
        System.out.println("当前的页面是"+page);
        //链接数据库进行删除操作
        UserService userService = new UserServiceImpl();
        System.out.println("进行删除操作");
        boolean b = userService.deleteBlog(id);
        if (b){

            //删除成功
            System.out.println("删除成功");
//            //跳转到删除内容所在的页面
//            if (page==null||page.equals("")){
//                MessageState messageState = new MessageState("删除成功","/aishangboke/pageSearchServlet","查看我的博客");
//                request.setAttribute("messageState",messageState);
//                request.getRequestDispatcher("/view/success.jsp").forward(request,response);
//            }else {
//                request.setAttribute("page",page);
//                request.getRequestDispatcher("/pageSearchServlet").forward(request,response);
//            }

             //根据blog的来源跳转到相应页面。
         if (now.equals("allblog")){
             request.getRequestDispatcher("/pageSearchAllServlet").forward(request,response);
         }else{
             request.setAttribute("page",page);
             request.getRequestDispatcher("/pageSearchServlet").forward(request,response);
         }

        }else {
            //删除失败
            if (page==null||page.equals("")){
                MessageState messageState = new MessageState("删除失败","/aishangboke/lookBlogServlet?id="+id+"&state=look","返回原博客");
                request.setAttribute("messageState",messageState);
                request.getRequestDispatcher("/view/success.jsp").forward(request,response);
            }else {
                //删除失败
                System.out.println("删除失败");
                response.sendRedirect("/pageSearchServlet");
            }

        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
this.doPost(request,response);
    }


}
