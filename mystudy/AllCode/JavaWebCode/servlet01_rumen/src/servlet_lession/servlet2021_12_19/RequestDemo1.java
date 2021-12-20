package servlet_lession.servlet2021_12_19;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

/**
 * @Classname RequestDemo1
 * @Description TODO
 * @Date 2021/12/19 13:17
 * @Created by DELL
 */
@WebServlet("/req/demo1")
public class RequestDemo1 extends HttpServlet {


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求行数据
//        System.out.println("username:"+req.getParameter("username"));
//        System.out.println("password:"+req.getParameter("password"));
//        System.out.println("参数集合");
//        Map<String, String[]> parameterMap = req.getParameterMap();
//        for (String key:parameterMap.keySet()
//             ) {
//            String[] paramers = parameterMap.get(key);
//            System.out.println(Arrays.toString(paramers));
//        }
//        System.out.println("请求方法"+req.getMethod());
//        System.out.println("请求虚拟路径"+req.getContextPath());
//        System.out.println("请求接口路径"+req.getServletPath());
//        System.out.println("请求URI"+req.getRequestURI());
//        System.out.println("请求URL"+req.getRequestURL().toString());
//        //获取请求头数据
//        System.out.println("获取浏览器版本"+req.getHeader("user-agent"));
//        System.out.println("获取请求地址" + req.getHeader("referer"));
//        Enumeration<String> headerNames = req.getHeaderNames();
//        System.out.println(headerNames);
        //获取请求体数据
        //获取请求消息体，即请求参数
        //1.获取字符流
        BufferedReader bf =req.getReader();
        //2.获取数据
        String line= null;
        //line=参数，如果为空，这说明没有数据了
        while ((line=bf.readLine())!=null){
            System.out.println(line);
        }

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

}
