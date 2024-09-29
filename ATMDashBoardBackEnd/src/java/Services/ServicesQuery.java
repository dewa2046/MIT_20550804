/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

/**
 *
 * @author Administrator
 */
import BackEnd.DBConnect;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.json.JSONObject;

public class ServicesQuery {

    public Connection connection = null;
    public Statement stmnt = null;
    public ResultSet rs = null;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    String connectionTypeLocal = "local";

    public String getATMModels() {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "select model_id,model_name from ATM_MODELS where model_status = 1";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String getATMVenders() {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "select vender_id,vender_name from VENDER_DETAILS where vender_status = 1";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    //****************************************
    public int addUpdateATMDetails(JSONObject jsonObj) {

//        String ResultJson = "\"\"";
        int up = 0;

        try {
            String atmname = jsonObj.getString("atmname").trim();
            int brcode = Integer.parseInt(jsonObj.getString("brcode"));
            String location = jsonObj.getString("location").trim();
            String ip = jsonObj.getString("ip").trim();
            String os = jsonObj.getString("os").trim();
            int model = Integer.parseInt(jsonObj.getString("model"));
            int vender = Integer.parseInt(jsonObj.getString("vender"));
            String installedDate = jsonObj.getString("installedDate").trim();
            String divtype = jsonObj.getString("divtype").trim();
            String serial = jsonObj.getString("serial").trim();
            int emv = Integer.parseInt(jsonObj.getString("emv"));
            int recycler = Integer.parseInt(jsonObj.getString("recycler"));
            int status = Integer.parseInt(jsonObj.getString("status"));
            int remote = Integer.parseInt(jsonObj.getString("remote"));
            int overseas = Integer.parseInt(jsonObj.getString("overseas"));
            int uid = Integer.parseInt(jsonObj.getString("uid"));
            int protocol = 0;

            if (vender == 1) {
                protocol = 2; //NDC
            } else {
                protocol = 1; //DDC
            }

//
//            System.out.println(atmname + "~" + brcode + "~" + br + "~" + location + "~" + area + "~" + province + "~"
//                    + ip + "~" + dhp + "~" + authp + "~" + "~" + config + "~" + model + "~" + "~" + vender + "~" + installedDate + "~" + divtype + "~" + emv + "~" + recycler + "~"
//                    + status + "~" + serial + "~" + brtel + "~" + mgrtel + "~" + mcst + "~" + overseas + "~" + sms);
            DBConnect obj = new DBConnect();
            connection = obj.dbConnect_Local();
            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            String updateQry = "UPDATE ATM_INFO SET BR_CODE=?,LOCATION=?,IP=?,OS=?,MODEL=?,VENDER=?,"
                    + "INST_DATE=?,DIV_TYPE=?,SERIAL=?,EMV=?,RECYCLER=?,STATUS=?,"
                    + "REMOTE=?,OVERSEAS=?,PROTOCOL=?,LAST_UPDATE=?,LAST_UPDATE_BY=? "
                    + "WHERE ATM_NAME=?";

            PreparedStatement pstmt = connection.prepareStatement(updateQry);

            pstmt.setInt(1, brcode);
            pstmt.setString(2, location);
            pstmt.setString(3, ip);
            pstmt.setString(4, os);
            pstmt.setInt(5, model);
            pstmt.setInt(6, vender);
            Date date1 = null;
            if (installedDate != null && !installedDate.isEmpty()) {
                String dt1 = installedDate;
                date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dt1);
                pstmt.setDate(7, new java.sql.Date(date1.getTime()));
            } else {
                pstmt.setNull(7, java.sql.Types.DATE);
            }
            pstmt.setString(8, divtype);
            pstmt.setString(9, serial);
            pstmt.setInt(10, emv);
            pstmt.setInt(11, recycler);
            pstmt.setInt(12, status);
            pstmt.setInt(13, remote);
            pstmt.setInt(14, overseas);
            pstmt.setInt(15, protocol);
            pstmt.setTimestamp(16, new java.sql.Timestamp(t1));
            pstmt.setInt(17, uid);
            pstmt.setString(18, atmname);

            if (pstmt.executeUpdate() <= 0) {
                String insertQry = "INSERT INTO ATM_INFO VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement pstmt1 = connection.prepareStatement(insertQry);

                pstmt1.setString(1, atmname);
                pstmt1.setInt(2, brcode);
                pstmt1.setString(3, location);
                pstmt1.setString(4, ip);
                pstmt1.setString(5, os);
                pstmt1.setInt(6, model);
                pstmt1.setInt(7, vender);
                if (installedDate != null && !installedDate.isEmpty()) {
                    String dt1 = installedDate;
                    date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dt1);
                    pstmt1.setDate(8, new java.sql.Date(date1.getTime()));
                } else {
                    pstmt1.setNull(8, java.sql.Types.DATE);
                }
                pstmt1.setString(9, divtype);
                pstmt1.setString(10, serial);
                pstmt1.setInt(11, emv);
                pstmt1.setInt(12, recycler);
                pstmt1.setInt(13, status);
                pstmt1.setInt(14, remote);
                pstmt1.setInt(15, overseas);
                pstmt1.setInt(16, protocol);
                pstmt1.setTimestamp(17, new java.sql.Timestamp(t1));
                pstmt1.setInt(18, uid);

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

    public String GetATMDetails(String atm) {

        String ResultJson = "\"\"";

        try {
            String sql = "";

            sql = "SELECT * FROM ATM_INFO WHERE ATM_NAME='" + atm + "'";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String getAllATMDetails(JSONObject jsonObj) {

        String ResultJson = "\"\"";

        try {
            int userBr = Integer.parseInt(jsonObj.getString("UBr"));
            int userLvl = Integer.parseInt(jsonObj.getString("ULvl"));
            String sql = "";

//            if (userLvl == 1) {
            if (userLvl != 3) { //if user is not branch user
                sql = "select ATM_NAME,a.BR_CODE,br_name,location,model_name,vender_name,a.status "
                        + "from ATM_INFO a,VENDER_DETAILS v,BRANCH_DETAILS b,ATM_MODELS m "
                        + "where a.vender = v.vender_id  and a.br_code = b.br_code and a.model = m.model_id and a.status = 1 "
                        + "order by ATM_NAME";
            } else {
                sql = "select ATM_NAME,a.BR_CODE,br_name,location,model_name,vender_name,a.status "
                        + "from ATM_INFO a,VENDER_DETAILS v,BRANCH_DETAILS b,ATM_MODELS m "
                        + "where a.vender = v.vender_id  and a.br_code = b.br_code and a.model = m.model_id and a.status = 1 "
                        + "and a.br_code = " + userBr + " order by ATM_NAME";
            }
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String getATMContactDetails(String atm) {

        String ResultJson = "\"\"";

        try {
            String sql = "";

            sql = "SELECT c.*,u.USER_NAME FROM ATM_OFFICER_CONTACTS c , USER_DETAILS u "
                    + "WHERE c.LAST_UPDATED_BY = u.USER_ID and ATM_NAME='" + atm + "'";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    //****************************************
    public int addUpdateATMContacts(JSONObject jsonObj) {

//        String ResultJson = "\"\"";
        int up = 0;

        try {
            String atmname = jsonObj.getString("atmname").trim();
            String pf1 = jsonObj.getString("pf1").trim();
            String off1 = jsonObj.getString("off1").trim();
            String grade1 = jsonObj.getString("grade1").trim();
            String mob1 = jsonObj.getString("mob1").trim();
            String ext1 = jsonObj.getString("ext1").trim();

            String pf2 = jsonObj.getString("pf2").trim();
            String off2 = jsonObj.getString("off2").trim();
            String grade2 = jsonObj.getString("grade2").trim();
            String mob2 = jsonObj.getString("mob2").trim();
            String ext2 = jsonObj.getString("ext2").trim();

            String pf3 = jsonObj.getString("pf3").trim();
            String off3 = jsonObj.getString("off3").trim();
            String grade3 = jsonObj.getString("grade3").trim();
            String mob3 = jsonObj.getString("mob3").trim();
            String ext3 = jsonObj.getString("ext3").trim();

            int uid = Integer.parseInt(jsonObj.getString("uid"));

//
//            System.out.println(atmname + "~" + brcode + "~" + br + "~" + location + "~" + area + "~" + province + "~"
//                    + ip + "~" + dhp + "~" + authp + "~" + "~" + config + "~" + model + "~" + "~" + vender + "~" + installedDate + "~" + divtype + "~" + emv + "~" + recycler + "~"
//                    + status + "~" + serial + "~" + brtel + "~" + mgrtel + "~" + mcst + "~" + overseas + "~" + sms);
            DBConnect obj = new DBConnect();
            connection = obj.dbConnect_Local();
            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            String updateQry = "UPDATE ATM_OFFICER_CONTACTS SET PF_1=?,OFFICER_1=?,GRADE_1=?,TEL_MOB_1=?,TEL_EXT_1=?,"
                    + "PF_2=?,OFFICER_2=?,GRADE_2=?,TEL_MOB_2=?,TEL_EXT_2=?,"
                    + "PF_3=?,OFFICER_3=?,GRADE_3=?,TEL_MOB_3=?,TEL_EXT_3=?,"
                    + "LAST_UPDATED=?,LAST_UPDATED_BY=? "
                    + "WHERE ATM_NAME=?";

            PreparedStatement pstmt = connection.prepareStatement(updateQry);

            pstmt.setString(1, pf1);
            pstmt.setString(2, off1);
            pstmt.setString(3, grade1);
            pstmt.setString(4, mob1);
            pstmt.setString(5, ext1);
            pstmt.setString(6, pf2);
            pstmt.setString(7, off2);
            pstmt.setString(8, grade2);
            pstmt.setString(9, mob2);
            pstmt.setString(10, ext2);
            pstmt.setString(11, pf3);
            pstmt.setString(12, off3);
            pstmt.setString(13, grade3);
            pstmt.setString(14, mob3);
            pstmt.setString(15, ext3);
            pstmt.setTimestamp(16, new java.sql.Timestamp(t1));
            pstmt.setInt(17, uid);
            pstmt.setString(18, atmname);

            if (pstmt.executeUpdate() <= 0) {
                String insertQry = "INSERT INTO ATM_OFFICER_CONTACTS VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement pstmt1 = connection.prepareStatement(insertQry);

                pstmt1.setString(1, atmname);
                pstmt1.setString(2, pf1);
                pstmt1.setString(3, off1);
                pstmt1.setString(4, grade1);
                pstmt1.setString(5, mob1);
                pstmt1.setString(6, ext1);
                pstmt1.setString(7, pf2);
                pstmt1.setString(8, off2);
                pstmt1.setString(9, grade2);
                pstmt1.setString(10, mob2);
                pstmt1.setString(11, ext2);
                pstmt1.setString(12, pf3);
                pstmt1.setString(13, off3);
                pstmt1.setString(14, grade3);
                pstmt1.setString(15, mob3);
                pstmt1.setString(16, ext3);
                pstmt1.setTimestamp(17, new java.sql.Timestamp(t1));
                pstmt1.setInt(18, uid);

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

    //****************************************
    public String getATMStatus(JSONObject jsonObj) {

        String ResultJson = "\"\"";

        try {
            int userBr = Integer.parseInt(jsonObj.getString("UBr"));
            int userLvl = Integer.parseInt(jsonObj.getString("ULvl"));
            String sql = "";

//            if (userLvl == 1) {
            if (userLvl != 3) { //if user is not branch user
                sql = "select STATION,ERR_CODE,s.LAST_UPDATE,b.br_code,br_name,location,p.province_name,m.model_name,v.vender_name,ip,a.div_type,a.protocol "
                        + "from ATM_STATUS_NOW s,ATM_INFO a,BRANCH_DETAILS b,ATM_MODELS m,VENDER_DETAILS v,PROVINCE_DETAILS p "
                        + "where s.STATION = a.atm_name and a.br_code = b.br_code and a.model = m.model_id and a.vender = v.vender_id and b.province_id = p.province_id and a.status = 1 "
                        + "order by b.br_code,STATION";
            } else {
                sql = "select STATION,ERR_CODE,s.LAST_UPDATE,b.br_code,br_name,location,p.province_name,m.model_name,v.vender_name,ip,a.div_type,a.protocol "
                        + "from ATM_STATUS_NOW s,ATM_INFO a,BRANCH_DETAILS b,ATM_MODELS m,VENDER_DETAILS v,PROVINCE_DETAILS p "
                        + "where s.STATION = a.atm_name and a.br_code = b.br_code and a.model = m.model_id and a.vender = v.vender_id and b.province_id = p.province_id and a.status = 1 "
                        + "and b.br_code=" + userBr + " order by STATION";
            }

            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    //****************************************
    public String getDashBoardDetail_1_2(String condition, String name) {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "select count(*) as " + name + " from ATM_STATUS_NOW d,ATM_INFO a "
                    + "where d.STATION=a.ATM_NAME and a.STATUS=1 and " + condition;
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    //****************************************
    public String getATMUpdatedTime() {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "SELECT last_update FROM ( select * from ATM_STATUS_NOW order by last_update desc) WHERE ROWNUM <= 1";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    //****************************************
    public String getATMDeviceList() {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "select vender_name,div_type,count(*) as tot from ATM_INFO a,VENDER_DETAILS v "
                    + "where a.vender = v. vender_id and status = 1 group by vender_name,div_type";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    //****************************************
    public String getDashBoardDetail_4(String condition, String name) {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "select count(*) as " + name + " from ATM_STATUS_NOW d,ATM_INFO a "
                    + "where d.STATION=a.ATM_NAME and a.STATUS=1 and " + condition;
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    //****************************************
    public String getDashboardSeparate(String condition) {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "select STATION,ERR_CODE,s.LAST_UPDATE,b.br_code,br_name,location,p.province_name,m.model_name,v.vender_name,ip,a.div_type,a.protocol "
                    + "from ATM_STATUS_NOW s,ATM_INFO a,BRANCH_DETAILS b,ATM_MODELS m,VENDER_DETAILS v,PROVINCE_DETAILS p "
                    + "where s.STATION = a.atm_name and a.br_code = b.br_code and a.model = m.model_id and a.vender = v.vender_id and b.province_id = p.province_id and a.status = 1 and " + condition
                    + "order by b.br_code,STATION";

            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    //****************************************
    public String getDashboardSeparate_3(String condition) {

        String ResultJson = "\"\"";

        try {
            String sql = "";

            if (condition.trim().equals("ALL")) {
                sql = "select atm_name,a.br_code,br_name as branch,location,p.province_name as province,v.vender_name as vender,m.model_name as model,emv,recycler,a.div_type "
                        + "from ATM_INFO a,BRANCH_DETAILS b,ATM_MODELS m,VENDER_DETAILS v,PROVINCE_DETAILS p "
                        + "where a.br_code = b.br_code and a.model = m.model_id and a.vender = v.vender_id and b.province_id = p.province_id and a.status = 1 "
                        + "order by atm_name";

            } else {
                sql = "select atm_name,a.br_code,br_name as branch,location,p.province_name as province,v.vender_name as vender,m.model_name as model,emv,recycler,a.div_type "
                        + "from ATM_INFO a,BRANCH_DETAILS b,ATM_MODELS m,VENDER_DETAILS v,PROVINCE_DETAILS p "
                        + "where a.br_code = b.br_code and a.model = m.model_id and a.vender = v.vender_id and b.province_id = p.province_id and a.status = 1 AND " + condition
                        + " ORDER BY atm_name";
            }

            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String getFaultAllTypes() {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "select fault_id,fault_desc from FAULT_TYPES where fault_status = 1";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String getFaultTypeDetails(String fid) {

        String ResultJson = "\"\"";

        try {
            String sql = "";

            sql = "select far_id,far_desc,far_type,fault_desc,far_status from FAULT_ACTION_RESULT f,FAULT_TYPES t "
                    + "where f.far_type =t.fault_id and far_type =" + fid;
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String updateFaultDetails(JSONObject jsonObj) {

        String ResultJson = "\"\"";

        try {
            DBConnect obj = new DBConnect();
            connection = obj.dbConnect_Local();
            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            int farId = Integer.parseInt(jsonObj.getString("farID").trim());
            int resetUser = Integer.parseInt(jsonObj.getString("user").trim());
            String farDesc = jsonObj.getString("farDesc").trim().toUpperCase();
            int farStatus = Integer.parseInt(jsonObj.getString("farStatus").trim());

            String updateQry = "UPDATE FAULT_ACTION_RESULT SET FAR_DESC=?,FAR_STATUS=?,FAR_UPDATED=?,FAR_UPDATED_BY=? WHERE FAR_ID=?";
            PreparedStatement pstmt = connection.prepareStatement(updateQry);

            pstmt.setString(1, farDesc);
            pstmt.setInt(2, farStatus);
            pstmt.setTimestamp(3, new java.sql.Timestamp(t1));
            pstmt.setInt(4, resetUser);
            pstmt.setInt(5, farId);

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

    //**************************
    public String addFaultDetails(JSONObject jsonObj) {

        String ResultJson = "\"\"";

        try {

            String farDesc = jsonObj.getString("farDesc").trim();
            Integer faultCat = Integer.parseInt(jsonObj.getString("faultCat").trim());
            Integer farStatus = 1;
            Integer updateUser = Integer.parseInt(jsonObj.getString("user").trim());

            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            String sql = "";
            sql = "SELECT * FROM FAULT_ACTION_RESULT WHERE FAR_DESC='" + farDesc + "' AND FAR_TYPE = " + faultCat;
            DBConnect obj = new DBConnect();
            connection = obj.dbConnect_Local();
            stmnt = connection.createStatement();
            rs = stmnt.executeQuery(sql);

            if (!rs.next()) {

                String getQry = "SELECT MAX(CAST(FAR_ID AS NUMBER)) FROM FAULT_ACTION_RESULT";
                stmnt = connection.createStatement();
                rs = stmnt.executeQuery(getQry);

                if (rs.next()) {
                    int val = Integer.parseInt(rs.getString(1));
                    int maxId = 0;
                    if (val != 0) {
                        maxId = val;
                    }
                    System.out.println("maxid" + maxId);
//                    
                    String insertQry = "INSERT INTO FAULT_ACTION_RESULT(FAR_ID,FAR_DESC,FAR_TYPE,FAR_STATUS,FAR_UPDATED,FAR_UPDATED_BY) VALUES(?,?,?,?,?,?)";

                    PreparedStatement pstmt = connection.prepareStatement(insertQry);
                    pstmt.setInt(1, (maxId + 1));
                    pstmt.setString(2, farDesc);
                    pstmt.setInt(3, faultCat);
                    pstmt.setInt(4, farStatus);
                    pstmt.setTimestamp(5, new java.sql.Timestamp(t1));
                    pstmt.setInt(6, updateUser);

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

    public String getATMforFaults() {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "select ATM_NAME,a.BR_CODE,b.br_name,LOCATION from ATM_INFO a,BRANCH_DETAILS b where a.br_code = b.br_code and a.status = 1 order by ATM_NAME";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String getFaults() {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "select far_id,far_desc,far_type,fault_desc from FAULT_ACTION_RESULT f,FAULT_TYPES t where f.far_type = t.fault_id and far_status = 1 order by far_id";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    //**************************
    public String addFaultEntry(JSONObject jsonObj) {

        String ResultJson = "\"\"";

        try {
            String faultatm = jsonObj.getString("atm").trim();
            String fault = jsonObj.getString("fault").trim();
            String action = jsonObj.getString("action").trim();
            String result = jsonObj.getString("result").trim();
            String remark = jsonObj.getString("remark").trim();
            Integer updateUser = Integer.parseInt(jsonObj.getString("user").trim());

            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            String sql = "";
            sql = "SELECT count(*) as count FROM ATM_FAULT_ENTRY";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);
            String cleanString = ResultJson.replaceAll("[\\[\\]\"{}]", "");
            String[] parts = cleanString.split(":");
            int maxId = Integer.parseInt(parts[1]);

            connection = obj.dbConnect_Local();
            String insertQry = "INSERT INTO ATM_FAULT_ENTRY(FAULT_ENTRY_ID,FAULT_ATM,FAULT,ACTION,RESULT,REMARKS,FAULT_ENTRY_DATE,CREATED_USER) "
                    + "VALUES(?,?,?,?,?,?,?,?)";

            PreparedStatement pstmt = connection.prepareStatement(insertQry);
            pstmt.setInt(1, (maxId + 1));
            pstmt.setString(2, faultatm);
            pstmt.setString(3, fault);
            pstmt.setString(4, action);
            pstmt.setString(5, result);
            pstmt.setString(6, remark);
            pstmt.setTimestamp(7, new java.sql.Timestamp(t1));
            pstmt.setInt(8, updateUser);

            if (pstmt.executeUpdate() > 0) {
                ResultJson = "[{\"RESULT\": 1 }]";
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

    public String getEntry(String type) {

        String ResultJson = "\"\"";

        try {
            String sql = "";

            if (type.trim().toUpperCase().equals("ALL")) {
                sql = "SELECT * FROM (select fault_atm,fault,action,result,remarks,fault_entry_date,user_name "
                        + "from ATM_FAULT_ENTRY a,USER_DETAILS u where a.created_user = u.user_id order by fault_entry_date DESC) WHERE ROWNUM<=5";
            } else {
                sql = "SELECT * FROM "
                        + "(SELECT fault_atm,fault,action,result,remarks,fault_entry_date,user_name FROM "
                        + "ATM_FAULT_ENTRY a,USER_DETAILS u WHERE a.created_user = u.user_id and TRIM(fault_atm)='" + type + "' ORDER BY fault_entry_date DESC) "
                        + "WHERE ROWNUM<=5";
            }

            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String getCashDetails(String condition) {

        String ResultJson = "\"\"";

        try {
            String sql = "";

            if (condition.trim().equals("ALL")) {
                sql = "select ATM,BILLVAL,BEGCASH,CASHINCR,CASHOUT,ENDCASH,TIMESTAMP,b.br_code,br_name,location,p.province_name,m.model_name,v.vender_name,ip,a.div_type,a.protocol "
                        + "from ATM_CASHPO s,ATM_INFO a,BRANCH_DETAILS b,ATM_MODELS m,VENDER_DETAILS v,PROVINCE_DETAILS p "
                        + "where s.ATM = a.atm_name and a.br_code = b.br_code and a.model = m.model_id and a.vender = v.vender_id and b.province_id = p.province_id and a.status = 1 "
                        + "order by b.br_code,ATM";

            } else {
                sql = "select ATM,BILLVAL,BEGCASH,CASHINCR,CASHOUT,ENDCASH,TIMESTAMP,b.br_code,br_name,location,p.province_name,m.model_name,v.vender_name,ip,a.div_type,a.protocol "
                        + "from ATM_CASHPO s,ATM_INFO a,BRANCH_DETAILS b,ATM_MODELS m,VENDER_DETAILS v,PROVINCE_DETAILS p "
                        + "where s.ATM = a.atm_name and a.br_code = b.br_code and a.model = m.model_id and a.vender = v.vender_id and b.province_id = p.province_id and a.status = 1 "
                        + "and ATM='" + condition + "'";
            }

            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String getSMSDetails(String atm) {

        String ResultJson = "\"\"";

        try {
            String sql = "select * from (select sent_sms,sent_date from ATM_SMS_SEND where atm='" + atm + "' "
                    + "order by sent_date desc ) WHERE ROWNUM <= 5";

            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }
}
