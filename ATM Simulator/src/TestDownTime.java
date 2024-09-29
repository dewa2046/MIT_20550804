
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author it207440
 */
public class TestDownTime {

    public Connection connection = null;
    public Statement stmnt = null;
    public ResultSet rs = null;

    DecimalFormat df = new DecimalFormat("0.00");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");

    Calendar cal = Calendar.getInstance();

    public static void main(String[] args) {
        TestDownTime test = new TestDownTime();
//        test.getAllDownTimeReport("2024-03-19", "2024-03-20", 3 , 681); //branch
        test.getAllDownTimeReport("2024-03-19", "2024-03-20", 1, 0);
    }

    public String getAllDownTimeReport(String fromDate, String toDate, int userLvl, int uBr) {
        String ResultJson = "\"\"";

        try {
            DBConnect obj = new DBConnect();
            connection = obj.dbConnect_Local();

            try {
                cal.setTime(sdf.parse(toDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            cal.add(Calendar.DAY_OF_MONTH, 1);
            String endDate = sdf.format(cal.getTime());

            String sql = "";

            if (userLvl != 3) {
                sql = "select s.*,A.DIV_TYPE,V.VENDER_NAME,A.BR_CODE,A.LOCATION,A.PROTOCOL from ATM_STS_DESC_SUMMARY s,ATM_INFO a,VENDER_DETAILS v "
                        + "where s.station = a.atm_name and a.vender = v.vender_id and "
                        + "s.flt_date between TO_DATE( '" + fromDate + "', 'yyyy-mm-dd') and TO_DATE('" + endDate + "', 'yyyy-mm-dd') "
                        + "order by s.station,s.flt_date";
            } else {
                sql = "select s.*,A.DIV_TYPE,V.VENDER_NAME,A.BR_CODE,A.LOCATION,A.PROTOCOL from ATM_STS_DESC_SUMMARY s,ATM_INFO a,VENDER_DETAILS v "
                        + "where s.station = a.atm_name and a.vender = v.vender_id and "
                        + "s.flt_date between TO_DATE( '" + fromDate + "', 'yyyy-mm-dd') and TO_DATE('" + endDate + "', 'yyyy-mm-dd') "
                        + "and a.BR_CODE = " + uBr + " "
                        + "order by s.station,s.flt_date";
            }
            stmnt = connection.createStatement();
            rs = stmnt.executeQuery(sql);
            ArrayList<String> atmDetails = new ArrayList<String>();

            while (rs.next()) {
                String atm = rs.getString(1).trim();
                String err = rs.getString(2).trim();
                String breakTime = rs.getString(3).trim();
                String div = rs.getString(4).trim();
                String vender = rs.getString(5).trim();
                String brcode = rs.getString(6).trim();
                String location = "";
                if (rs.getString(7) != null) {
                    location = rs.getString(7);
                } else {
                    location = "__________";
                }
                int protocol = Integer.parseInt(rs.getString(8).trim());

                if (div.equals("CRM") && protocol == 2) {
                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "NDCCRM" + "~" + brcode + "~" + location + "~" + breakTime + "~" + div + "~" + vender);

                } else if ((div.equals("CRM")) && protocol == 1) {
                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "DDCCRM" + "~" + brcode + "~" + location + "~" + breakTime + "~" + div + "~" + vender);

                } else if ((div.equals("CDM")) && protocol == 1) {
                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "DDCCDM" + "~" + brcode + "~" + location + "~" + breakTime + "~" + div + "~" + vender);

                } else if ((div.equals("ATM")) && protocol == 1) {
                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "DDCATM" + "~" + brcode + "~" + location + "~" + breakTime + "~" + div + "~" + vender);
                }
            }

            rs.close();
            stmnt.close();
            obj.releaseCon(connection);

            Date date = new Date();
            ArrayList<String> atmDetailsEdit = new ArrayList<String>();
            TestDownTime AllDownTimeQuery = new TestDownTime();

            for (int i = 0; i < atmDetails.size(); i++) {
                String atm = atmDetails.get(i).split("~")[0];
                String errCode = atmDetails.get(i).split("~")[1];
                String brkTime = atmDetails.get(i).split("~")[2];
                String type = atmDetails.get(i).split("~")[3];
                Integer brcode = Integer.parseInt(atmDetails.get(i).split("~")[4]);
                String location = atmDetails.get(i).split("~")[5];
                String brkTimeNetwork = atmDetails.get(i).split("~")[6];
                String div = atmDetails.get(i).split("~")[7];
                String vender = atmDetails.get(i).split("~")[8];

                String cstEmpty = errCode.substring(0, 7).trim();
                String cstFault = errCode.substring(0, 7).trim();
                String downFault = errCode.split(" ")[8];
                String closeFault = errCode.split(" ")[9];
                String pinFault = errCode.split(" ")[5];
                String moneyDrwFault = errCode.split(" ")[6];
                String cardFault = errCode.split(" ")[7];
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return ResultJson;
    }

    public double getHourDifference(String dateSt, String dateStop) {

        double Hours = 0.0;
        double minutes = 0.0;
        double diffTime = 0.0;

        try {
            String dateStart = dateSt;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = null;
            Date d2 = null;
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);
            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            Hours = diff / (60 * 60 * 1000);
            minutes = (diff / (60 * 1000) % 60) / 60.0;
            diffTime = Hours + minutes;

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return diffTime;
    }
}
