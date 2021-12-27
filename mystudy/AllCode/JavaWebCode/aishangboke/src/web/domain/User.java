package web.domain;

import java.util.Date;

public class User {
    private String idcard;
    private String name;
    private int gender;
    private Date birth;
    private  String username;
    private  String password;

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }



    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
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

    @Override
    public String toString() {
        return "user{" +
                "idcard='" + idcard + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", birth=" + birth +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
