package entity;

import java.util.Date;

/**
 * @Classname Comment
 * @Description TODO
 * @Date 2021/12/24 16:42
 * @Created by DELL
 */
public class Comment {
    private  Integer cid;
    private Integer uid;
    private Integer aid;
    private String username;
    private String comment;
    private Date date;

    public Comment() {
    }

    public Comment(Integer cid, Integer uid, Integer aid, String username, String comment, Date date) {
        this.cid = cid;
        this.uid = uid;
        this.aid = aid;
        this.username = username;
        this.comment = comment;
        this.date = date;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        return cid.equals(comment.cid);
    }

    @Override
    public int hashCode() {
        return cid.hashCode();
    }

    @Override
    public String toString() {
        return "Comment{" +
                "cid=" + cid +
                ", uid=" + uid +
                ", aid=" + aid +
                ", username='" + username + '\'' +
                ", comment='" + comment + '\'' +
                ", date=" + date +
                '}';
    }
}
