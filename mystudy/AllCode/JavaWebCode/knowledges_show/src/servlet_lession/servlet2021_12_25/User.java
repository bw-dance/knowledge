package servlet_lession.servlet2021_12_25;

import java.util.Date;

/**
 * @Classname User
 * @Description TODO
 * @Date 2021/12/25 15:05
 * @Created by DELL
 */
public class User {
    private String name;
    private int age;
    private Date birthday;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
