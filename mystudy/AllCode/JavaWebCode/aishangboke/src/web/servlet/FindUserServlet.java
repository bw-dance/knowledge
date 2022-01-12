package web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebServlet("/findUserServlet")
public class FindUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        //进行数据库访问
        UserService judgeUser = new UserServiceImpl();
        User userNameExit = judgeUser.findUserNameExit(username);
        Map<String,Object> map = new HashMap<String, Object>();
        if(userNameExit==null){
            map.put("userExit",true);
            map.put("msg","用户名成立");
        }else {
            map.put("userExit",false);
            map.put("msg","该用户名已存在，请更改其他用户名");
        }
        ObjectMapper mapper  = new ObjectMapper();
        mapper.writeValue(response.getWriter(),map);
        System.out.println("用户名验证完毕");


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
this.doPost(request,response);
    }
}
