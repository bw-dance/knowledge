package servlet_lession.servlet2021_12_17;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname ServletDemo3
 * @Description TODO
 * @Date 2021/12/17 20:45
 * @Created by DELL
 */
@WebServlet(urlPatterns = "/demo3",initParams = {
        @WebInitParam(name = "username",value = "tom"),
        @WebInitParam(name = "password",value = "123456")
})
public class ServletDemo3 extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        System.out.println(this.getInitParameter("username"));
        System.out.println(this.getInitParameter("password"));
    }

}
