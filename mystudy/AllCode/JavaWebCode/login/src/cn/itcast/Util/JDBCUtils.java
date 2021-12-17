package cn.itcast.Util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCUtils {
    private static final String connectionUrl = "jdbc:mysql://localhost:3306/day14?useUnicode=true&characterEncoding=UTF-8&userSSL=false&serverTimezone=GMT%2B8";
    private static final String user = "root";
    private static final String passWord = "121156";
    //定义成员数据源
    private static ComboPooledDataSource ds;

    static {
        //数据源初始化

        try {
            ds = new ComboPooledDataSource();
            ds.setDriverClass("com.mysql.cj.jdbc.Driver");
            ds.setJdbcUrl(connectionUrl);
            ds.setUser(user);
            ds.setPassword(passWord);

            //初始连接数
            ds.setInitialPoolSize(5);
            //最大链接个数
            ds.setMaxPoolSize(20);
            //等等还有许多
        } catch (PropertyVetoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void closeAll(ResultSet rs, PreparedStatement pstmt, Connection con) {
//        for(AutoCloseable closeable:closeables) {
//            if(closeable!=null) {
//                try {
//                    closeable.close();
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (con != null) {
            try {
               con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


}
