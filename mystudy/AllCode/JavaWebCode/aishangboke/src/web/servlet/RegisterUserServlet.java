package web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import sun.plugin2.message.Message;
import web.domain.MessageState;
import web.domain.User;
import web.service.UserService;
import web.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//注册页面
@WebServlet("/registerUserServlet")
public class RegisterUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //获取参数列表的集合
        Map<String,String[]> parameterMap =request.getParameterMap();
        //获取出生日期 并进行日期格式的转换
        String birth = request.getParameter("birth");
        System.out.println(birth);
        //创建User对象
        User registerUser = new User();
        //使用BeanUtils进行封装
        try {
          //birth属性单独设置
          ConvertUtils.register(new DateLocaleConverter(),Date.class);
          BeanUtils.setProperty(registerUser,"birth",birth);
          BeanUtils.populate(registerUser,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println("传入的对象为");
        System.out.println(registerUser);
        //向数据库中插入数据
        UserService userService = new UserServiceImpl();
        int i = userService.registerUser(registerUser);
        if (i>0){
            //注册成功，跳转页面
            //页面内容
            MessageState messageState = new MessageState("创建成功","/aishangboke/view/login.jsp","点击登录");

            request.setAttribute("messageState",messageState);
            request.setAttribute("messageState",messageState);
            request.setAttribute("register","register");
           request.getRequestDispatcher("/view/success.jsp").forward(request,response);
        }else{
            //注册失败，跳转页面
            //页面内容
            MessageState messageState = new MessageState("创建失败","/aishangboke/view/login.jsp","重新创建");
            request.setAttribute("messageState",messageState);
            request.setAttribute("register","register");
            request.getRequestDispatcher("/view/success.jsp").forward(request,response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
this.doPost(request,response);
    }
}
