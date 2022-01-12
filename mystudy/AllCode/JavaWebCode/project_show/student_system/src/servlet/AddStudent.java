package servlet;

import entity.Student;
import org.apache.commons.beanutils.BeanUtils;
import service.IStudentService;
import service.impl.StudentServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Classname AddStundent
 * @Description TODO
 * @Date 2021/12/20 14:41
 * @Created by DELL
 */
@WebServlet("/add")
public class AddStudent extends HttpServlet {
    private static final IStudentService studentService = new StudentServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.…Ë÷√±‡¬Î
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        Map<String, String[]> parameterMap = request.getParameterMap();
        Student student = new Student();
        try {
            BeanUtils.populate(student, parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        student.setDeptId(18L);
        student.setMoney(BigDecimal.valueOf(100.00));
        student.setSex(false);
        student.setLogin_date(LocalDateTime.now());
        int i = studentService.addStudent(student);
        if (i <= 0) {
            request.setAttribute("message", "≤Â»Î ß∞‹");
            request.getRequestDispatcher("/login/fail.jsp").forward(request, response);
        } else {
            List<Student> allStudents = studentService.getAllStundents();
            request.setAttribute("message", allStudents);
            request.getRequestDispatcher("/login/homepage.jsp").forward(request, response);
        }
    }
}
