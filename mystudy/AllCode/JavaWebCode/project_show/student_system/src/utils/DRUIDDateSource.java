package utils;




import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DRUIDDateSource {
    //1.定义成员变量 DateSource
    private static DataSource ds;

    static {
        try {
            //1.加载配置文件
            Properties pro = new Properties();
            pro.load(DRUIDDateSource.class.getClassLoader().getResourceAsStream("druid.properties"));
            ds = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws Exception {
        Connection connection = null;
        try {
            connection = ds.getConnection();
            return connection;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    //获取链接池的方法
    public static DataSource getDataSource() {
        return ds;
    }

    //关闭数据库连接的封装，通过动态传参，将rs，pstmt，con按照顺序传进来
    public static void closeAll(AutoCloseable... closeables) {
        for (AutoCloseable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

}