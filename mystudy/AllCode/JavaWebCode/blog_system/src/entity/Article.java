package entity;

import java.util.Date;

/**
 * @Classname Article
 * @Description TODO
 * @Date 2021/12/24 16:34
 * @Created by DELL
 */
public class Article {
    private  Integer aid;
    private String title;
    private String text;
    private Date date;
    private String keyWord;
    private Integer likeCount;
    private Integer uid;

    public Article() {
    }

    public Article(Integer aid, String title, String text, Date date, String keyWord, Integer likeCount, Integer uid) {
        this.aid = aid;
        this.title = title;
        this.text = text;
        this.date = date;
        this.keyWord = keyWord;
        this.likeCount = likeCount;
        this.uid = uid;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        return aid.equals(article.aid);
    }

    @Override
    public int hashCode() {
        return aid.hashCode();
    }

    @Override
    public String toString() {
        return "Article{" +
                "aid=" + aid +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", keyWord='" + keyWord + '\'' +
                ", likeCount=" + likeCount +
                ", uid=" + uid +
                '}';
    }
}
