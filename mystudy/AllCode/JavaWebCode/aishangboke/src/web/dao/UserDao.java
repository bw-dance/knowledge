package web.dao;

import web.domain.Blog;
import web.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface UserDao {
    public User findUserExit(String username);
    public int registerUser(User user);
    public User loginUser(String username,String password);
    //分页查询
    public List<Blog> searchPageContent(int page, int pageNumber, String idcard);
    public List<Blog> searchPageContent(int page, int pageNumber);


    public  int contentNumber(String idcard);

    public  int contentNumber();

    public  Blog lookBlog(int id);

    public  int updateMyBlog(Blog blog);

    int deleteBlog(int parseInt);

    int addBlog(Blog blog);



}
