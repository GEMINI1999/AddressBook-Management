package cn.design;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SearchTool {

    public static boolean hasData(String strData) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        AddressBook addressBook = new AddressBook();
        boolean sign = false;
        try {
            conn = JDBCUtils.getConnection();
            stmt = conn.createStatement();
            if (!CharTool.isNumeric(strData)) {
                String sql = "select * from address_book where name = '" + strData + "'";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String str = rs.getString("name");
                    if (str == null) {
                        break;
                    } else {
                        sign = true;
                        break;
                    }
                }
            } else {
                String sql = "select * from address_book where phone_number = '" + strData + "'";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String str = rs.getString("name");
                    if (str == null) {
                        break;
                    } else {
                        sign = true;
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.release(rs, stmt, conn);
            return sign;
        }
    }
}
