package study.mybatis;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import study.mybatis.mapper.UserMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Classname UserTest
 * @Description TODO
 * @Date 2022/3/7 15:01
 * @Created by zhq
 */
public class UserTest {
    @Test
    public void UserInsert() throws IOException {
        InputStream config = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(config);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.insertUser();

    }


}
