package web.servlet;

import web.domain.PageSearch;
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
import java.util.Map;

@WebServlet("/pageSearchServlet")
public class PageSearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取登陆者的信息，session默认时Object对象，需要强制转换成User对象
       HttpSession session = request.getSession();


       User user = (User) session.getAttribute("user");
       UserService userService = new UserServiceImpl();

       //获取参数集合,并判断是否拥有参数page或者pagenumber，如果有跳转到相应的页面，如果没有，默认显示第一页
        //定义每页显示五条数据
        int pageNumber=5;
        boolean judge=false;
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String p:parameterMap.keySet()) {
            System.out.println("开始遍历");
            if (parameterMap.keySet().contains("page")) {
                judge = true;
                break;
            }
        }
        //判断后显示
        if(judge==true){
            String page = request.getParameter("page");
            PageSearch pageSearch = userService.searchPageContent(Integer.parseInt(page), pageNumber,user.getIdcard());
            request.setAttribute("pageSearch",pageSearch);
            request.getRequestDispatcher("/view/myblog.jsp").forward(request,response);
        }else {
            System.out.println("执行了");
            int page=0;
            if (page<=0){
                page=1;
            }
            System.out.println("登陆者的id为");
            System.out.println(user.getIdcard());
            PageSearch pageSearch = userService.searchPageContent(page, pageNumber,user.getIdcard());
            request.setAttribute("pageSearch",pageSearch);
            request.getRequestDispatcher("/view/myblog.jsp").forward(request,response);
        }




    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
this.doPost(request,response);
    }
}
