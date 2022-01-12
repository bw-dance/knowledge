package web.dao;

import web.utils.JDBCUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterDao {
    public static void main(String[] args) {
        Connection con  = null;
        PreparedStatement pstmt = null;
        try {
            con=JDBCUtils.getConnection();
            String sql = "insert into user values(?,?,?,?,?,?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,"123456");
            pstmt.setString(2,"张三");
            pstmt.setInt(3,0);
            pstmt.setString(4,"2012-12-22");
            pstmt.setString(5,"333");
            pstmt.setString(6,"333");
            int i = pstmt.executeUpdate();
            System.out.println(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
