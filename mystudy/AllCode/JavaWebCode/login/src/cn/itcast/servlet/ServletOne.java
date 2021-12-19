package cn.itcast.servlet;

import cn.itcast.dao.UserDao;
import cn.itcast.domain.User;
import org.apache.commons.beanutils.BeanUtils;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/ServletOne")
public class ServletOne extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.设置编码
        response.setContentType("text/html;charset=utf-8");
        //2.获取请求参数   并封装成对象
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//        User loginUser = new User(1, username, password);


        //A使用BeanUtils
        //A.1获取参数的map集合
        Map<String, String[]> parameterMap = request.getParameterMap();
        //A.2创建一个User对象
        User loginUser = new User();
        //A.3使用BeanUtils封装
        try {
            BeanUtils.populate(loginUser,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        //访问数据库并获取对象
        UserDao dao = new UserDao();
        User user = dao.login(loginUser);
        //判断是否是我们的用户
        if (user== null) {
            System.out.println("您不是我们的用户");
            request.getRequestDispatcher("ServletTwo").forward(request,response);
        } else {
            //请求成功
            System.out.println("亲爱的用户XXX，您好");
            //建立共享资源，因为之后的页面要显示用户的名字
            request.setAttribute("username",user.getUsername());
            request.getRequestDispatcher("ServletThree").forward(request,response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
