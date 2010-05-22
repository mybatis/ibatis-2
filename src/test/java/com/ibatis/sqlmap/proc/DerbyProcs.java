package com.ibatis.sqlmap.proc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyProcs {
  public static void selectRows(int p1, int p2, int p3, int p4, ResultSet[] rs1, ResultSet[] rs2) throws SQLException {
    Connection conn = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps1 = conn.prepareStatement("select * from account where acc_id in (?,?)");
    ps1.setInt(1, p1);
    ps1.setInt(2, p2);
    rs1[0] = ps1.executeQuery();
    PreparedStatement ps2 = conn.prepareStatement("select * from account where acc_id in (?,?)");
    ps2.setInt(1, p3);
    ps2.setInt(2, p4);
    rs2[0] = ps2.executeQuery();
    conn.close();
  }

}