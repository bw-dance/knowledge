package entity;

/**
 * @Classname User
 * @Description TODO
 * @Date 2021/12/24 16:39
 * @Created by DELL
 */
public class User {
    private Integer uid;
    private String account;
    private String password;
    private String name;
    private Boolean gender;
    private Integer fansNumber;
    private String question;
    private String answer;
    private String introduce;

    public User() {
    }

    public User(Integer uid, String account, String password, String name, Boolean gender, Integer fansNumber, String question, String answer, String introduce) {
        this.uid = uid;
        this.account = account;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.fansNumber = fansNumber;
        this.question = question;
        this.answer = answer;
        this.introduce = introduce;
    }



    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Integer getFansNumber() {
        return fansNumber;
    }

    public void setFansNumber(Integer fansNumber) {
        this.fansNumber = fansNumber;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }


    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}
