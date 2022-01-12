package dao.impl;

import com.sun.org.glassfish.gmbal.ManagedObject;
import dao.IStudentDao;
import entity.Student;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import utils.DRUIDDateSource;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * @Classname StudentDaoImpl
 * @Description TODO
 * @Date 2021/12/20 12:37
 * @Created by DELL
 */
public class StudentDaoImpl implements IStudentDao {
    private JdbcTemplate template = new JdbcTemplate(DRUIDDateSource.getDataSource());


    @Override
    public Student login(String username, String password) {
        String sql = "SELECT * FROM tb_students_info WHERE username=? AND password=?";
        Student student = template.queryForObject(sql,new BeanPropertyRowMapper<Student>(Student.class),username,password);
        System.out.println(student);
        return student;
    }

    public List<Student> getAllStudent() {
        String sql = "SELECT * FROM tb_students_info ";

        List<Student> query = template.query(sql, new BeanPropertyRowMapper<Student>(Student.class));
        System.out.println(query);
        return query;
    }

    @Override
    public int addStudent(Student student) {
        String sql = "INSERT INTO tb_students_info VALUE (?,?,?,?,?,?,?,?,?,?)";
        Date loginDate = new Date(Date.from(student.getLogin_date().atZone(ZoneOffset.ofHours(8)).toInstant()).getTime());
        int update = template.update(sql,
                null,
                student.getUsername(),
                student.getPassword(),
                student.getName(),
                student.getDeptId(),
                student.getAge(), student.getSex(), student.getHeight(), student.getMoney(), loginDate);
        return update;
    }
}
