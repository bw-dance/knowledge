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
 * @Classname AllStudent
 * @Description TODO
 * @Date 2021/12/21 22:11
 * @Created by DELL
 */
@WebServlet("/all")
public class AllStudent extends HttpServlet {
    private static final IStudentService studentService = new StudentServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            List<Student> allStudents = studentService.getAllStundents();
            req.setAttribute("message", allStudents);
            req.getRequestDispatcher("/login/homepage.jsp").forward(req, resp);
    }
}
