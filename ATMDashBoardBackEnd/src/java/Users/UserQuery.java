/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Users;

import BackEnd.DBConnect;
import BackEnd.MD5Hashing;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Hashtable;
import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class UserQuery {

    public Connection connection = null;
    public Statement stmnt = null;
    public ResultSet rs = null;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    String connectionTypeLocal = "local";

    public String CheckLogin(String uName, String pwd, String ip) {

        String ResultJson = "\"\"";

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            Date d1 = null;
            Date d2 = null;
            String pattern = "yyyy-MM-dd HH:mm:ss";
            String curDate = null;

            MD5Hashing md5 = new MD5Hashing();
            String passMD5 = md5.MD5Password(pwd);
            String sql = "";
            sql = "SELECT * FROM USER_DETAILS WHERE user_name='" + uName + "' and password='" + passMD5 + "'";
            DBConnect obj = new DBConnect();
            connection = obj.dbConnect_Local();
            stmnt = connection.createStatement();
            rs = stmnt.executeQuery(sql);
            boolean canLog = true;

            if (rs.next()) {

                int uStatus = Integer.parseInt(rs.getString("USER_STATUS"));
                String uId = rs.getString("USER_ID");
                String pwdStatus = rs.getString("PASSWORD_STATUS");
                String pwdLastChanged = rs.getString("PASSWORD_LAST_CHANGED");
                int uLevel = Integer.parseInt(rs.getString("USER_LEVEL"));
                int uBranch = Integer.parseInt(rs.getString("USER_BRANCH"));

                if (pwdStatus == null) {
                    pwdStatus = "0";
                }
                if (pwdLastChanged == null) {
                    pwdLastChanged = "1990-01-01 00:00:00";
                }

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
                curDate = dtf.format(now);

                d1 = format.parse(pwdLastChanged);
                d2 = format.parse(curDate);

                long diff = d2.getTime() - d1.getTime();

                long diffDays = diff / (24 * 60 * 60 * 1000); // get date differnce

                if (uStatus == 0) {
                    ResultJson = "[{\"USER_ID\":" + uId + " , \"RESULT\":\"INACTIVE\"}]"; // Inactive User                    

                } else {
                    if (Integer.parseInt(pwdStatus) == 0) {
                        ResultJson = "[{\"USER_ID\":" + uId + " , \"RESULT\":\"NEW\"}]"; // for newly created user pswd should be changed
                        canLog = false;
                    } else if (diffDays > 90) {
                        ResultJson = "[{\"USER_ID\":" + uId + " , \"RESULT\":\"EXPIRED\"}]"; // if last pwd changed date is more than 90 days need to change
                        canLog = false;
                    } else {
                        ResultJson = "[{\"USER_ID\":" + uId + " , \"RESULT\":\"OK\" , \"USER_LVL\":" + uLevel + " , \"USER_BR\":" + uBranch + "}]";
                        if (updateLastLoginDetails(ip, uId)) {
                            canLog = true;
                        }

                    }
                }

            }
            rs.close();
            stmnt.close();
            obj.releaseCon(connection);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public boolean updateLastLoginDetails(String ip, String uId) {

        boolean isUpdate = false;

        try {
            DBConnect obj = new DBConnect();
            connection = obj.dbConnect_Local();
            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            String updateQry = "UPDATE USER_DETAILS SET LAST_LOGIN_IP=?,LAST_LOGIN=? WHERE USER_ID=?";
            PreparedStatement pstmt = connection.prepareStatement(updateQry);

            pstmt.setString(1, ip);
            pstmt.setTimestamp(2, new java.sql.Timestamp(t1));
            pstmt.setString(3, uId);

            if (pstmt.executeUpdate() > 0) {
                isUpdate = true;
            }

            pstmt.close();
            obj.releaseCon(connection);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return isUpdate;

    }

//    public String CheckLogin(String uName, String pwd, String ip) {
//
//        String ResultJson = "\"\"";
//
//        try {
//
//            if (checkAD(uName, pwd)) {
////                System.out.println("Logged in");
//                String sql = "";
//                sql = "SELECT * FROM DC_USERS WHERE user_name='" + uName + "'";
////                System.out.println(sql);
//                DBConnect obj = new DBConnect();
//                connection = obj.dbConnect_Local();
//                stmnt = connection.createStatement();
//                rs = stmnt.executeQuery(sql);
//
//                if (rs.next()) {
//                    int uStatus = Integer.parseInt(rs.getString("status"));
//                    String uId = rs.getString("user_id");
//
////                    System.out.println(uId);
//
//                    if (uStatus == 0) {
//                        ResultJson = "[{\"USER_ID\":" + uId + " , \"RESULT\":\"INACTIVE\"}]"; // Inactive User                
//                    } else {
//                        ResultJson = "[{\"USER_ID\":" + uId + " , \"RESULT\":\"OK\"}]"; //Active User
//                        updateLastLogin(uId,ip);
//                    }
//                } else {
//                    System.out.println("fail");
//                    ResultJson = "[{\"USER_ID\":\"" + uName + "\" , \"RESULT\":\"NOUSER\"}]"; // Not assignto the system
//                }
//
//                rs.close();
//                stmnt.close();
//                obj.releaseCon(connection);
//
//            }
////            else {
////                ResultJson = "[{\"USER_ID\":" + uName + " , \"RESULT\":\"NOADUSER\"}]"; // Not a AD User  
////            }
//
//        } catch (Exception e) {
//            System.out.println("Error when executing querry.\n" + e);
//        }
//
//        return ResultJson;
//    }
//
//    public boolean checkAD(String aduserid, String adpwd) {
//        
////        System.out.println("user-"+aduserid + "~pwd-"+adpwd);
//
//        boolean result = false;
//        String dn = aduserid.trim() + "@bankofceylon";
//        String ldapURL = "ldap://bankofceylon.local:389";
//
//        Hashtable<String, String> environment
//                = new Hashtable<String, String>();
//        environment.put(Context.INITIAL_CONTEXT_FACTORY,
//                "com.sun.jndi.ldap.LdapCtxFactory");
//        environment.put(Context.PROVIDER_URL, ldapURL);
//        environment.put(Context.SECURITY_AUTHENTICATION, "simple");
//        environment.put(Context.SECURITY_PRINCIPAL, dn);
//        environment.put(Context.SECURITY_CREDENTIALS, adpwd.trim());
//
//        try {
//            DirContext authContext = new InitialDirContext(environment);
//            // user is authenticated            
//            result = true;
//            //System.out.println(authContext.getEnvironment());
//            authContext.close();
//
//        } catch (AuthenticationNotSupportedException ex) {
//            System.out.println("The authentication is not supported by the server" + ex);
//            result = false;
//        } catch (AuthenticationException ex) {
//            System.out.println("login failed--" + ex);
//            result = false;
//            // Authentication failed
//
//        } catch (NamingException ex) {
//            System.out.println("login failed--" + ex);
//            System.out.println("exception--" + ex);
//            result = false;
//        }
//
//        return result;
//    }
//
//    public void updateLastLogin(String userId, String ip) {
//
//        try {
//
//            DBConnect obj = new DBConnect();
//            connection = obj.dbConnect_Local();
//            LocalDateTime now = LocalDateTime.now();
//            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
//            long t1 = logDate.getTime();
//
//            int uId = Integer.parseInt(userId.trim());
//            String updateQry = "UPDATE DC_USERS SET LAST_LOGIN=?,LAST_LOGIN_IP=? WHERE USER_ID=?";
//            PreparedStatement pstmt = connection.prepareStatement(updateQry);
//
//            pstmt.setTimestamp(1, new java.sql.Timestamp(t1));
//            pstmt.setString(2, ip);
//            pstmt.setInt(3, uId);
//            pstmt.executeUpdate();
//
//            pstmt.close();
//            obj.releaseCon(connection);
//
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//
//    }
//
    public String getAllowedFun(int uLvl) {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "SELECT FUN_ID FROM USER_LVL_ALLOWED_FUN WHERE USER_LVL_ID=" + uLvl;
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }
//

    public String getSystemUsers() {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "select u.user_id,u.user_name,u.first_name,u.last_name,u.user_status,u.user_level,u.user_branch,u.user_mob_no,u.user_email,l.level_desc,b.br_name "
                    + "from user_details u, user_level l,branch_details b "
                    + "where u.user_level = l.user_level and u.user_branch=b.br_code order by user_id";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String getAllUserFunctions() {

        String ResultJson = "\"\"";

        try {
            String sql = "";
//            sql = "SELECT FUN_ID,FUN_DESC,FUN_NAME FROM DC_FUNCTIONS WHERE fun_status = 1 and SUBSTR(fun_name,1,2) = 'FN' ORDER BY fun_name";
            sql = "SELECT FUN_ID,FUN_DESC,FUN_NAME FROM FUNCTIONS WHERE fun_status = 1 ORDER BY fun_name";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String getUserLvlFunctions(String uId) {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "select f.FUN_ID,f.FUN_NAME,f.FUN_DESC from FUNCTIONS f, USER_LVL_ALLOWED_FUN a where f.FUN_ID = a.FUN_ID "
                    + "and f.FUN_STATUS = 1 and a.USER_LVL_ID = " + uId + " ORDER BY f.FUN_NAME";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String updateUserFunc(String uId, String funId, String updUser, String userGroupDesc) {

        String ResultJson = "\"\"";

        try {
            updateUserGroup(uId, userGroupDesc, updUser);
            LocalDateTime now = LocalDateTime.now();
            DBConnect obj = new DBConnect();
            connection = obj.dbConnect_Local();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            String deleteQry = "DELETE FROM USER_LVL_ALLOWED_FUN WHERE user_lvl_id=? ";
            PreparedStatement pstmt = connection.prepareStatement(deleteQry);
            pstmt.setString(1, uId);
            pstmt.executeUpdate();

            if (funId.length() > 0) {

                String[] funArr = funId.split(",");

                String insertQry = "INSERT INTO USER_LVL_ALLOWED_FUN VALUES(?,?,?,?)";
                PreparedStatement pstmt1 = connection.prepareStatement(insertQry);

                for (int i = 0; i < funArr.length; i++) {
                    pstmt1.setString(1, uId);
                    pstmt1.setInt(2, Integer.parseInt(funArr[i]));
                    pstmt1.setTimestamp(3, new java.sql.Timestamp(t1));
                    pstmt1.setString(4, updUser);
                    pstmt1.addBatch();
                }

                int[] updateCounts = pstmt1.executeBatch();
                pstmt1.close();

                System.out.println("Updted Count" + updateCounts.length);

                if (updateCounts.length > 0) {
                    ResultJson = "[{\"RESULT\": 1 }]";
                } else {
                    ResultJson = "[{\"RESULT\": 0 }]";
                }
            } else {
                ResultJson = "[{\"RESULT\": -1 }]";
            }

            pstmt.close();
            obj.releaseCon(connection);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    //**************************
    public String addNewUserGroup(JSONObject jsonObj) {

        String ResultJson = "\"\"";

        try {
            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            String uGroup = jsonObj.getString("UGroup").trim();
            Integer updateUser = Integer.parseInt(jsonObj.getString("UpdatedUser").trim());

            DBConnect obj = new DBConnect();
            connection = obj.dbConnect_Local();

            String getQry = "SELECT MAX(CAST(USER_LEVEL AS NUMBER)) FROM USER_LEVEL";
            stmnt = connection.createStatement();
            rs = stmnt.executeQuery(getQry);

            if (rs.next()) {
                String val = rs.getString(1);
                BigInteger maxId = new BigInteger("0");
                if (val != null) {
                    maxId = new BigInteger(val);
                }

                String insertQry = "INSERT INTO USER_LEVEL VALUES(?,?,?,?,?)";

                PreparedStatement pstmt = connection.prepareStatement(insertQry);
                pstmt.setString(1, (maxId.add(BigInteger.ONE)) + ""); // userLevelId
                pstmt.setString(2, uGroup); // user Group Name
                pstmt.setInt(3, 1); // status
                pstmt.setTimestamp(4, new java.sql.Timestamp(t1)); // last updated
                pstmt.setInt(5, updateUser); //user updated by               

                if (pstmt.executeUpdate() > 0) {
                    ResultJson = "[{\"RESULT\": 1 }]";
                } else {
                    ResultJson = "[{\"RESULT\": 0 }]";
                }

                pstmt.close();
            }

            rs.close();
            stmnt.close();
            obj.releaseCon(connection);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    //***********************************
    public String updateUserGroup(String userLvlId, String userLvlDesc, String updUser) {

        String ResultJson = "\"\"";

        try {
            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            String sql = "";
            sql = "SELECT LEVEL_DESC FROM USER_LEVEL WHERE USER_LEVEL =" + userLvlId;
            DBConnect obj = new DBConnect();
            connection = obj.dbConnect_Local();
            stmnt = connection.createStatement();
            rs = stmnt.executeQuery(sql);

            if (rs.next()) {
                String preDesc = rs.getString(1).trim();
                BigInteger maxId = new BigInteger("0");
                if (!preDesc.equals(userLvlDesc)) {
                    try {
                        String updateQry = "UPDATE USER_LEVEL SET LEVEL_DESC=?,LAST_UPDATE=?,LAST_UPDATE_BY=? WHERE USER_LEVEL=?";
                        PreparedStatement pstmt = connection.prepareStatement(updateQry);
                        pstmt.setString(1, userLvlDesc);
                        pstmt.setTimestamp(2, new java.sql.Timestamp(t1));
                        pstmt.setString(3, updUser);
                        pstmt.setString(4, userLvlId);
                        pstmt.executeUpdate();
                        pstmt.close();
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
            }

            rs.close();
            stmnt.close();
            obj.releaseCon(connection);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String updateUserDet(JSONObject jsonObj) {

        String ResultJson = "\"\"";

        try {
            DBConnect obj = new DBConnect();
            connection = obj.dbConnect_Local();
            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            int uId = Integer.parseInt(jsonObj.getString("userId").trim());
            String fName = jsonObj.getString("firstName").trim();
            String lName = jsonObj.getString("lastName").trim();
            Integer uLvl = Integer.parseInt(jsonObj.getString("userLvl").trim());
            Integer uBr = Integer.parseInt(jsonObj.getString("userBr").trim());
            String uMob = jsonObj.getString("userMob").trim();
            String uEmail = jsonObj.getString("userEmail").trim();
            int status = Integer.parseInt(jsonObj.getString("status").trim());
            int updatedUser = Integer.parseInt(jsonObj.getString("updatedUser").trim());

            String updateQry = "UPDATE USER_DETAILS SET FIRST_NAME=?,LAST_NAME=?,USER_STATUS=?,USER_LEVEL=?,USER_BRANCH=?,"
                    + "LAST_RESET=?,LAST_RESET_BY=?,USER_MOB_NO=?,USER_EMAIL=? WHERE USER_ID=?";
            PreparedStatement pstmt = connection.prepareStatement(updateQry);

            pstmt.setString(1, fName);
            pstmt.setString(2, lName);
            pstmt.setInt(3, status);
            pstmt.setInt(4, uLvl);
            pstmt.setInt(5, uBr);
            pstmt.setTimestamp(6, new java.sql.Timestamp(t1));
            pstmt.setInt(7, updatedUser);
            pstmt.setString(8, uMob);
            pstmt.setString(9, uEmail);
            pstmt.setInt(10, uId);

            if (pstmt.executeUpdate() > 0) {
                ResultJson = "[{\"RESULT\": 1 }]";
            } else {
                ResultJson = "[{\"RESULT\": 0 }]";
            }

            pstmt.close();
            obj.releaseCon(connection);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;

    }

    public String createNewUser(JSONObject jsonObj) {

        String ResultJson = "\"\"";

        try {          

            String uName = jsonObj.getString("userName").trim();
            String fName = jsonObj.getString("firstName").trim();
            String lName = jsonObj.getString("lastName").trim();
            Integer uLvl = Integer.parseInt(jsonObj.getString("userLvl").trim());
            Integer uBr = Integer.parseInt(jsonObj.getString("userBr").trim());
            String uMob = jsonObj.getString("userMob").trim();
            String uEmail = jsonObj.getString("userEmail").trim();
            Integer updateUser = Integer.parseInt(jsonObj.getString("updateUser").trim());
            
            String pwd = uName.toLowerCase();
            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            MD5Hashing md5 = new MD5Hashing();
            String passMD5 = md5.MD5Password(pwd);
            long t1 = logDate.getTime();

            String sql = "";
            sql = "SELECT * FROM USER_DETAILS WHERE user_name='" + uName + "'";
            DBConnect obj = new DBConnect();
            connection = obj.dbConnect_Local();
            stmnt = connection.createStatement();
            rs = stmnt.executeQuery(sql);

            if (!rs.next()) {

                String getQry = "SELECT MAX(CAST(USER_ID AS NUMBER)) FROM USER_DETAILS";
                stmnt = connection.createStatement();
                rs = stmnt.executeQuery(getQry);

                if (rs.next()) {
                    String val = rs.getString(1);
                    BigInteger maxId = new BigInteger("0");
                    if (val != null) {
                        maxId = new BigInteger(val);
                    }
                    System.out.println("maxid" + maxId);

//                    String insertQry = "INSERT INTO USER_DETAILS(USER_ID,USER_NAME,FIRST_NAME,LAST_NAME,DEPT,STATUS,LAST_RESET,LAST_RESET_BY) VALUES(?,?,?,?,?,?,?,?)";
                    String insertQry = "INSERT INTO USER_DETAILS(USER_ID,USER_NAME,FIRST_NAME,LAST_NAME,PASSWORD,PASSWORD_STATUS,PASSWORD_LAST_CHANGED,USER_STATUS,"
                            + "USER_LEVEL,USER_BRANCH,USER_MOB_NO,USER_EMAIL,USER_CREATED,USER_CREATED_BY) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                    PreparedStatement pstmt = connection.prepareStatement(insertQry);
                    pstmt.setString(1, (maxId.add(BigInteger.ONE)) + ""); // userId
                    pstmt.setString(2, uName); // user Name
                    pstmt.setString(3, fName); // first Name
                    pstmt.setString(4, lName); // last Name                    
                    pstmt.setString(5, passMD5); // password
                    pstmt.setInt(6, 0); // password status 0 - pwd change status
                    pstmt.setTimestamp(7, new java.sql.Timestamp(t1)); // password changed date
                    pstmt.setInt(8, 1); //user status 1 - active
                    pstmt.setInt(9, uLvl); //user Level
                    pstmt.setInt(10, uBr); //user Branch
                    pstmt.setString(11, uMob); //user Mobile
                    pstmt.setString(12, uEmail); // user Email                     
                    pstmt.setTimestamp(13, new java.sql.Timestamp(t1)); // user create date
                    pstmt.setInt(14, updateUser); //user create by

                    if (pstmt.executeUpdate() > 0) {
                        ResultJson = "[{\"RESULT\": 1 }]";
                    } else {
                        ResultJson = "[{\"RESULT\": -1 }]";
                    }

                    pstmt.close();
                }

            } else {
                ResultJson = "[{\"RESULT\": 0 }]";
            }

            rs.close();
            stmnt.close();
            obj.releaseCon(connection);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String getPassword(String uId) {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "SELECT PASSWORD FROM USER_DETAILS WHERE USER_ID='" + uId + "'";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }
//

    public String changePassword(JSONObject jsonObj) {

        String ResultJson = "\"\"";

        try {
            DBConnect obj = new DBConnect();
            connection = obj.dbConnect_Local();
            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            int uId = Integer.parseInt(jsonObj.getString("uId").trim());
            String pwd = jsonObj.getString("pass").trim();

            String updateQry = "UPDATE USER_DETAILS SET PASSWORD=?,PASSWORD_STATUS=?,PASSWORD_LAST_CHANGED=? WHERE USER_ID=?";
            PreparedStatement pstmt = connection.prepareStatement(updateQry);

            pstmt.setString(1, pwd);
            pstmt.setInt(2, 1);
            pstmt.setTimestamp(3, new java.sql.Timestamp(t1));
            pstmt.setInt(4, uId);

            if (pstmt.executeUpdate() > 0) {
                ResultJson = "[{\"RESULT\": 1 }]";
            } else {
                ResultJson = "[{\"RESULT\": 0 }]";
            }

            pstmt.close();
            obj.releaseCon(connection);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;

    }
//

    public String resetPassword(JSONObject jsonObj) {

        String ResultJson = "\"\"";

        try {
            DBConnect obj = new DBConnect();
            connection = obj.dbConnect_Local();
            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            int uId = Integer.parseInt(jsonObj.getString("resetUser").trim());
            int resetUser = Integer.parseInt(jsonObj.getString("resetUserBy").trim());
            String userName = jsonObj.getString("resetUserName").trim().toLowerCase();
//            String resetUser = jsonObj.getString("resetUserBy").trim();
//           

//            String pwd = userName.toLowerCase();
            String pwd = userName;
            MD5Hashing md5 = new MD5Hashing();
            String passMD5 = md5.MD5Password(pwd);

            String updateQry = "UPDATE USER_DETAILS SET PASSWORD=?,PASSWORD_STATUS=?,LAST_RESET=?,LAST_RESET_BY=? WHERE USER_ID=?";
            PreparedStatement pstmt = connection.prepareStatement(updateQry);

            pstmt.setString(1, passMD5);
            pstmt.setInt(2, 0);
            pstmt.setTimestamp(3, new java.sql.Timestamp(t1));
            pstmt.setInt(4, resetUser);
            pstmt.setInt(5, uId);

            if (pstmt.executeUpdate() > 0) {
                ResultJson = "[{\"RESULT\": 1 }]";
            } else {
                ResultJson = "[{\"RESULT\": 0 }]";
            }

            pstmt.close();
            obj.releaseCon(connection);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;

    }

    public String getIndicator(String ind) {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "SELECT IN_VAL FROM INDICATORS WHERE IN_NAME='" + ind + "'";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }
//

    public String getUserLvl() {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "SELECT USER_LEVEL,LEVEL_DESC FROM USER_LEVEL WHERE LEVEL_STATUS = 1 ORDER BY USER_LEVEL";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String getUserBranch() {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "SELECT BR_CODE,BR_NAME FROM BRANCH_DETAILS WHERE BR_STATUS = 1 ORDER BY BR_NAME";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String getAllBranches() {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "select br_code,br_name,b.province_id,province_name from BRANCH_DETAILS b,PROVINCE_DETAILS p "
                    + "where b.province_id = p.province_id and br_status = 1 order by br_code";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String getAllProvinces() {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "select * from PROVINCE_DETAILS";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    //****************************************
    public int AddUpdateBranchDetails(JSONObject jsonObj) {

        String ResultJson = "\"\"";
        int up = 0;

        try {
            int brcode = Integer.parseInt(jsonObj.getString("brcode"));
            String brName = jsonObj.getString("brname").trim().toUpperCase();
            int province = Integer.parseInt(jsonObj.getString("province"));
            int updatedUser = Integer.parseInt(jsonObj.getString("user").trim());

            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            DBConnect obj = new DBConnect();
            connection = obj.dbConnect_Local();
            String updateQry = "UPDATE BRANCH_DETAILS SET BR_NAME=?,PROVINCE_ID=?,LAST_UPDATED=?,"
                    + "LAST_UPDATED_BY=? WHERE BR_CODE=?";

            PreparedStatement pstmt = connection.prepareStatement(updateQry);

            pstmt.setString(1, brName);
            pstmt.setInt(2, province);
            pstmt.setTimestamp(3, new java.sql.Timestamp(t1));
            pstmt.setInt(4, updatedUser);
            pstmt.setInt(5, brcode);

            if (pstmt.executeUpdate() <= 0) {
                String insertQry = "INSERT INTO BRANCH_DETAILS VALUES(?,?,?,?,?,?)";
                PreparedStatement pstmt1 = connection.prepareStatement(insertQry);

                pstmt1.setInt(1, brcode);
                pstmt1.setString(2, brName);
                pstmt1.setInt(3, province);
                pstmt1.setTimestamp(4, new java.sql.Timestamp(t1));
                pstmt1.setInt(5, updatedUser);
                pstmt1.setInt(6, 1);

                if (pstmt1.executeUpdate() > 0) {
                    up = 1;
                }
                pstmt1.close();
            } else {
                up = 1;
                pstmt.close();
            }

            obj.releaseCon(connection);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return up;
    }
}
