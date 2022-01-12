package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Classname Student
 * @Description TODO
 * @Date 2021/12/20 11:22
 * @Created by DELL
 */
public class Student {
    private Long id;
    private String username;
    private String password;
    private String name;
    private Long deptId;
    private int age;
    private Boolean sex;
    private int height;
    private BigDecimal money;
    private LocalDateTime login_date;

    public Student() {
    }

    public Student(Long id, String username, String password, String name, Long deptId, int age, Boolean sex, int height, BigDecimal money, LocalDateTime login_date) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.deptId = deptId;
        this.age = age;
        this.sex = sex;
        this.height = height;
        this.money = money;
        this.login_date = login_date;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public LocalDateTime getLogin_date() {
        return login_date;
    }

    public void setLogin_date(LocalDateTime login_date) {
        this.login_date = login_date;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", deptId=" + deptId +
                ", age=" + age +
                ", sex=" + sex +
                ", height=" + height +
                ", money=" + money +
                ", login_date=" + login_date +
                '}';
    }
}
