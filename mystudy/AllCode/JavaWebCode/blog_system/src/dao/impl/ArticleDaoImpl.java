package dao.impl;

import com.alibaba.druid.pool.DruidDataSource;
import dao.IArticleDao;
import entity.Article;
import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import utils.DRUIDDateSource;

/**
 * @Classname ArticleDaoImpl
 * @Description TODO
 * @Date 2021/12/24 16:52
 * @Created by DELL
 */
public class ArticleDaoImpl implements IArticleDao {
    private JdbcTemplate template = new JdbcTemplate(DRUIDDateSource.getDataSource());
    @Test
    public void  testDataSource(){
        int id = 1;
        String sql = "SELECT * FROM bl_article WHERE aid=?";
        Article student = template.queryForObject(sql,new BeanPropertyRowMapper<Article>(Article.class),id);
        System.out.println(student);
    }
}
