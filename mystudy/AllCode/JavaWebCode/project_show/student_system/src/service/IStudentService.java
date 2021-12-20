package service;

import entity.Student;

import java.util.List;

/**
 * @Classname StudentService
 * @Description TODO
 * @Date 2021/12/20 11:23
 * @Created by DELL
 */
public interface IStudentService {

    Student login(String username, String password);

    List<Student> getAllStundents();

    int addStudent(Student student);
}
