package cn.itcast.dao;

import cn.itcast.Util.JDBCUtils;
import cn.itcast.domain.User;

import java.sql.*;

/**
 * 操作数据库中user表的类
 */

/**
 * 登陆方法
 * 参数：只有用户名和密码
 * 返回：use包含用户的全部数据
 */
public class UserDao {
    public User login(User loginUser) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = JDBCUtils.getConnection();
            String sql = "select*from user where username=? and password=?";
            System.out.println(sql);
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, loginUser.getUsername());
            pstmt.setString(2, loginUser.getPassword());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
                return  user;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 因为此时我们需要关闭两个pstmt，传递两个pstmt参数，所以我们需要完善我们的工具类
        JDBCUtils.closeAll(rs, pstmt, con);

        }
        return null;
    }

}
