package web.service.impl;

import org.junit.Test;
import web.dao.UserDao;
import web.dao.impl.UserDaoImpl;
import web.domain.Blog;
import web.domain.PageSearch;
import web.domain.User;
import web.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    //查找用户名是否存在
    private UserDao userDao = new UserDaoImpl();
    @Override
    public User findUserNameExit(String username) {

        return userDao.findUserExit(username);
    }

    @Override
    public int registerUser(User user) {

        return  userDao.registerUser(user);
    }

    @Override
    public User loginUser(String username, String password) {
        return userDao.loginUser(username,password);
    }

    @Override
    /**
     *    private int page;//当前页数
     *     private int pageNumber;//每页有多少条数据
     *     private List<Map<String, Object>> blogs;//返回的数据
     *     private  int totalContent;//一共有多少条数据
     *     private  int totalPage; //一共有多少页
     */
    public PageSearch searchPageContent(int page, int pageNumber, String idcard) {
        int startPage =(page-1)*5;//开始的页面编号
        System.out.println("当前的startPage"+page);
        int totalContent = userDao.contentNumber(idcard); //查询一共有多少条当前对象的博客
        List<Blog> blogs = userDao.searchPageContent(startPage, pageNumber, idcard);//存放博客的容器
        int totalPage=totalContent%5==0?totalContent / pageNumber:totalContent / pageNumber+1;//一共有多少页
        PageSearch pageSearch = new PageSearch(page,pageNumber,blogs,totalContent,totalPage);
        return pageSearch;
    }
    public PageSearch searchPageContent(int page, int pageNumber) {
        int startPage =(page-1)*5;//开始的页面编号
        System.out.println("当前的startPage"+page);
        int totalContent = userDao.contentNumber(); //查询一共有多少条当前对象的博客
        List<Blog> blogs = userDao.searchPageContent(startPage, pageNumber);//存放博客的容器
        int totalPage=totalContent%5==0?totalContent / pageNumber:totalContent / pageNumber+1;//一共有多少页
        PageSearch pageSearch = new PageSearch(page,pageNumber,blogs,totalContent,totalPage);
        return pageSearch;
    }

    @Override
    public Blog lookBlog(String id) {
        return userDao.lookBlog(Integer.parseInt(id));
    }

    @Override
    public boolean updateMyBlog(Blog updateBlog) {
     int i = userDao.updateMyBlog(updateBlog);
     if(i>0){
         return true;
     }else {
         return false;
     }
    }

    @Override
    public boolean deleteBlog(String id) {
        int i = userDao.deleteBlog(Integer.parseInt(id));
        if(i>0){
            return true;
        }else {
            return false;
        }

    }

    @Override
    public int addBlog(Blog blog) {
       int i= userDao.addBlog(blog);
       return i;

    }

    @Override
    public boolean deleteSelectBlog(String... blogids) {
        int i=0;
        for (int a= 0;a<blogids.length;a++) {
            i = userDao.deleteBlog(Integer.parseInt(blogids[a]));
            if (i==0){
                return false;
            }
        }
        if(i==1){
            return true;
        }
        return  false;

    }




}
