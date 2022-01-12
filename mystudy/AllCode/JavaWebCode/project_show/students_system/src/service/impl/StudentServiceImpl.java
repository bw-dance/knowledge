package service.impl;


import dao.IStudentDao;
import dao.impl.StudentDaoImpl;
import entity.Student;
import service.IStudentService;

import java.util.List;


/**
 * @Classname StudentServiceImpl
 * @Description TODO
 * @Date 2021/12/20 11:23
 * @Created by DELL
 */
public class StudentServiceImpl implements IStudentService {
    private static final IStudentDao studentDao = new StudentDaoImpl();

    @Override
    public Student login(String username, String password) {
        Student student = studentDao.login(username, password);
        return student;

    }

    @Override
    public List<Student> getAllStundents() {
        return studentDao.getAllStudent();
    }

    @Override
    public int addStudent(Student student) {
        return studentDao.addStudent(student);
    }
}
