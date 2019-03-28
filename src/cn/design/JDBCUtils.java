package cn.design;

import java.sql.*;

public class JDBCUtils {
    public static Connection getConnection() throws SQLException,
            ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/address_book?useSSL=true";
        String username = "root";
        String password = "My5070qL";
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public static void release(Statement stmt, Connection conn) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            stmt = null;
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn = null;
        }
    }

    public static void release(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rs = null;
        }
        release(stmt, conn);
    }
}
