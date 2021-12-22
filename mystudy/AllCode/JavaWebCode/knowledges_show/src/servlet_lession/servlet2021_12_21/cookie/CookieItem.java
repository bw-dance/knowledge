package servlet_lession.servlet2021_12_21.cookie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @Classname CookieItem
 * @Description TODO
 * @Date 2021/12/21 20:59
 * @Created by DELL
 */
@WebServlet("/cookie/visit")
public class CookieItem extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置响应消息体的编码格式
        response.setContentType("text/html;charset=utf-8");
        //1、判断是否有cookie  先获取所有的cookie、
        Cookie[] cookies = request.getCookies();
        //1.1设置一个关于是否有lastname的cookie判断
        boolean flag = false;
        //2.遍历cookie数组
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies
            ) {
                //3.获取cookie的名称
                String name = cookie.getName();
                //4.判断名称是否是：lastname
                if ("lastTime".equals(name)) {
                    //有该cookie 表示不是第一次访问
                    flag = true;
                    //响应数据
                    //获取cookie的数据
                    String value = cookie.getValue();
                    System.out.println("解码前" + value);
                    //URL解码
                    value = URLDecoder.decode(value, "utf-8");
                    System.out.println("解码后" + value);
                    response.getWriter().write("<h1>欢迎回来，您上次的访问时间为" + value + "</h1>");//此处为中文消息，需要设置响应消息体


                    //设置cookie的值，现在的值
                    //获取当前时间的字符串，重新设置cookie的值，重新发送
                    Date date = new Date();
                    System.out.println("修改前" + date);
                    //设置时间的格式，默认的为美国的时间格式
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH：mm：ss");//设置日期时间的格式
                    //设置时区，以下三种方式，任选其一。
//                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                    sdf.setTimeZone(TimeZone.getTimeZone("Etc/GMT-8"));
                    String str_date = sdf.format(date);
                    System.out.println("编码前" + str_date);
                    //URL编码
                    str_date = URLEncoder.encode(str_date, "utf-8");
                    System.out.println("编码后" + str_date);
                    //设置cookie
                    cookie.setValue(str_date);
                    //设置cookie的存活时间.存活一个月
                    cookie.setMaxAge(60 * 60 * 24 * 30);
                    //发送cookie
                    response.addCookie(cookie);

                    //找到名字为lastname的cookie之后，就不要循环了
                    break;
                }
            }
        }
        if (cookies == null || cookies.length == 0 || flag == false) {
            Date date = new Date();
            //设置时间的格式，默认的为美国的时间格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年mm月dd日  HH：mm：ss");//设置日期时间的格式
            //设置时区，以下三种方式，任选其一。
//                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            sdf.setTimeZone(TimeZone.getTimeZone("Etc/GMT-8"));
            //将时间生成字符串
            String str_date = sdf.format(date);
            //URL编码
            str_date = URLEncoder.encode(str_date, "utf-8");
            System.out.println("编码后" + str_date);
            //设置cookie
            Cookie cookie = new Cookie("lastTime", str_date);
            cookie.setValue(str_date);
            //设置cookie的存活时间.存活一个月
            cookie.setMaxAge(60 * 60 * 24 * 30);
            //发送cookie
            response.addCookie(cookie);
            //响应数据
            //获取cookie的数据
            String value = cookie.getValue();
            //URL解码
            value = URLDecoder.decode(value, "utf-8");
            System.out.println("解码后" + value);
            response.getWriter().write("<h1>你好，欢迎您首次访问</h1>" + value);//此处为中文消息，需要设置响应消息体
        }
    }

}
