/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BackEnd;

/**
 *
 * @author Administrator
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import org.json.JSONArray;

public class DBConnect {

    Connection conn = null;
    public Connection connection = null;
    public String msg = "";
    public Statement stmnt = null;
    public ResultSet rs = null;
    public ObjectNode node = new ObjectMapper().createObjectNode();
    public JSONArray convertToJSON;
    public JSONArray emptyJSON = new JSONArray();

    //********************************************
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

    //********************************************
    //Get DB Data
    public String getDBData(String query) throws NamingException {
        try {
            //Establish connection
            connection = dbConnect_Local();

            stmnt = connection.createStatement();
            rs = stmnt.executeQuery(query);
            JsonConvertor conObj = new JsonConvertor();

            try {
                convertToJSON = conObj.rsToJson(rs, emptyJSON);
            } catch (Exception ex) {
                Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
            }

            rs.close();
            stmnt.close();
            releaseCon(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //return rs;         
        String out = convertToJSON.toString();

        if (convertToJSON.toString().equals("") || convertToJSON.toString().equals("[]")) {
            return "\"\"";
        } else {
            return convertToJSON.toString();
        }
    }
}
