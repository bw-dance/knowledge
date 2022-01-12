package web.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

public class JDBCUtils {
    //1.定义成员变量 DateSource
    private  static DataSource ds;

    static {

        try {
            //1.加载配置文件
            Properties pro = new Properties();
            pro.load(JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties"));
            ds = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  static Connection getConnection() throws  Exception{
        return ds.getConnection();
    }
    //获取链接池的方法
    public  static  DataSource getDataSource(){
        return  ds;
    }



}
