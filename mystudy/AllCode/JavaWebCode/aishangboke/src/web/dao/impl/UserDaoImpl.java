package web.dao.impl;


import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import web.dao.UserDao;
import web.domain.Blog;
import web.domain.User;
import web.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;


public class UserDaoImpl implements UserDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());


    /**
     * 查询新建立的用户名数据库中是否存在
     *
     * @param username
     * @return
     */
    @Override
    public User findUserExit(String username) {
        try {
            String sql = "select*from user where username=?";
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
            return user;
        } catch (DataAccessException e) {
            return null;
        }

    }

    /**
     * 注册账号
     *
     * @param user
     */
    @Override
    public int registerUser(User user) {
        String sql = "insert into user value(?,?,?,?,?,?)";
        int update = template.update(sql, user.getIdcard(), user.getName(), user.getGender(), user.getBirth(), user.getUsername(), user.getPassword());
        return update;

    }

    @Override
    public User loginUser(String username, String password) {
        try {
            String sql = "select*from user where username=? and password=?";
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username, password);
            return user;
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public int contentNumber(String idcard) {
        String sql = "select count(*) from blog where author=?";
        Integer integer = template.queryForObject(sql, Integer.class, idcard);

        return integer;
    }

    @Override
    public int contentNumber() {
        String sql = "select count(*) from blog";
        Integer integer = template.queryForObject(sql, Integer.class);
        return integer;
    }


    @Override

    public List<Blog> searchPageContent(int page, int pageNumber, String authorIdcard) {
        String sql = "select*from blog where author=? limit ?,?";
        List<Blog> blogs = template.query(sql, new BeanPropertyRowMapper<Blog>(Blog.class), authorIdcard, page, pageNumber);

        return blogs;
    }

    @Override
    public List<Blog> searchPageContent(int page, int pageNumber) {
        String sql = "select*from blog limit ?,?";
        List<Blog> blogs = template.query(sql, new BeanPropertyRowMapper<Blog>(Blog.class), page, pageNumber);

        return blogs;
    }

    //查看当前博客
    public Blog lookBlog(int id) {
        String sql = "select*from blog where id=?";
        Blog blog = template.queryForObject(sql, new BeanPropertyRowMapper<Blog>(Blog.class), id);

        return blog;
    }

    //修改博客
    @Override
    public int updateMyBlog(Blog blog) {
        String sql = "update blog set title=?,blog.update=?,author=?,content=? where id=?";
        int update = template.update(sql, blog.getTitle(), blog.getUpDate(), blog.getAuthor(), blog.getContent(), blog.getId());

        return update;
    }

    //删除博客
    @Override
    public int deleteBlog(int id) {
        String sql = "delete from blog where id=?";
        int update = template.update(sql, id);
        return update;
    }


    @Override
    public int addBlog(Blog blog) {
        String sql = "insert into blog value(?,?,?,?,?)";
//        int update = template.update(sql, blog.getId(), blog.getTitle(), blog.getUpDate(), blog.getAuthor(), blog.getUpDate());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                int a = 1;
                PreparedStatement pstmt = connection.prepareStatement(sql);
                //设置参数
                pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setObject(a, blog.getId());
                pstmt.setString(++a, blog.getTitle());
                //时间转换
                Date upDate = blog.getUpDate();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = formatter.format(upDate);
                //设置时间
                pstmt.setString(++a, format);
                //设置author
                pstmt.setString(++a, blog.getAuthor());
                //设置内容
                pstmt.setString(++a, blog.getContent());
                return pstmt;
            }
        }, keyHolder);
        if (update > 0) {

            return keyHolder.getKey().intValue();
        } else {
            return update;
        }


    }

    @Test
    public void a() {

        JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

//yyyy-MM-dd HH:mm:ss  类型的时间
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String current = formatter.format(date);
        System.out.println("yyyy-MM-dd HH:mm:ss" + current);

        String sql = "insert into datesTime value(?)";
        int update = template.update(sql, current);
        System.out.println(update);

        //yyyy-MM-dd  类型的时间
        Date dates = new Date();
        SimpleDateFormat formatters = new SimpleDateFormat("yyyy-MM-dd");
        String currents = formatters.format(dates);
        System.out.println("yyyy-MM-dd" + currents);

        String sql1 = "insert into dates value(?)";
        int update1 = template.update(sql1, currents);
        System.out.println(update1);

        Date date1 = new Date();
        String sql2 = "insert into datesTime value(?)";
        int update2 = template.update(sql2, date1);
        System.out.println(update2);

    }

    @Test
    public void b() {
        System.out.println("张浩琦");
    }


}
