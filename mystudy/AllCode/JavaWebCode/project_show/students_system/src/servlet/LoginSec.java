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
@WebServlet("/loginSec")
public class LoginSec extends HttpServlet {
    private static final IStudentService studentService = new StudentServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException {
        //1.设置编码
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        HttpSession session = request.getSession();
        Student user = (Student) session.getAttribute("user");
        if (user != null) {
            response.sendRedirect("/students_system/all");
            return;
        }
        //2.获取请求参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if ("".equals(username) || "".equals(password) || username == null || password == null) {
            request.setAttribute("message", "失败:用户名和密码不能为null");
            request.getRequestDispatcher("/login/login.jsp").forward(request, response);
            return;
        }
        //3.查询数据库
        Student student = studentService.login(username, password);
        if (student == null) {
            request.setAttribute("message", "失败:用户不存在");
            request.getRequestDispatcher("/login/login.jsp").forward(request, response);
        } else {
            session.setAttribute("user",student);
            response.sendRedirect("/students_system/all");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException {
        this.doPost(request, response);
    }

}
