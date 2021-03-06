package servlet;

import entity.Student;
import service.IStudentService;
import service.impl.StudentServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


/**
 * @Classname Login
 * @Description TODO
 * @Date 2021/12/20 11:25
 * @Created by DELL
 */
@WebServlet("/login")
public class LoginSession extends HttpServlet {
    private static final IStudentService studentService = new StudentServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException {
        //1.设置编码
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        HttpSession session = request.getSession();
        Student user = (Student) session.getAttribute("user");
        if (user != null) {
            request.setAttribute("message", user);
            request.getRequestDispatcher("/login/homepage.jsp").forward(request, response);
            return;
        }
        //2.获取请求参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if ("".equals(username) || "".equals(password) || username == null || password == null) {
            request.setAttribute("message", "fail");
            request.getRequestDispatcher("/login/fail.jsp").forward(request, response);
            return;
        }
        //3.查询数据库
        Student student = studentService.login(username, password);
        if (student == null) {
            request.setAttribute("message", "fail");
            request.getRequestDispatcher("/login/fail.jsp").forward(request, response);
        } else {
            session.setAttribute("user", user);
//            //获取当前学生
            request.setAttribute("message", student);
            request.getRequestDispatcher("/login/homepage.jsp").forward(request, response);
        }
    }

}
