
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author it207440
 */
public class sqlop {

    DBConnect dbconnect = new DBConnect();
    Connection conn = null;
    Statement stmnt = null;
    ResultSet rs = null;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ArrayList<String> getMachineList() {

        ArrayList<String> atmDetails = new ArrayList<String>();

        try {
            conn = dbconnect.dbConnect_Local();
            stmnt = conn.createStatement();

            String query = "select atm_name,location from ATM_INFO where "
                    + "status = 1 order by br_code";
            rs = stmnt.executeQuery(query);

            while (rs.next()) {
                atmDetails.add(rs.getString(1) + "~" + rs.getString(2));
            }
            rs.close();
            stmnt.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return atmDetails;
    }

    public String getATMDetails(String atm) {

        String atmDet = "";

        try {
            conn = dbconnect.dbConnect_Local();
            stmnt = conn.createStatement();

            String query = "select div_type,protocol from ATM_INFO where "
                    + "atm_name = '" + atm + "'";
            rs = stmnt.executeQuery(query);

            if (rs.next()) {
                atmDet = rs.getString(1) + "~" + rs.getString(2);
            }
            rs.close();
            stmnt.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return atmDet;
    }

    public String getATMStatus(String atm) {

        String atmDet = "";

        try {
            conn = dbconnect.dbConnect_Local();
            stmnt = conn.createStatement();

            String query = "select station,err_code,flt_count,criticality from "
                    + "ATM_STATUS_NOW where station = '" + atm + "'";
            rs = stmnt.executeQuery(query);

            if (rs.next()) {
                atmDet = rs.getString(1) + "~" + rs.getString(2)
                        + "~" + rs.getString(3) + "~" + rs.getString(4);
            }
            rs.close();
            stmnt.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return atmDet;
    }

    public int updateATMStatus(String txt) {
        int up = 0;
        try {
            String atm = txt.split("#")[0];
            String err = txt.split("#")[1];
            int flt = Integer.parseInt(txt.split("#")[2]);
            int priority = Integer.parseInt(txt.split("#")[3]);

            DBConnect obj = new DBConnect();
            conn = obj.dbConnect_Local();

            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            String updateQry = "UPDATE ATM_STATUS_NOW SET ERR_CODE=?,FLT_COUNT=?,CRITICALITY=?,LAST_UPDATE=? "
                    + "WHERE STATION=?";

            PreparedStatement pstmt = conn.prepareStatement(updateQry);
            pstmt.setString(1, err);
            pstmt.setInt(2, flt);
            pstmt.setInt(3, priority);
            pstmt.setTimestamp(4, new java.sql.Timestamp(t1));
            pstmt.setString(5, atm);

            if (pstmt.executeUpdate() <= 0) {
                String insertQry = "INSERT INTO ATM_STATUS_NOW VALUES(?,?,?,?,?)";
                PreparedStatement pstmt1 = conn.prepareStatement(insertQry);

                pstmt1.setString(1, atm);
                pstmt1.setString(2, err);
                pstmt1.setInt(3, flt);
                pstmt1.setInt(4, priority);
                pstmt1.setTimestamp(5, new java.sql.Timestamp(t1));

                if (pstmt1.executeUpdate() > 0) {
                    up = 1;
                }
                pstmt1.close();
            } else {
                up = 1;
                pstmt.close();
            }
            obj.releaseCon(conn);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return up;
    }

    public int updateATMStatusSummary(String atm, String err) {

        int up = 0;
        boolean canInsert = false;
        try {
            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            String sql = "";
            sql = "select * from (select err_code from ATM_STS_DESC_SUMMARY "
                    + "where station = '" + atm + "' order by flt_date desc) where rownum<=1";
            DBConnect obj = new DBConnect();
            conn = obj.dbConnect_Local();
            stmnt = conn.createStatement();
            rs = stmnt.executeQuery(sql);
            if (rs.next()) {
                if (!rs.getString(1).trim().matches(err.trim())) {
                    canInsert = true;
                }
            } else {
                canInsert = true;
            }

            if (canInsert) {
                String insertQry = "INSERT INTO ATM_STS_DESC_SUMMARY(STATION,ERR_CODE,FLT_DATE,NETWORK_DATE,UPDATED_TIME) "
                        + "VALUES(?,?,?,?,?)";

                PreparedStatement pstmt = conn.prepareStatement(insertQry);
                pstmt.setString(1, atm);
                pstmt.setString(2, err);
                pstmt.setTimestamp(3, new java.sql.Timestamp(t1));
                pstmt.setTimestamp(4, new java.sql.Timestamp(t1));
                pstmt.setTimestamp(5, new java.sql.Timestamp(t1));

                if (pstmt.executeUpdate() > 0) {
                    up = 1;
                } else {
                    up = 0;
                }

                pstmt.close();

            }
            obj.releaseCon(conn);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return up;

    }

    public int insertSMS(String atm, String err, int protocol, String location, String div) {

        int up = 0;

        try {

            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            ArrayList<String> OperatorsContacts = new ArrayList<String>();

            OperatorsContacts = getOperatorContacts(atm);

            if (OperatorsContacts.size() > 0) {
                int maxId = getMaxSMSID();
                boolean canInsert = checkPreErr(atm, err);
                String sms = "";

                if (!canInsert) {
                    sms = genarateSMSTxt(atm, err, location, protocol, div);

                    DBConnect obj = new DBConnect();
                    conn = obj.dbConnect_Local();

                    String insertQry = "INSERT INTO ATM_SMS_SEND(SMS_ID,ATM,ERR_CODE,SENT_SMS,SENT_DATE) "
                            + "VALUES(?,?,?,?,?)";

                    PreparedStatement pstmt = conn.prepareStatement(insertQry);
                    pstmt.setInt(1, maxId + 1);
                    pstmt.setString(2, atm);
                    pstmt.setString(3, err);
                    pstmt.setString(4, sms);
                    pstmt.setTimestamp(5, new java.sql.Timestamp(t1));

                    if (pstmt.executeUpdate() > 0) {
                        up = 1;
                    } else {
                        up = 0;
                    }

                    if (up > 0) {
                        for (int i = 0; i < OperatorsContacts.size(); i++) {
                            insertSMSTo(maxId + 1, OperatorsContacts.get(i));
                        }
                    }

                    pstmt.close();

                    obj.releaseCon(conn);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return up;

    }

    public int insertSMSFltLvl0(String atm, String err, String location) {
        int up = 0;

        try {
            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            ArrayList<String> OperatorsContacts = new ArrayList<String>();

            OperatorsContacts = getOperatorContacts(atm);

            if (OperatorsContacts.size() > 0) {
                int maxId = getMaxSMSID();
                boolean canInsert = checkPreErr(atm, err);
                String sms = "";
                if (!canInsert) {
                    sms = atm + "(" + location + ") is functioning well.Thanks";

                    DBConnect obj = new DBConnect();
                    conn = obj.dbConnect_Local();

                    String insertQry = "INSERT INTO ATM_SMS_SEND(SMS_ID,ATM,ERR_CODE,SENT_SMS,SENT_DATE) "
                            + "VALUES(?,?,?,?,?)";

                    PreparedStatement pstmt = conn.prepareStatement(insertQry);
                    pstmt.setInt(1, maxId + 1);
                    pstmt.setString(2, atm);
                    pstmt.setString(3, err);
                    pstmt.setString(4, sms);
                    pstmt.setTimestamp(5, new java.sql.Timestamp(t1));

                    if (pstmt.executeUpdate() > 0) {
                        up = 1;
                    } else {
                        up = 0;
                    }

                    if (up > 0) {
                        for (int i = 0; i < OperatorsContacts.size(); i++) {
                            insertSMSTo(maxId + 1, OperatorsContacts.get(i));
                        }
                    }

                    pstmt.close();

                    obj.releaseCon(conn);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return up;

    }

    public int insertSMSTo(int smsId, String mob) {

        int up = 0;

        try {
            DBConnect obj = new DBConnect();
            conn = obj.dbConnect_Local();

            String insertQry = "INSERT INTO ATM_SMS_SEND_TO(SMS_ID,SMS_TO) "
                    + "VALUES(?,?)";

            PreparedStatement pstmt = conn.prepareStatement(insertQry);
            pstmt.setInt(1, smsId);
            pstmt.setString(2, mob);

            if (pstmt.executeUpdate() > 0) {
                up = 1;
            } else {
                up = 0;
            }
            pstmt.close();

            obj.releaseCon(conn);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return up;
    }

    public ArrayList<String> getOperatorContacts(String atm) {

        ArrayList<String> OperatorsContacts = new ArrayList<String>();

        try {
            conn = dbconnect.dbConnect_Local();
            stmnt = conn.createStatement();
            String getQry = "SELECT TEL_MOB_1,TEL_MOB_2,TEL_MOB_3 FROM ATM_OFFICER_CONTACTS "
                    + "WHERE ATM_NAME ='" + atm + "'";
            rs = stmnt.executeQuery(getQry);

            if (rs.next()) {
                if (rs.getString(1) != null) {
                    OperatorsContacts.add(rs.getString(1));
                }
                if (rs.getString(2) != null) {
                    OperatorsContacts.add(rs.getString(2));
                }
                if (rs.getString(3) != null) {
                    OperatorsContacts.add(rs.getString(3));
                }
            }
            rs.close();
            stmnt.close();
            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return OperatorsContacts;

    }

    public int getMaxSMSID() {

        int smsId = 0;

        try {
            conn = dbconnect.dbConnect_Local();
            stmnt = conn.createStatement();

            String query = "select count(*) from ATM_SMS_SEND";
            rs = stmnt.executeQuery(query);

            if (rs.next()) {
                smsId = Integer.parseInt(rs.getString(1));
            }
            rs.close();
            stmnt.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return smsId;
    }

    public boolean checkPreErr(String atm, String err) {

        boolean errFound = false;

        try {
            conn = dbconnect.dbConnect_Local();
            stmnt = conn.createStatement();

            String query = "SELECT err_code FROM ATM_SMS_SEND WHERE ATM='" + atm + "'";
            rs = stmnt.executeQuery(query);

            if (rs.next()) {
                if (rs.getString("err_code").trim().equals(err.trim())) {
                    errFound = true;
                } else {
                    errFound = false;
                }
            } else {
                errFound = false;
            }
            rs.close();
            stmnt.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return errFound;
    }

    public String genarateSMSTxt(String atm, String err, String location, int protocol, String divType) {
        String smsBr = "";

        try {
            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            String txnDate = (new java.sql.Timestamp(t1)).toString();
            txnDate = txnDate.substring(0, txnDate.length() - 2);

            int cat1 = Integer.parseInt(err.split(" ")[0].trim());
            int cat2 = Integer.parseInt(err.split(" ")[1].trim());
            int cat3 = Integer.parseInt(err.split(" ")[2].trim());
            int cat4 = Integer.parseInt(err.split(" ")[3].trim());
            int rcpt = Integer.parseInt(err.split(" ")[4].trim());
            int pinpad = Integer.parseInt(err.split(" ")[5].trim());
            int mDrawer = Integer.parseInt(err.split(" ")[6].trim());
            int cardReader = Integer.parseInt(err.split(" ")[7].trim());
            int stat = Integer.parseInt(err.split(" ")[8].trim());
            int state = Integer.parseInt(err.split(" ")[9].trim());

            smsBr = atm + "(" + location + ") is faulted - ";

            if (protocol == 1) {
                for (int i = 0; i < 4; i++) {
                    if (Integer.parseInt(err.split(" ")[i].trim()) == 1) {
                        smsBr = smsBr + "CST" + (i + 1) + ": LOW CASH, ";
                    }
                    if (Integer.parseInt(err.split(" ")[i].trim()) == 2) {
                        smsBr = smsBr + "CST" + (i + 1) + ": EMPTY, ";
                    }
                    if (Integer.parseInt(err.split(" ")[i].trim()) == 9) {
                        smsBr = smsBr + "CST" + (i + 1) + ": FAULT, ";
                    }
                }

                if (stat == 0 || state == 0) {
                    smsBr = smsBr + "STATUS: DOWN,";
                }
                if (rcpt == 1) {
                    smsBr = smsBr + "RCPT PRNT: PAPER LOW,";
                }
                if (rcpt == 2) {
                    smsBr = smsBr + "RCPT PRNT: PAPER OUT,";
                }
                if (rcpt == 3) {
                    smsBr = smsBr + "RCPT PRNT: FAULT,";
                }
                if (pinpad == 1) {
                    smsBr = smsBr + "PIN PAD: ERROR,";
                }

                if (divType.equals("ATM")) {
                    if (mDrawer == 1) {
                        smsBr = smsBr + " MONEY DRAWER: FAULT,";
                    }
                    if (mDrawer == 2) {
                        smsBr = smsBr + " MONEY IN DRAWER: YES,";
                    }
                } else {
                    if (!(mDrawer == 0 || mDrawer == 1)) {
                        smsBr = smsBr + " CURRENCY ACCEPTOR: FAULT,";
                    }
                }
                if (cardReader != 0) {
                    smsBr = smsBr + " CARD READER: FAULT,";
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    if (Integer.parseInt(err.split(" ")[i].trim()) == 2) {
                        smsBr = smsBr + "CST" + (i + 1) + ": LOW CASH, ";
                    }
                    if (Integer.parseInt(err.split(" ")[i].trim()) == 3) {
                        smsBr = smsBr + "CST" + (i + 1) + ": EMPTY, ";
                    }
                    if (Integer.parseInt(err.split(" ")[i].trim()) == 0) {
                        smsBr = smsBr + "CST" + (i + 1) + ": NOT PRESENT, ";
                    }
                }
                if (stat == 0 || state == 0) {
                    smsBr = smsBr + "STATUS: DOWN,";
                }
                if (rcpt == 2) {
                    smsBr = smsBr + "RCPT PRNT: PAPER LOW,";
                }
                if (rcpt == 3) {
                    smsBr = smsBr + "RCPT PRNT: PAPER OUT/FAULT,";
                }
                if (pinpad == 4) {
                    smsBr = smsBr + "PIN PAD: ERROR,";
                }
                if (mDrawer == 140) {
                    smsBr = smsBr + " CURRENCY ACCEPTOR: FAULT,";
                }
                if (mDrawer == 440) {
                    smsBr = smsBr + " REJECT BIN: OVER FILL,";
                }
                if (cardReader != 0) {
                    smsBr = smsBr + " CARD READER: FAULT,";
                }
            }

            smsBr = smsBr.substring(0, smsBr.length() - 1) + " as @ " + txnDate + ".Please Attend. Thanks";

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return smsBr;
    }

    public String getATMCstDetails(String atm) {

        String atmDet = "";

        try {
            conn = dbconnect.dbConnect_Local();
            stmnt = conn.createStatement();

            String query = "select billval,cashincr,cashout,endcash from atm_cashpo where "
                    + "atm = '" + atm + "'";
            rs = stmnt.executeQuery(query);

            if (rs.next()) {
                atmDet = rs.getString(1) + "#" + rs.getString(2) + "#" + rs.getString(3) + "#" + rs.getString(4);
            }
            rs.close();
            stmnt.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return atmDet;
    }

    public int updateATMCashOut(String atm, String txtOut, String txtEnd) {
        int up = 0;
        try {

            DBConnect obj = new DBConnect();
            conn = obj.dbConnect_Local();

            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            String updateQry = "UPDATE ATM_CASHPO SET CASHOUT=?,ENDCASH=?,TIMESTAMP=? "
                    + "WHERE ATM=?";

            PreparedStatement pstmt = conn.prepareStatement(updateQry);
            pstmt.setString(1, txtOut);
            pstmt.setString(2, txtEnd);
            pstmt.setTimestamp(3, new java.sql.Timestamp(t1));
            pstmt.setString(4, atm);

            if (pstmt.executeUpdate() > 0) {
                up = 1;
                pstmt.close();
            }
            obj.releaseCon(conn);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return up;
    }

    public int updateATMCashIn(String atm, String txtIn, String txtEnd) {
        int up = 0;
        try {

            DBConnect obj = new DBConnect();
            conn = obj.dbConnect_Local();

            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            String updateQry = "UPDATE ATM_CASHPO SET CASHINCR=?,ENDCASH=?,TIMESTAMP=? "
                    + "WHERE ATM=?";

            PreparedStatement pstmt = conn.prepareStatement(updateQry);
            pstmt.setString(1, txtIn);
            pstmt.setString(2, txtEnd);
            pstmt.setTimestamp(3, new java.sql.Timestamp(t1));
            pstmt.setString(4, atm);

            if (pstmt.executeUpdate() > 0) {
                up = 1;
                pstmt.close();
            }
            obj.releaseCon(conn);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return up;
    }
    
    public int cashLoading(String txt) {
        int up = 0;
        try {
            String atm = txt.split("#")[0];
            String cashLoading = txt.split("#")[1];  
            String emptyCash = "0~0~0~0";
            String defaultBillVal = "100~500~1000~5000";

            DBConnect obj = new DBConnect();
            conn = obj.dbConnect_Local();

            LocalDateTime now = LocalDateTime.now();
            Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dtf.format(now));
            long t1 = logDate.getTime();

            String updateQry = "UPDATE ATM_CASHPO SET BEGCASH=?,CASHINCR=?,CASHOUT=?,ENDCASH=?,TIMESTAMP=? "
                    + "WHERE ATM=?";

            PreparedStatement pstmt = conn.prepareStatement(updateQry);
            pstmt.setString(1, cashLoading);
            pstmt.setString(2, emptyCash);
            pstmt.setString(3, emptyCash);
            pstmt.setString(4, cashLoading);
            pstmt.setTimestamp(5, new java.sql.Timestamp(t1));
            pstmt.setString(6, atm);

            if (pstmt.executeUpdate() <= 0) {
                String insertQry = "INSERT INTO ATM_CASHPO VALUES(?,?,?,?,?,?,?)";
                PreparedStatement pstmt1 = conn.prepareStatement(insertQry);

                pstmt1.setString(1, atm);
                pstmt1.setString(2, defaultBillVal);
                pstmt1.setString(3, cashLoading);
                pstmt1.setString(4, emptyCash);
                pstmt1.setString(5, emptyCash);
                pstmt1.setString(6, cashLoading);
                pstmt1.setTimestamp(7, new java.sql.Timestamp(t1));

                if (pstmt1.executeUpdate() > 0) {
                    up = 1;
                }
                pstmt1.close();
            } else {
                up = 1;
                pstmt.close();
            }
            obj.releaseCon(conn);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return up;
    }

}
