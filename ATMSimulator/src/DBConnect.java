
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author it207440
 */
public class DBConnect {
    
    public static Connection dbConnect_Local() {

        Connection con = null;
        try {
            String url = "jdbc:oracle:thin:@//localhost:1521/XEPDB1"; // with ojdbc7.jar. can't connect with ojdbc14.jar
            String driver = "oracle.jdbc.driver.OracleDriver";
            String userName = "bocatm";
            String password = "bocatm";
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url, userName, password);

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return con;
    }
    
    //********************************************
    public void releaseCon(Connection con) {

        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }
    
}
