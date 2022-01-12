package web.service;

import web.domain.Blog;
import web.domain.PageSearch;
import web.domain.User;

import java.util.ArrayList;
import java.util.List;

public interface UserService {
       //验证用户名是否存在
       public User findUserNameExit(String username);
       //创建用户
       public int registerUser(User user);
       //用户登陆判断
       public User loginUser(String username,String password);
       //分页查询
         //1.登陆者的
       public PageSearch searchPageContent(int page, int pageNumber, String idcard);
       //2.所有人的
          public PageSearch searchPageContent(int page, int pageNumber);
      //查看bolg
       public Blog lookBlog(String id);
     //更新blog
    boolean updateMyBlog(Blog updateBlog);
//删除blog
    boolean deleteBlog(String id);
//添加blog
     int addBlog(Blog blog);
//删除选择的blog
    boolean deleteSelectBlog(String...blogids);

}
