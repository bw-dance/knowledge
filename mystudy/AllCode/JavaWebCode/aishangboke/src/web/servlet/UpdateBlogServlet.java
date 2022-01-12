package web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
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
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/updateBlogServlet")
public class UpdateBlogServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        Date date = new Date();
        Blog updateBlog = new Blog();
        Map<String, String[]> parameterMap = request.getParameterMap();
        HttpSession session = request.getSession();
        User user =(User) session.getAttribute("user");
        try {
            BeanUtils.setProperty(updateBlog,"upDate",date);
            BeanUtils.populate(updateBlog,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        UserService userService = new UserServiceImpl();


        if (user.getIdcard().equals(updateBlog.getAuthor())){
            //更新的blog是我的bolg
            System.out.println("可以进行更新");
            boolean b = userService.updateMyBlog(updateBlog);
            if (b){
                System.out.println("跟新成功");
                //更新成功
                String encode = URLEncoder.encode("更新成功", "UTF-8");
                MessageState messageState = new MessageState(encode,"/aishangboke/lookBlogServlet?state=look&id="+updateBlog.getId(),"点击查看");
                request.setAttribute("messageState",messageState);
                System.out.println("更新数据");
                System.out.println(messageState.getJudge());
                System.out.println(messageState.getUrl());
                System.out.println(messageState.getUrlName());
                request.getRequestDispatcher("/view/success.jsp").forward(request,response);
            }else {
                //更新失败
                String encode = URLEncoder.encode("更新失败", "UTF-8");
                MessageState messageState = new MessageState(encode,"/aishangboke/servlet/pageSearchAllServlet","返回修改");
                request.setAttribute("messageState",messageState);
                request.getRequestDispatcher("/view/success.jsp").forward(request,response);
            }
        }



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
this.doPost(request,response);
    }
}
