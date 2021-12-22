package dao;

import entity.Student;

import java.util.List;

/**
 * @Classname StudentService
 * @Description TODO
 * @Date 2021/12/20 11:23
 * @Created by DELL
 */
public interface IStudentDao {
    List<Student> getAllStudent();

    Student login(String username, String password);
    int  addStudent(Student student);
}
