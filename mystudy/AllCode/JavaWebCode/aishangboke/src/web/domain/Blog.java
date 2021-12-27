package web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Blog {
    private Integer id;
    private String title;
    private String author;

    private Date upDate;
    private String content;

    public Blog() {

    }

    public  Blog(Integer id, String title, String author, Date upDate, String content) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.upDate = upDate;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUpDate() {
        return upDate;
    }

    public void setUpDate(Date upDate) {
        this.upDate = upDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", upDate=" + upDate + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
