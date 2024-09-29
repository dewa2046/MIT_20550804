/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reports;

import BackEnd.DBConnect;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class ReportsQuery {

    public Connection connection = null;
    public Statement stmnt = null;
    public ResultSet rs = null;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    String connectionTypeLocal = "local";

    DecimalFormat df = new DecimalFormat("0.00");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");

    Calendar cal = Calendar.getInstance();

    public String getProvinceDetails() {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "select province_id,province_name from PROVINCE_DETAILS order by province_name";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String getATMInfoReport(String qry) {

        String ResultJson = "\"\"";

        try {
            String sql = "";

            sql = "select ATM_NAME,a.BR_CODE,BR_NAME,LOCATION,PROVINCE_NAME,OS,DIV_TYPE,EMV,RECYCLER,REMOTE,OVERSEAS,MODEL_NAME,VENDER_NAME "
                    + "from ATM_INFO a, BRANCH_DETAILS b,ATM_MODELS m,VENDER_DETAILS v,PROVINCE_DETAILS p "
                    + "where a.br_code = b.br_code and a.model = m.model_id and a.vender = v.vender_id and b.province_id = p.province_id "
                    + qry + " a.status = 1 order by a.br_code";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

//************************************
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
                        + "order by s.station,s.updated_time";
            } else {
                sql = "select s.*,A.DIV_TYPE,V.VENDER_NAME,A.BR_CODE,A.LOCATION,A.PROTOCOL from ATM_STS_DESC_SUMMARY s,ATM_INFO a,VENDER_DETAILS v "
                        + "where s.station = a.atm_name and a.vender = v.vender_id and "
                        + "s.flt_date between TO_DATE( '" + fromDate + "', 'yyyy-mm-dd') and TO_DATE('" + endDate + "', 'yyyy-mm-dd') "
                        + "and a.BR_CODE = " + uBr + " "
                        + "order by s.station,s.updated_time";
            }

            stmnt = connection.createStatement();
            rs = stmnt.executeQuery(sql);

            ArrayList<String> atmDetails = new ArrayList<String>();

            while (rs.next()) {
                String atm = rs.getString(1).trim();
                String err = rs.getString(2).trim();
                String breakTime = rs.getString(3).trim();
                String networkTime = rs.getString(4).trim();
                String div = rs.getString(6).trim();
                String vender = rs.getString(7).trim();
                String brcode = rs.getString(8).trim();
                String location = "";
                if (rs.getString(9) != null) {
                    location = rs.getString(9);
                } else {
                    location = "__________";
                }
                int protocol = Integer.parseInt(rs.getString(10).trim());

                if (div.equals("CRM") && protocol == 2) {
                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "NDCCRM" + "~" + brcode + "~" + location + "~" + networkTime + "~" + div + "~" + vender);

                } else if ((div.equals("CRM")) && protocol == 1) {
                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "DDCCRM" + "~" + brcode + "~" + location + "~" + networkTime + "~" + div + "~" + vender);

                } else if ((div.equals("CDM")) && protocol == 1) {
                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "DDCCDM" + "~" + brcode + "~" + location + "~" + networkTime + "~" + div + "~" + vender);

                } else if ((div.equals("ATM")) && protocol == 1) {
                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "DDCATM" + "~" + brcode + "~" + location + "~" + networkTime + "~" + div + "~" + vender);
                }
            }
            rs.close();
            stmnt.close();
            obj.releaseCon(connection);

            Date date = new Date();
            ArrayList<String> atmDetailsEdit = new ArrayList<String>();
            ReportsQuery AllDownTimeQuery = new ReportsQuery();

            String atmNext = "";
            int startindex = 0;
            int totErrors = 0;

            int totcstEmpty = 0;
            int totcstFault = 0;
            int totpinFault = 0;
            int totcardFault = 0;
            int totmoneyDrwFault = 0;
            int totdownColseFault = 0;
            int totdownFault = 0;
            int totcardCloseFault = 0;

            float totcstEmptyTime = 0;
            float totcstFaultTime = 0;
            float totpinFaultTime = 0;
            float totcardFaultTime = 0;
            float totmoneyDrwFaultTime = 0;
            float totdownColseFaultTime = 0;
            float totdownFaultTime = 0;
            float totcardCloseFaultTime = 0;

            String cstEmpty;
            String cstEmptyStartTime = "";
            String cstEmptyEndTime = "";
            String cstEmptyStartTimeFirst = "";
            String NextCstEmpty = "";
            String cstEmptyStr = "";
            int CstEmptyCount = 0;

            String cstFault;
            String cstFaultStartTime = "";
            String cstFaultEndTime = "";
            String cstFaultStartTimeFirst = "";
            String NextCstFault = "";
            String cstFaultStr = "";
            int CstFaultCount = 0;

            String pinFault;
            String pinFaultStartTime = "";
            String pinFaultEndTime = "";
            String pinFaultStartTimeFirst = "";
            String NextPinFault = "";
            String pinFaultStr = "";
            int pinFaultCount = 0;

            String cardFault;
            String cardFaultStartTime = "";
            String cardFaultEndTime = "";
            String cardFaultStartTimeFirst = "";
            String NextCardFault = "";
            String cardFaultStr = "";
            int cardFaultCount = 0;

            String moneyDrwFault;
            String moneyDrwFaultStartTime = "";
            String moneyDrwFaultEndTime = "";
            String moneyDrwFaultStartTimeFirst = "";
            String NextMoneyDrwFault = "";
            String moneyDrwFaultStr = "";
            int moneyDrwFaultCount = 0;

            String downColseFault;
            String downColseFaultStartTime = "";
            String downColseFaultEndTime = "";
            String downColseFaultStartTimeFirst = "";
            String NextDownColseFault = "";
            String downColseFaultStr = "";
            int downColseFaultCount = 0;

            String downFault;
            String downFaultStartTime = "";
            String downFaultEndTime = "";
            String downFaultStartTimeFirst = "";
            String NextDownFault = "";
            String downFaultStr = "";
            int downFaultCount = 0;

            String cardCloseFault;
            String cardCloseFaultStartTime = "";
            String cardCloseFaultEndTime = "";
            String cardCloseFaultStartTimeFirst = "";
            String NextCardCloseFault = "";
            String cardCloseFaultStr = "";
            int cardCloseFaultCount = 0;

            String closeFault;
            String closeFaultStartTime = "";
            String closeFaultEndTime = "";
            String closeFaultStartTimeFirst = "";
            String NextCloseFault = "";
            String closeFaultStr = "";
            int closeFaultCount = 0;

//            String stat;
//            String state;
//            int networkTimeDiff = 20;
//            int downTimeTimeDiff = 10;
            int networkTimeDiff = 5;
            int downTimeTimeDiff = 5;

            for (int i = 0; i < atmDetails.size(); i++) {
                try {
                    String atm = atmDetails.get(i).split("~")[0];
                    String errCode = atmDetails.get(i).split("~")[1];
                    String brkTime = atmDetails.get(i).split("~")[2];
                    String type = atmDetails.get(i).split("~")[3];
                    Integer brcode = Integer.parseInt(atmDetails.get(i).split("~")[4]);
                    String location = atmDetails.get(i).split("~")[5];
                    String brkTimeNetwork = atmDetails.get(i).split("~")[6];
                    String div = atmDetails.get(i).split("~")[7];
                    String vender = atmDetails.get(i).split("~")[8];

                    cstEmpty = errCode.substring(0, 7).trim();
                    cstFault = errCode.substring(0, 7).trim();
                    downFault = errCode.split(" ")[4];
                    closeFault = errCode.split(" ")[5];
                    pinFault = errCode.split(" ")[8];
                    moneyDrwFault = errCode.split(" ")[9];
                    cardFault = errCode.split(" ")[10];

                    downColseFault = downFault + " " + closeFault;

                    if (i != atmDetails.size() - 1) {
                        atmNext = atmDetails.get(i + 1).split("~")[0];
                        NextCstEmpty = atmDetails.get(i + 1).split("~")[1].substring(0, 7).trim();
                        NextCstFault = atmDetails.get(i + 1).split("~")[1].substring(0, 7).trim();
                        NextPinFault = atmDetails.get(i + 1).split("~")[1].split(" ")[8];
                        NextCardFault = atmDetails.get(i + 1).split("~")[1].split(" ")[10];
                        NextMoneyDrwFault = atmDetails.get(i + 1).split("~")[1].split(" ")[9];
                        NextDownColseFault = atmDetails.get(i + 1).split("~")[1].split(" ")[4] + " " + atmDetails.get(i + 1).split("~")[1].split(" ")[5]; // down or closed
                        NextDownFault = atmDetails.get(i + 1).split("~")[1].split(" ")[4]; // down
                        NextCardCloseFault = atmDetails.get(i + 1).split("~")[1].split(" ")[10] + " " + atmDetails.get(i + 1).split("~")[1].split(" ")[5]; // card fault and closed
                        NextCloseFault = atmDetails.get(i + 1).split("~")[1].split(" ")[5]; // down
                    } else if (i == atmDetails.size() - 1) {
                        atmNext = "";
                        NextCstEmpty = "";
                        NextCstFault = "";
                        NextPinFault = "";
                        NextCardFault = "";
                        NextMoneyDrwFault = "";
                        NextDownColseFault = "";
                        NextDownFault = "";
                        NextCardCloseFault = "";
                        NextCloseFault = "";
                    }

                    //Start Time
                    if (startindex == 0) {
                        //All Cassette Empty
                        if ((cstEmpty.equals("2 2 2 2") && !type.equals("NDCCRM")) || (cstEmpty.equals("3 3 3 3") && type.equals("NDCCRM"))) {
//                            cstEmptyStartTime = fromDate + " 00:00:00";
                            cstEmptyStartTime = brkTime;
                        } else {
                            cstEmptyStartTime = brkTime;
                        }

                        //All Cassette Fault
                        if ((cstFault.equals("9 9 9 9") && !type.equals("NDCCRM")) || (cstFault.equals("0 0 0 0") && type.equals("NDCCRM"))) {
//                            cstFaultStartTime = fromDate + " 00:00:00";
                            cstFaultStartTime = brkTime;
                        } else {
                            cstFaultStartTime = brkTime;
                        }

                        //PinPad Fault
                        if ((pinFault.equals("1") && !type.equals("NDCCRM")) || (pinFault.equals("4") && type.equals("NDCCRM"))) {
//                            pinFaultStartTime = fromDate + " 00:00:00";
                            pinFaultStartTime = brkTime;
                        } else {
                            pinFaultStartTime = brkTime;
                        }

                        //CardReader Fault
                        if ((!cardFault.equals("0") && !type.equals("NDCCRM")) || (!cardFault.equals("0") && type.equals("NDCCRM"))) {
//                            cardFaultStartTime = fromDate + " 00:00:00";
                            cardFaultStartTime = brkTime;
                        } else {
                            cardFaultStartTime = brkTime;
                        }

                        //ModeyDrawer Fault
                        if (((moneyDrwFault.equals("1") || moneyDrwFault.equals("2")) && type.equals("DDCATM"))
                                || (!(moneyDrwFault.equals("0") || moneyDrwFault.equals("1")) && (type.equals("DDCCDM") || type.equals("DDCCRM")))
                                || ((moneyDrwFault.equals("140") || moneyDrwFault.equals("440")) && type.equals("NDCCRM"))) {
//                            moneyDrwFaultStartTime = fromDate + " 00:00:00";
                            moneyDrwFaultStartTime = brkTime;
                        } else {
                            moneyDrwFaultStartTime = brkTime;
                        }

                        //Down/Close Fault
                        if (downColseFault.equals("0 0") || downColseFault.equals("0 1") || downColseFault.equals("1 0")) {
//                            downColseFaultStartTime = fromDate + " 00:00:00";
                            downColseFaultStartTime = brkTimeNetwork;
                        } else {
                            downColseFaultStartTime = brkTimeNetwork;
                        }

                        //Down Fault
                        if (downFault.equals("0")) {
//                            downFaultStartTime = fromDate + " 00:00:00";
                            downFaultStartTime = brkTimeNetwork;
                        } else {
                            downFaultStartTime = brkTimeNetwork;
                        }

                        //CardReader Fault + Closed
                        if (((!cardFault.equals("0") && !type.equals("NDCCRM")) || (!cardFault.equals("0") && type.equals("NDCCRM"))) && closeFault.equals("0")) {
//                            cardCloseFaultStartTime = fromDate + " 00:00:00";
                            cardCloseFaultStartTime = brkTimeNetwork;
                        } else {
                            cardCloseFaultStartTime = brkTimeNetwork; //use the networktime because brktime has the card reader fault time
                        }

                    } else {
                        cstEmptyStartTime = brkTime;
                        cstFaultStartTime = brkTime;
                        pinFaultStartTime = brkTime;
                        cardFaultStartTime = brkTime;
                        moneyDrwFaultStartTime = brkTime;
                        downColseFaultStartTime = brkTimeNetwork;
                        downFaultStartTime = brkTimeNetwork;
                        cardCloseFaultStartTime = brkTimeNetwork; //use the networktime because brktime has the card reader fault time
                    }

                    //End Time
                    if (!atm.equals(atmNext)) {
                        //cassette empty
                        if ((cstEmpty.equals("2 2 2 2") && !type.equals("NDCCRM")) || (cstEmpty.equals("3 3 3 3") && type.equals("NDCCRM"))) {
                            if (sdf.format(date).equals(toDate)) {
                                cstEmptyEndTime = toDate + " " + sdf2.format(date);
                            } else {
                                cstEmptyEndTime = toDate + " 24:00:00";
                            }

                        } else {
                            cstEmptyEndTime = brkTime;
                        }

                        //cassete fault
                        if ((cstFault.equals("9 9 9 9") && !type.equals("NDCCRM")) || (cstFault.equals("0 0 0 0") && type.equals("NDCCRM"))) {
                            if (sdf.format(date).equals(toDate)) {
                                cstFaultEndTime = toDate + " " + sdf2.format(date);
                            } else {
                                cstFaultEndTime = toDate + " 24:00:00";
                            }
                        } else {
                            cstFaultEndTime = brkTime;
                        }
//                        
                        //PinPad fault
                        if ((pinFault.equals("1") && !type.equals("NDCCRM")) || (pinFault.equals("4") && type.equals("NDCCRM"))) {
                            if (sdf.format(date).equals(toDate)) {
                                pinFaultEndTime = toDate + " " + sdf2.format(date);
                            } else {
                                pinFaultEndTime = toDate + " 24:00:00";
                            }
                        } else {
                            pinFaultEndTime = brkTime;
                        }
//                        
                        //CardReader fault
                        if ((!cardFault.equals("0") && !type.equals("NDCCRM")) || (!cardFault.equals("0") && type.equals("NDCCRM"))) {
                            if (sdf.format(date).equals(toDate)) {
                                cardFaultEndTime = toDate + " " + sdf2.format(date);
                            } else {
                                cardFaultEndTime = toDate + " 24:00:00";
                            }
                        } else {
                            cardFaultEndTime = brkTime;
                        }

                        //MoneyDrawer fault
                        if (((moneyDrwFault.equals("1") || moneyDrwFault.equals("2")) && type.equals("DDCATM"))
                                || (!(moneyDrwFault.equals("0") || moneyDrwFault.equals("1")) && (type.equals("DDCCDM") || type.equals("DDCCRM")))
                                || ((moneyDrwFault.equals("140") || moneyDrwFault.equals("440")) && type.equals("NDCCRM"))) {
                            if (sdf.format(date).equals(toDate)) {
                                moneyDrwFaultEndTime = toDate + " " + sdf2.format(date);
                            } else {
                                moneyDrwFaultEndTime = toDate + " 24:00:00";
                            }
                        } else {
                            moneyDrwFaultEndTime = brkTime;
                        }

                        //Down/Closed fault
                        if (downFault.equals("0") || closeFault.equals("0")) {
                            if (sdf.format(date).equals(toDate)) {
                                downColseFaultEndTime = toDate + " " + sdf2.format(date);
                            } else {
                                downColseFaultEndTime = toDate + " 24:00:00";
                            }
                        } else {
                            downColseFaultEndTime = brkTimeNetwork;
                        }

                        //Down fault
                        if (downFault.equals("0")) {
                            if (sdf.format(date).equals(toDate)) {
                                downFaultEndTime = toDate + " " + sdf2.format(date);
                            } else {
                                downFaultEndTime = toDate + " 24:00:00";
                            }
                        } else {
                            downFaultEndTime = brkTimeNetwork;
                        }

                        //CardReader fault + closed
                        if (((!cardFault.equals("0") && !type.equals("NDCCRM")) || (!cardFault.equals("0") && type.equals("NDCCRM"))) && closeFault.equals("0")) {
                            if (sdf.format(date).equals(toDate)) {
                                cardCloseFaultEndTime = toDate + " " + sdf2.format(date);
                            } else {
                                cardCloseFaultEndTime = toDate + " 24:00:00";
                            }
                        } else {
                            cardCloseFaultEndTime = brkTimeNetwork; //use the networktime because brktime has the card reader fault time
                        }

                    } else {
                        if (i != atmDetails.size() - 1) {
                            cstEmptyEndTime = atmDetails.get(i + 1).split("~")[2];
                            cstFaultEndTime = atmDetails.get(i + 1).split("~")[2];
                            pinFaultEndTime = atmDetails.get(i + 1).split("~")[2];
                            cardFaultEndTime = atmDetails.get(i + 1).split("~")[2];
                            moneyDrwFaultEndTime = atmDetails.get(i + 1).split("~")[2];
                            downColseFaultEndTime = atmDetails.get(i + 1).split("~")[6];
                            downFaultEndTime = atmDetails.get(i + 1).split("~")[6];
                            cardCloseFaultEndTime = atmDetails.get(i + 1).split("~")[6];//use the networktime because brktime has the card reader fault time
                        } else {
                            cstEmptyEndTime = brkTime;
                            cstFaultEndTime = brkTime;
                            pinFaultEndTime = brkTime;
                            cardFaultEndTime = brkTime;
                            moneyDrwFaultEndTime = brkTime;
                            downColseFaultEndTime = brkTimeNetwork;
                            downFaultEndTime = brkTimeNetwork;
                            cardCloseFaultEndTime = brkTimeNetwork;//use the networktime because brktime has the card reader fault time
                        }
                    }

                    //Calculate
                    //Not HITACHI CRM
                    if (!type.equals("NDCCRM")) {
                        //cassette Empty
                        if (cstEmpty.equals("2 2 2 2") || cstEmpty.equals("3 3 3 3")) {
                            if (cstEmpty.equals(NextCstEmpty)) {
                                CstEmptyCount++;
                                if (CstEmptyCount == 1) {
                                    cstEmptyStartTimeFirst = cstEmptyStartTime;
                                }
                            } else {
                                if (CstEmptyCount == 0) {
                                    cstEmptyStartTimeFirst = cstEmptyStartTime;
                                }

                                if ((AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60) > downTimeTimeDiff) {
                                    cstEmptyStr += cstEmptyStartTimeFirst.substring(2, cstEmptyStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + cstEmptyEndTime.substring(2, cstEmptyEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totcstEmpty++;
                                    totcstEmptyTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60));
                                }

                                CstEmptyCount = 0;
                                cstEmptyStartTimeFirst = "N/A";

                            }
                        }

                        //cassette Fault
                        if (cstFault.equals("9 9 9 9")) {
                            if (cstFault.equals(NextCstFault)) {
                                CstFaultCount++;
                                if (CstFaultCount == 1) {
                                    cstFaultStartTimeFirst = cstFaultStartTime;
                                }
                            } else {
                                if (CstFaultCount == 0) {
                                    cstFaultStartTimeFirst = cstFaultStartTime;
                                }

                                if ((AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60) > downTimeTimeDiff) {
                                    cstFaultStr += cstFaultStartTimeFirst.substring(2, cstFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + cstFaultEndTime.substring(2, cstFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totcstFault++;
                                    totcstFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60));
                                }

                                CstFaultCount = 0;
                                cstFaultStartTimeFirst = "N/A";
                            }
                        }

                        //PinPad Fault
                        if (pinFault.equals("1")) {
                            if (pinFault.equals(NextPinFault)) {
                                pinFaultCount++;
                                if (pinFaultCount == 1) {
                                    pinFaultStartTimeFirst = pinFaultStartTime;
                                }
                            } else {
                                if (pinFaultCount == 0) {
                                    pinFaultStartTimeFirst = pinFaultStartTime;
                                }
                                if ((AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60) > downTimeTimeDiff) {
                                    pinFaultStr += pinFaultStartTimeFirst.substring(2, pinFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + pinFaultEndTime.substring(2, pinFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totpinFault++;
                                    totpinFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60));
                                }

                                pinFaultCount = 0;
                                pinFaultStartTimeFirst = "N/A";
                            }
                        }
//                        
                        //CardReader Fault
                        if (!cardFault.equals("0")) {
                            if (cardFault.equals(NextCardFault)) {
                                cardFaultCount++;
                                if (cardFaultCount == 1) {
                                    cardFaultStartTimeFirst = cardFaultStartTime;
                                }
                            } else {
                                if (cardFaultCount == 0) {
                                    cardFaultStartTimeFirst = cardFaultStartTime;
                                }
                                if ((AllDownTimeQuery.getHourDifference(cardFaultStartTimeFirst, cardFaultEndTime) * 60) > downTimeTimeDiff) {
                                    cardFaultStr += cardFaultStartTimeFirst.substring(2, cardFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + cardFaultEndTime.substring(2, cardFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(cardFaultStartTimeFirst, cardFaultEndTime) * 60) + "(min)~";
//                                    totErrors++;
                                    totcardFault++;
                                    totcardFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cardFaultStartTimeFirst, cardFaultEndTime) * 60));
                                }

                                cardFaultCount = 0;
                                cardFaultStartTimeFirst = "N/A";
                            }
                        }

                        //MoneyDrawer Fault
                        if (((moneyDrwFault.equals("1") || moneyDrwFault.equals("2")) && type.equals("DDCATM"))
                                || (!(moneyDrwFault.equals("0") || moneyDrwFault.equals("1")) && (type.equals("DDCCDM") || type.equals("DDCCRM")))) {
                            if (moneyDrwFault.equals(NextMoneyDrwFault)) {
                                moneyDrwFaultCount++;
                                if (moneyDrwFaultCount == 1) {
                                    moneyDrwFaultStartTimeFirst = moneyDrwFaultStartTime;
                                }
                            } else {
                                if (moneyDrwFaultCount == 0) {
                                    moneyDrwFaultStartTimeFirst = moneyDrwFaultStartTime;
                                }

                                if ((AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60) > downTimeTimeDiff) {
                                    moneyDrwFaultStr += moneyDrwFaultStartTimeFirst.substring(2, moneyDrwFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + moneyDrwFaultEndTime.substring(2, moneyDrwFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totmoneyDrwFault++;
                                    totmoneyDrwFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60));
                                }

                                moneyDrwFaultCount = 0;
                                moneyDrwFaultStartTimeFirst = "N/A";
                            }
                        }

                        //Down/Close Fault
                        if (downFault.equals("0") || closeFault.equals("0")) {
                            if (NextDownColseFault.equals("0 0") || NextDownColseFault.equals("0 1") || NextDownColseFault.equals("1 0")) {
                                downColseFaultCount++;
                                if (downColseFaultCount == 1) {
                                    downColseFaultStartTimeFirst = downColseFaultStartTime;
                                }
                            } else {
                                if (downColseFaultCount == 0) {
                                    downColseFaultStartTimeFirst = downColseFaultStartTime;
                                }

                                //Taking only morethan 20 min
                                if ((AllDownTimeQuery.getHourDifference(downColseFaultStartTimeFirst, downColseFaultEndTime) * 60) > downTimeTimeDiff) {
                                    downColseFaultStr += downColseFaultStartTimeFirst.substring(2, downColseFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + downColseFaultEndTime.substring(2, downColseFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(downColseFaultStartTimeFirst, downColseFaultEndTime) * 60) + "(min)~";
//                                    totErrors++;
                                    totdownColseFault++;
                                    totdownColseFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(downColseFaultStartTimeFirst, downColseFaultEndTime) * 60));

                                }
                                downColseFaultCount = 0;
                                downColseFaultStartTimeFirst = "N/A";

                            }
                        }

                        //Down Fault
                        if (downFault.equals("0")) {
                            if (downFault.equals(NextDownFault)) {
                                downFaultCount++;
                                if (downFaultCount == 1) {
                                    downFaultStartTimeFirst = downFaultStartTime;
                                }
                            } else {
                                if (downFaultCount == 0) {
                                    downFaultStartTimeFirst = downFaultStartTime;
                                }

                                //Taking only morethan 20 min
                                if ((AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60) > downTimeTimeDiff) {
                                    downFaultStr += downFaultStartTimeFirst.substring(2, downFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + downFaultEndTime.substring(2, downFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totdownFault++;
                                    totdownFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60));

                                }
                                downFaultCount = 0;
                                downFaultStartTimeFirst = "N/A";

                            }
                        }

                        //CardReader Fault + close
                        if (!cardFault.equals("0") && closeFault.equals("0")) {
                            if (cardFault.equals(NextCardFault) && closeFault.equals(NextCloseFault)) {
                                cardCloseFaultCount++;
                                if (cardCloseFaultCount == 1) {
                                    cardCloseFaultStartTimeFirst = cardCloseFaultStartTime;
                                }
                            } else {
                                if (cardCloseFaultCount == 0) {
                                    cardCloseFaultStartTimeFirst = cardCloseFaultStartTime;
                                }
                                if ((AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60) > downTimeTimeDiff) {
                                    cardCloseFaultStr += cardCloseFaultStartTimeFirst.substring(2, cardCloseFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + cardCloseFaultEndTime.substring(2, cardCloseFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totcardCloseFault++;
                                    totcardCloseFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60));
                                }

                                cardCloseFaultCount = 0;
                                cardCloseFaultStartTimeFirst = "N/A";
                            }
                        }

                    } //HITACHI CRM
                    else if (type.equals("NDCCRM")) {
                        //cassette Empty
                        if (cstEmpty.equals("3 3 3 3")) {
                            if (cstEmpty.equals(NextCstEmpty)) {
                                CstEmptyCount++;
                                if (CstEmptyCount == 1) {
                                    cstEmptyStartTimeFirst = cstEmptyStartTime;
                                }
                            } else {
                                if (CstEmptyCount == 0) {
                                    cstEmptyStartTimeFirst = cstEmptyStartTime;
                                }
                                if ((AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60) > downTimeTimeDiff) {
                                    cstEmptyStr += cstEmptyStartTimeFirst.substring(2, cstEmptyStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + cstEmptyEndTime.substring(2, cstEmptyEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totcstEmpty++;
                                    totcstEmptyTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60));
                                }

                                CstEmptyCount = 0;
                                cstEmptyStartTimeFirst = "N/A";
                            }
                        }

                        //cassette Fault
                        if (cstFault.equals("0 0 0 0")) {
                            if (cstFault.equals(NextCstFault)) {
                                CstFaultCount++;
                                if (CstFaultCount == 1) {
                                    cstFaultStartTimeFirst = cstFaultStartTime;
                                }
                            } else {
                                if (CstFaultCount == 0) {
                                    cstFaultStartTimeFirst = cstFaultStartTime;
                                }
                                if ((AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60) > downTimeTimeDiff) {
                                    cstFaultStr += cstFaultStartTimeFirst.substring(2, cstFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + cstFaultEndTime.substring(2, cstFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totcstFault++;
                                    totcstFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60));
                                }

                                CstFaultCount = 0;
                                cstFaultStartTimeFirst = "N/A";
                            }
                        }

                        //PinPad Fault
                        if (pinFault.equals("4")) {
                            if (pinFault.equals(NextPinFault)) {
                                pinFaultCount++;
                                if (pinFaultCount == 1) {
                                    pinFaultStartTimeFirst = pinFaultStartTime;
                                }
                            } else {
                                if (pinFaultCount == 0) {
                                    pinFaultStartTimeFirst = pinFaultStartTime;
                                }

                                if ((AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60) > downTimeTimeDiff) {
                                    pinFaultStr += pinFaultStartTimeFirst.substring(2, pinFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + pinFaultEndTime.substring(2, pinFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totpinFault++;
                                    totpinFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60));
                                }

                                pinFaultCount = 0;
                                pinFaultStartTimeFirst = "N/A";
                            }
                        }
//                        
                        //CardReader Fault
                        if (!cardFault.equals("0")) {
                            if (cardFault.equals(NextCardFault)) {
                                cardFaultCount++;
                                if (cardFaultCount == 1) {
                                    cardFaultStartTimeFirst = cardFaultStartTime;
                                }
                            } else {
                                if (cardFaultCount == 0) {
                                    cardFaultStartTimeFirst = cardFaultStartTime;
                                }
                                if ((AllDownTimeQuery.getHourDifference(cardFaultStartTimeFirst, cardFaultEndTime) * 60) > downTimeTimeDiff) {
                                    cardFaultStr += cardFaultStartTimeFirst.substring(2, cardFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + cardFaultEndTime.substring(2, cardFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(cardFaultStartTimeFirst, cardFaultEndTime) * 60) + "(min)~";
//                                    totErrors++;
                                    totcardFault++;
                                    totcardFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cardFaultStartTimeFirst, cardFaultEndTime) * 60));
                                }

                                cardFaultCount = 0;
                                cardFaultStartTimeFirst = "N/A";
                            }
                        }

                        //MoneyDrawer Fault
                        if ((moneyDrwFault.equals("140") || moneyDrwFault.equals("440"))) {
                            if (moneyDrwFault.equals(NextMoneyDrwFault)) {
                                moneyDrwFaultCount++;
                                if (moneyDrwFaultCount == 1) {
                                    moneyDrwFaultStartTimeFirst = moneyDrwFaultStartTime;
                                }
                            } else {
                                if (moneyDrwFaultCount == 0) {
                                    moneyDrwFaultStartTimeFirst = moneyDrwFaultStartTime;
                                }
                                if ((AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60) > downTimeTimeDiff) {
                                    moneyDrwFaultStr += moneyDrwFaultStartTimeFirst.substring(2, moneyDrwFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + moneyDrwFaultEndTime.substring(2, moneyDrwFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totmoneyDrwFault++;
                                    totmoneyDrwFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60));
                                }

                                moneyDrwFaultCount = 0;
                                moneyDrwFaultStartTimeFirst = "N/A";
                            }
                        }

                        //Down/Close Fault
                        if (downFault.equals("0") || closeFault.equals("0")) {
                            if (NextDownColseFault.equals("0 0") || NextDownColseFault.equals("0 1") || NextDownColseFault.equals("1 0")) {
                                downColseFaultCount++;
                                if (downColseFaultCount == 1) {
                                    downColseFaultStartTimeFirst = downColseFaultStartTime;
                                }
                            } else {
                                if (downColseFaultCount == 0) {
                                    downColseFaultStartTimeFirst = downColseFaultStartTime;
                                }

                                //Taking only morethan 20 min
                                if ((AllDownTimeQuery.getHourDifference(downColseFaultStartTimeFirst, downColseFaultEndTime) * 60) > downTimeTimeDiff) {
                                    downColseFaultStr += downColseFaultStartTimeFirst.substring(2, downColseFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + downColseFaultEndTime.substring(2, downColseFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(downColseFaultStartTimeFirst, downColseFaultEndTime) * 60) + "(min)~";
//                                    totErrors++;
                                    totdownColseFault++;
                                    totdownColseFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(downColseFaultStartTimeFirst, downColseFaultEndTime) * 60));

                                }
                                downColseFaultCount = 0;
                                downColseFaultStartTimeFirst = "N/A";

                            }
                        }

                        //Down Fault
                        if (downFault.equals("0")) {
                            if (NextDownFault.equals("0")) {
                                downFaultCount++;
                                if (downFaultCount == 1) {
                                    downFaultStartTimeFirst = downFaultStartTime;
                                }
                            } else {
                                if (downFaultCount == 0) {
                                    downFaultStartTimeFirst = downFaultStartTime;
                                }

                                //Taking only morethan 20 min
                                if ((AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60) > networkTimeDiff) {
                                    downFaultStr += downFaultStartTimeFirst.substring(2, downFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + downFaultEndTime.substring(2, downFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totdownFault++;
                                    totdownFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60));

                                }
                                downFaultCount = 0;
                                downFaultStartTimeFirst = "N/A";

                            }
                        }

                        //CardReader Fault + close
                        if (!cardFault.equals("0") && closeFault.equals("0")) {
                            if (cardFault.equals(NextCardFault) && closeFault.equals("0")) {
                                cardCloseFaultCount++;
                                if (cardCloseFaultCount == 1) {
                                    cardCloseFaultStartTimeFirst = cardCloseFaultStartTime;
                                }
                            } else {
                                if (cardCloseFaultCount == 0) {
                                    cardCloseFaultStartTimeFirst = cardCloseFaultStartTime;
                                }
                                if ((AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60) > downTimeTimeDiff) {
                                    cardCloseFaultStr += cardCloseFaultStartTimeFirst.substring(2, cardCloseFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + cardCloseFaultEndTime.substring(2, cardCloseFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totcardCloseFault++;
                                    totcardCloseFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60));
                                }

                                cardCloseFaultCount = 0;
                                cardCloseFaultStartTimeFirst = "N/A";

                            }
                        }

                    }

                    //********************************************************
                    // If atm change Reset all values and add to array else assign last value 
                    if (!atm.equals(atmNext)) {

                        if (totcstEmpty == 0) {
                            cstEmptyStr = "N/A";
                        }
                        if (totcstFault == 0) {
                            cstFaultStr = "N/A";
                        }
                        if (totpinFault == 0) {
                            pinFaultStr = "N/A";
                        }
                        if (totcardFault == 0) {
                            cardFaultStr = "N/A";
                        }
                        if (totmoneyDrwFault == 0) {
                            moneyDrwFaultStr = "N/A";
                        }
                        if (totdownColseFault == 0) {
                            downColseFaultStr = "N/A";
                        }
                        if (totdownFault == 0) {
                            downFaultStr = "N/A";
                        }
                        if (totcardCloseFault == 0) {
                            cardCloseFaultStr = "N/A";
                        }
                        if (totErrors > 0) {
                            atmDetailsEdit.add(atm + "#" + brcode + "#" + location + "#" + div + "#" + vender
                                    + "#" + cstEmptyStr + totcstEmptyTime
                                    + "#" + cstFaultStr + totcstFaultTime
                                    + "#" + pinFaultStr + totpinFaultTime
                                    + "#" + cardFaultStr + totcardFaultTime
                                    + "#" + moneyDrwFaultStr + totmoneyDrwFaultTime
                                    + "#" + downColseFaultStr + totdownColseFaultTime
                                    + "#" + downFaultStr + totdownFaultTime
                                    + "#" + cardCloseFaultStr + totcardCloseFaultTime
                                    + totErrors);
                        }

//                        atmDetailsEdit.add(atm + "#" + brcode + "#" + location
//                                + "#" + cstEmptyStr + totcstEmpty
//                                + "#" + cstFaultStr + totcstFault
//                                + "#" + pinFaultStr + totpinFault
//                                + "#" + cardFaultStr + totcardFault
//                                + "#" + moneyDrwFaultStr + totmoneyDrwFault
//                                + "#" + downColseFaultStr + totdownColseFault
//                                + totErrors);
                        startindex = 0;
                        totErrors = 0;

                        //cassette Empty
                        NextCstEmpty = "";
                        CstEmptyCount = 0;
                        cstEmptyStartTimeFirst = "N/A";
                        cstEmptyStr = "";
                        totcstEmpty = 0;
                        totcstEmptyTime = 0;

                        //cassette Fault
                        NextCstFault = "";
                        CstFaultCount = 0;
                        cstFaultStartTimeFirst = "N/A";
                        cstFaultStr = "";
                        totcstFault = 0;
                        totcstFaultTime = 0;
//                        
                        //PinPad Fault
                        NextPinFault = "";
                        pinFaultCount = 0;
                        pinFaultStartTimeFirst = "N/A";
                        pinFaultStr = "";
                        totpinFault = 0;
                        totpinFaultTime = 0;
//                        
                        //CardReader Fault
                        NextCardFault = "";
                        cardFaultCount = 0;
                        cardFaultStartTimeFirst = "N/A";
                        cardFaultStr = "";
                        totcardFault = 0;
                        totcardFaultTime = 0;

                        //MoneyDrawer Fault
                        NextMoneyDrwFault = "";
                        moneyDrwFaultCount = 0;
                        moneyDrwFaultStartTimeFirst = "N/A";
                        moneyDrwFaultStr = "";
                        totmoneyDrwFault = 0;
                        totmoneyDrwFaultTime = 0;

                        //DownClosed Fault
                        NextDownColseFault = "";
                        downColseFaultCount = 0;
                        downColseFaultStartTimeFirst = "N/A";
                        downColseFaultStr = "";
                        totdownColseFault = 0;
                        totdownColseFaultTime = 0;

                        //Down Fault
                        NextDownFault = "";
                        downFaultCount = 0;
                        downFaultStartTimeFirst = "N/A";
                        downFaultStr = "";
                        totdownFault = 0;
                        totdownFaultTime = 0;

                        //Card Fault + Close
                        NextCardCloseFault = "";
                        cardCloseFaultCount = 0;
                        cardCloseFaultStartTimeFirst = "N/A";
                        cardCloseFaultStr = "";
                        totcardCloseFault = 0;
                        totcardCloseFaultTime = 0;

                    } else if (i == atmDetails.size() - 1) {
                        if (totcstEmpty == 0) {
                            cstEmptyStr = "N/A";
                        }
                        if (totcstFault == 0) {
                            cstFaultStr = "N/A";
                        }
                        if (totpinFault == 0) {
                            pinFaultStr = "N/A";
                        }
                        if (totcardFault == 0) {
                            cardFaultStr = "N/A";
                        }
                        if (totmoneyDrwFault == 0) {
                            moneyDrwFaultStr = "N/A";
                        }
                        if (totdownColseFault == 0) {
                            downColseFaultStr = "N/A";
                        }
                        if (totdownFault == 0) {
                            downFaultStr = "N/A";
                        }

                        if (totcardCloseFault == 0) {
                            cardCloseFaultStr = "N/A";
                        }

                        if (totErrors > 0) {
                            atmDetailsEdit.add(atm + "#" + brcode + "#" + location + "#" + div + "#" + vender
                                    + "#" + cstEmptyStr + totcstEmptyTime
                                    + "#" + cstFaultStr + totcstFaultTime
                                    + "#" + pinFaultStr + totpinFaultTime
                                    + "#" + cardFaultStr + totcardFaultTime
                                    + "#" + moneyDrwFaultStr + totmoneyDrwFaultTime
                                    + "#" + downColseFaultStr + totdownColseFaultTime
                                    + "#" + downFaultStr + totdownFaultTime
                                    + "#" + cardCloseFaultStr + totcardCloseFaultTime
                                    + totErrors);
                        }
                        totErrors = 0;
                    } else {
                        startindex++;
                    }

                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
//            printAll(atmDetailsEdit);
            ResultJson = "[";
            for (int i = 0; i < atmDetailsEdit.size(); i++) {

                try {
                    String atm = atmDetailsEdit.get(i).split("#")[0].trim();
                    String brcode = atmDetailsEdit.get(i).split("#")[1].trim();
                    String location = atmDetailsEdit.get(i).split("#")[2].trim();
                    String div = atmDetailsEdit.get(i).split("#")[3].trim();
                    String vender = atmDetailsEdit.get(i).split("#")[4].trim();
                    String cstEmptyStrR = atmDetailsEdit.get(i).split("#")[5].trim();
                    String cstFaultStrR = atmDetailsEdit.get(i).split("#")[6].trim();
                    String pinFaultStrR = atmDetailsEdit.get(i).split("#")[7].trim();
                    String cardFaultStrR = atmDetailsEdit.get(i).split("#")[8].trim();
                    String moneyDrwFaultStrR = atmDetailsEdit.get(i).split("#")[9].trim();
                    String downColseFaultStrR = atmDetailsEdit.get(i).split("#")[10].trim();
                    String downFaultStrR = atmDetailsEdit.get(i).split("#")[11].trim();
                    String cardCloseFaultStrR = atmDetailsEdit.get(i).split("#")[12].trim();

                    ResultJson += "{\"ATM\":\"" + atm + "\",\"BRCODE\":" + brcode + ",\"LOCAION\":\"" + location
                            + "\",\"div\":\"" + div + "\",\"vender\":\"" + vender
                            + "\",\"cstEmpty\":\"" + cstEmptyStrR + "\",\"cstFault\":\"" + cstFaultStrR + "\",\"pinFault\":\"" + pinFaultStrR
                            + "\",\"cardFault\":\"" + cardFaultStrR + "\",\"moneyDrwFault\":\"" + moneyDrwFaultStrR + "\",\"downColseFault\":\"" + downColseFaultStrR
                            + "\",\"downFault\":\"" + downFaultStrR
                            + "\",\"cardCloseFault\":\"" + cardCloseFaultStrR
                            + "\"},";
//                            + "\"},";
//                    ResultJson += "{\"ATM\":\"" + atm + "\",\"BRCODE\":" + brcode + ",\"LOCAION\":\"" + location + "\",\"cstEmpty\":\"" + cstEmptyStrR
//                            + "\",\"cstFault\":\"" + cstFaultStrR + "\",\"pinFault\":\"" + pinFaultStrR + "\",\"cardFault\":\"" + cardFaultStrR
//                            + "\",\"moneyDrwFault\":\"" + moneyDrwFaultStrR + "\",\"downColseFault\":\"" + downColseFaultStrR + "\"},";
//                    System.out.println(atmDetailsEdit.get(i).trim());
                } catch (Exception e) {
                    System.out.println(e.toString());
                }

            }
            ResultJson += "]";
            ResultJson = ResultJson.replaceAll("},]", "}]");
//            System.out.println(ResultJson);

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return ResultJson;

    }

    //************************************
    //************************************
//    public String getAllDownTimeReport(String fromDate, String toDate, int userLvl, int uBr) {
//
//        String ResultJson = "\"\"";
//
//        try {
//            DBConnect obj = new DBConnect();
//            connection = obj.dbConnect_Local();
//
//            try {
//                cal.setTime(sdf.parse(toDate));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            cal.add(Calendar.DAY_OF_MONTH, 1);
//            String endDate = sdf.format(cal.getTime());
//
//            String sql = "";
//
//            if (userLvl != 3) {
//                sql = "select s.*,A.DIV_TYPE,V.VENDER_NAME,A.BR_CODE,A.LOCATION,A.PROTOCOL from ATM_STS_DESC_SUMMARY s,ATM_INFO a,VENDER_DETAILS v "
//                        + "where s.station = a.atm_name and a.vender = v.vender_id and "
//                        + "s.flt_date between TO_DATE( '" + fromDate + "', 'yyyy-mm-dd') and TO_DATE('" + endDate + "', 'yyyy-mm-dd') "
//                        + "order by s.station,s.flt_date";
//            } else {
//                sql = "select s.*,A.DIV_TYPE,V.VENDER_NAME,A.BR_CODE,A.LOCATION,A.PROTOCOL from ATM_STS_DESC_SUMMARY s,ATM_INFO a,VENDER_DETAILS v "
//                        + "where s.station = a.atm_name and a.vender = v.vender_id and "
//                        + "s.flt_date between TO_DATE( '" + fromDate + "', 'yyyy-mm-dd') and TO_DATE('" + endDate + "', 'yyyy-mm-dd') "
//                        + "and a.BR_CODE = " + uBr + " "
//                        + "order by s.station,s.flt_date";
//            }
//
//            stmnt = connection.createStatement();
//            rs = stmnt.executeQuery(sql);
//
//            ArrayList<String> atmDetails = new ArrayList<String>();
//
//            while (rs.next()) {
//                String atm = rs.getString(1).trim();
//                String err = rs.getString(2).trim();
//                String breakTime = rs.getString(3).trim();
//                String div = rs.getString(4).trim();
//                String vender = rs.getString(5).trim();
//                String brcode = rs.getString(6).trim();
//                String location = "";
//                if (rs.getString(7) != null) {
//                    location = rs.getString(7);
//                } else {
//                    location = "__________";
//                }
//                int protocol = Integer.parseInt(rs.getString(8).trim());
//
//                if (div.equals("CRM") && protocol == 2) {
//                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "NDCCRM" + "~" + brcode + "~" + location + "~" + breakTime + "~" + div + "~" + vender);
//
//                } else if ((div.equals("CRM")) && protocol == 1) {
//                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "DDCCRM" + "~" + brcode + "~" + location + "~" + breakTime + "~" + div + "~" + vender);
//
//                } else if ((div.equals("CDM")) && protocol == 1) {
//                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "DDCCDM" + "~" + brcode + "~" + location + "~" + breakTime + "~" + div + "~" + vender);
//
//                } else if ((div.equals("ATM")) && protocol == 1) {
//                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "DDCATM" + "~" + brcode + "~" + location + "~" + breakTime + "~" + div + "~" + vender);
//                }
//            }
//            rs.close();
//            stmnt.close();
//            obj.releaseCon(connection);
//
//            Date date = new Date();
//            ArrayList<String> atmDetailsEdit = new ArrayList<String>();
//            ReportsQuery AllDownTimeQuery = new ReportsQuery();
//
//            String atmNext = "";
//            int startindex = 0;
//            int totErrors = 0;
//
//            int totcstEmpty = 0;
//            int totcstFault = 0;
//            int totpinFault = 0;
//            int totcardFault = 0;
//            int totmoneyDrwFault = 0;
//            int totdownColseFault = 0;
//            int totdownFault = 0;
//            int totcardCloseFault = 0;
//
//            float totcstEmptyTime = 0;
//            float totcstFaultTime = 0;
//            float totpinFaultTime = 0;
//            float totcardFaultTime = 0;
//            float totmoneyDrwFaultTime = 0;
//            float totdownColseFaultTime = 0;
//            float totdownFaultTime = 0;
//            float totcardCloseFaultTime = 0;
//
//            String cstEmpty;
//            String cstEmptyStartTime = "";
//            String cstEmptyEndTime = "";
//            String cstEmptyStartTimeFirst = "";
//            String NextCstEmpty = "";
//            String cstEmptyStr = "";
//            int CstEmptyCount = 0;
//
//            String cstFault;
//            String cstFaultStartTime = "";
//            String cstFaultEndTime = "";
//            String cstFaultStartTimeFirst = "";
//            String NextCstFault = "";
//            String cstFaultStr = "";
//            int CstFaultCount = 0;
//
//            String pinFault;
//            String pinFaultStartTime = "";
//            String pinFaultEndTime = "";
//            String pinFaultStartTimeFirst = "";
//            String NextPinFault = "";
//            String pinFaultStr = "";
//            int pinFaultCount = 0;
//
//            String cardFault;
//            String cardFaultStartTime = "";
//            String cardFaultEndTime = "";
//            String cardFaultStartTimeFirst = "";
//            String NextCardFault = "";
//            String cardFaultStr = "";
//            int cardFaultCount = 0;
//
//            String moneyDrwFault;
//            String moneyDrwFaultStartTime = "";
//            String moneyDrwFaultEndTime = "";
//            String moneyDrwFaultStartTimeFirst = "";
//            String NextMoneyDrwFault = "";
//            String moneyDrwFaultStr = "";
//            int moneyDrwFaultCount = 0;
//
//            String downColseFault;
//            String downColseFaultStartTime = "";
//            String downColseFaultEndTime = "";
//            String downColseFaultStartTimeFirst = "";
//            String NextDownColseFault = "";
//            String downColseFaultStr = "";
//            int downColseFaultCount = 0;
//
//            String downFault;
//            String downFaultStartTime = "";
//            String downFaultEndTime = "";
//            String downFaultStartTimeFirst = "";
//            String NextDownFault = "";
//            String downFaultStr = "";
//            int downFaultCount = 0;
//
//            String cardCloseFault;
//            String cardCloseFaultStartTime = "";
//            String cardCloseFaultEndTime = "";
//            String cardCloseFaultStartTimeFirst = "";
//            String NextCardCloseFault = "";
//            String cardCloseFaultStr = "";
//            int cardCloseFaultCount = 0;
//
//            String closeFault;
//            String closeFaultStartTime = "";
//            String closeFaultEndTime = "";
//            String closeFaultStartTimeFirst = "";
//            String NextCloseFault = "";
//            String closeFaultStr = "";
//            int closeFaultCount = 0;
//
////            String stat;
////            String state;
////            int networkTimeDiff = 20;
////            int downTimeTimeDiff = 10;
//            int networkTimeDiff = 5;
//            int downTimeTimeDiff = 5;
//
//            for (int i = 0; i < atmDetails.size(); i++) {
//                try {
//                    String atm = atmDetails.get(i).split("~")[0];
//                    String errCode = atmDetails.get(i).split("~")[1];
//                    String brkTime = atmDetails.get(i).split("~")[2];
//                    String type = atmDetails.get(i).split("~")[3];
//                    Integer brcode = Integer.parseInt(atmDetails.get(i).split("~")[4]);
//                    String location = atmDetails.get(i).split("~")[5];
//                    String brkTimeNetwork = atmDetails.get(i).split("~")[6];
//                    String div = atmDetails.get(i).split("~")[7];
//                    String vender = atmDetails.get(i).split("~")[8];
//
//                    cstEmpty = errCode.substring(0, 7).trim();
//                    cstFault = errCode.substring(0, 7).trim();
//                    downFault = errCode.split(" ")[8];
//                    closeFault = errCode.split(" ")[9];
//                    pinFault = errCode.split(" ")[5];
//                    moneyDrwFault = errCode.split(" ")[6];
//                    cardFault = errCode.split(" ")[7];
//
//                    downColseFault = downFault + " " + closeFault;
//
//                    if (i != atmDetails.size() - 1) {
//                        atmNext = atmDetails.get(i + 1).split("~")[0];
//                        NextCstEmpty = atmDetails.get(i + 1).split("~")[1].substring(0, 7).trim();
//                        NextCstFault = atmDetails.get(i + 1).split("~")[1].substring(0, 7).trim();
//                        NextPinFault = atmDetails.get(i + 1).split("~")[1].split(" ")[5];
//                        NextCardFault = atmDetails.get(i + 1).split("~")[1].split(" ")[7];
//                        NextMoneyDrwFault = atmDetails.get(i + 1).split("~")[1].split(" ")[6];
//                        NextDownColseFault = atmDetails.get(i + 1).split("~")[1].split(" ")[8] + " " + atmDetails.get(i + 1).split("~")[1].split(" ")[9]; // down or closed
//                        NextDownFault = atmDetails.get(i + 1).split("~")[1].split(" ")[8]; // down
//                        NextCardCloseFault = atmDetails.get(i + 1).split("~")[1].split(" ")[7] + " " + atmDetails.get(i + 1).split("~")[1].split(" ")[9]; // card fault and closed
//                        NextCloseFault = atmDetails.get(i + 1).split("~")[1].split(" ")[9]; // down
//                    }
//
//                    //Start Time
//                    if (startindex == 0) {
//                        //All Cassette Empty
//                        if ((cstEmpty.equals("2 2 2 2") && !type.equals("NDCCRM")) || (cstEmpty.equals("3 3 3 3") && type.equals("NDCCRM"))) {
//                            cstEmptyStartTime = fromDate + " 00:00:00";
//                        } else {
//                            cstEmptyStartTime = brkTime;
//                        }
//
//                        //All Cassette Fault
//                        if ((cstFault.equals("9 9 9 9") && !type.equals("NDCCRM")) || (cstFault.equals("0 0 0 0") && type.equals("NDCCRM"))) {
//                            cstFaultStartTime = fromDate + " 00:00:00";
//                        } else {
//                            cstFaultStartTime = brkTime;
//                        }
//
//                        //PinPad Fault
//                        if ((pinFault.equals("1") && !type.equals("NDCCRM")) || (pinFault.equals("4") && type.equals("NDCCRM"))) {
//                            pinFaultStartTime = fromDate + " 00:00:00";
//                        } else {
//                            pinFaultStartTime = brkTime;
//                        }
//
//                        //CardReader Fault
//                        if ((!cardFault.equals("0") && !type.equals("NDCCRM")) || (!cardFault.equals("0") && type.equals("NDCCRM"))) {
//                            cardFaultStartTime = fromDate + " 00:00:00";
//                        } else {
//                            cardFaultStartTime = brkTime;
//                        }
//
//                        //ModeyDrawer Fault
//                        if (((moneyDrwFault.equals("1") || moneyDrwFault.equals("2")) && type.equals("DDCATM"))
//                                || (!(moneyDrwFault.equals("0") || moneyDrwFault.equals("1")) && (type.equals("DDCCDM") || type.equals("DDCCRM")))
//                                || ((moneyDrwFault.equals("140") || moneyDrwFault.equals("440")) && type.equals("NDCCRM"))) {
//                            moneyDrwFaultStartTime = fromDate + " 00:00:00";
//                        } else {
//                            moneyDrwFaultStartTime = brkTime;
//                        }
//
//                        //Down/Close Fault
//                        if (downColseFault.equals("0 0") || downColseFault.equals("0 1") || downColseFault.equals("1 0")) {
//                            downColseFaultStartTime = fromDate + " 00:00:00";
//                        } else {
//                            downColseFaultStartTime = brkTimeNetwork;
//                        }
//
//                        //Down Fault
//                        if (downFault.equals("0")) {
//                            downFaultStartTime = fromDate + " 00:00:00";
//                        } else {
//                            downFaultStartTime = brkTimeNetwork;
//                        }
//
//                        //CardReader Fault + Closed
//                        if (((!cardFault.equals("0") && !type.equals("NDCCRM")) || (!cardFault.equals("0") && type.equals("NDCCRM"))) && closeFault.equals("0")) {
//                            cardCloseFaultStartTime = fromDate + " 00:00:00";
//                        } else {
//                            cardCloseFaultStartTime = brkTimeNetwork; //use the networktime because brktime has the card reader fault time
//                        }
//
//                    } else {
//                        cstEmptyStartTime = brkTime;
//                        cstFaultStartTime = brkTime;
//                        pinFaultStartTime = brkTime;
//                        cardFaultStartTime = brkTime;
//                        moneyDrwFaultStartTime = brkTime;
//                        downColseFaultStartTime = brkTimeNetwork;
//                        downFaultStartTime = brkTimeNetwork;
//                        cardCloseFaultStartTime = brkTimeNetwork; //use the networktime because brktime has the card reader fault time
//                    }
//
//                    //End Time
//                    if (!atm.equals(atmNext)) {
//                        //cassette empty
//                        if ((cstEmpty.equals("2 2 2 2") && !type.equals("NDCCRM")) || (cstEmpty.equals("3 3 3 3") && type.equals("NDCCRM"))) {
//                            if (sdf.format(date).equals(toDate)) {
//                                cstEmptyEndTime = toDate + " " + sdf2.format(date);
//                            } else {
//                                cstEmptyEndTime = toDate + " 24:00:00";
//                            }
//
//                        } else {
//                            cstEmptyEndTime = brkTime;
//                        }
//
//                        //cassete fault
//                        if ((cstFault.equals("9 9 9 9") && !type.equals("NDCCRM")) || (cstFault.equals("0 0 0 0") && type.equals("NDCCRM"))) {
//                            if (sdf.format(date).equals(toDate)) {
//                                cstFaultEndTime = toDate + " " + sdf2.format(date);
//                            } else {
//                                cstFaultEndTime = toDate + " 24:00:00";
//                            }
//                        } else {
//                            cstFaultEndTime = brkTime;
//                        }
////                        
//                        //PinPad fault
//                        if ((pinFault.equals("1") && !type.equals("NDCCRM")) || (pinFault.equals("4") && type.equals("NDCCRM"))) {
//                            if (sdf.format(date).equals(toDate)) {
//                                pinFaultEndTime = toDate + " " + sdf2.format(date);
//                            } else {
//                                pinFaultEndTime = toDate + " 24:00:00";
//                            }
//                        } else {
//                            pinFaultEndTime = brkTime;
//                        }
////                        
//                        //CardReader fault
//                        if ((!cardFault.equals("0") && !type.equals("NDCCRM")) || (!cardFault.equals("0") && type.equals("NDCCRM"))) {
//                            if (sdf.format(date).equals(toDate)) {
//                                cardFaultEndTime = toDate + " " + sdf2.format(date);
//                            } else {
//                                cardFaultEndTime = toDate + " 24:00:00";
//                            }
//                        } else {
//                            cardFaultEndTime = brkTime;
//                        }
//
//                        //MoneyDrawer fault
//                        if (((moneyDrwFault.equals("1") || moneyDrwFault.equals("2")) && type.equals("DDCATM"))
//                                || (!(moneyDrwFault.equals("0") || moneyDrwFault.equals("1")) && (type.equals("DDCCDM") || type.equals("DDCCRM")))
//                                || ((moneyDrwFault.equals("140") || moneyDrwFault.equals("440")) && type.equals("NDCCRM"))) {
//                            if (sdf.format(date).equals(toDate)) {
//                                moneyDrwFaultEndTime = toDate + " " + sdf2.format(date);
//                            } else {
//                                moneyDrwFaultEndTime = toDate + " 24:00:00";
//                            }
//                        } else {
//                            moneyDrwFaultEndTime = brkTime;
//                        }
//
//                        //Down/Closed fault
//                        if (downFault.equals("0") || closeFault.equals("0")) {
//                            if (sdf.format(date).equals(toDate)) {
//                                downColseFaultEndTime = toDate + " " + sdf2.format(date);
//                            } else {
//                                downColseFaultEndTime = toDate + " 24:00:00";
//                            }
//                        } else {
//                            downColseFaultEndTime = brkTimeNetwork;
//                        }
//
//                        //Down fault
//                        if (downFault.equals("0")) {
//                            if (sdf.format(date).equals(toDate)) {
//                                downFaultEndTime = toDate + " " + sdf2.format(date);
//                            } else {
//                                downFaultEndTime = toDate + " 24:00:00";
//                            }
//                        } else {
//                            downFaultEndTime = brkTimeNetwork;
//                        }
//
//                        //CardReader fault + closed
//                        if (((!cardFault.equals("0") && !type.equals("NDCCRM")) || (!cardFault.equals("0") && type.equals("NDCCRM"))) && closeFault.equals("0")) {
//                            if (sdf.format(date).equals(toDate)) {
//                                cardCloseFaultEndTime = toDate + " " + sdf2.format(date);
//                            } else {
//                                cardCloseFaultEndTime = toDate + " 24:00:00";
//                            }
//                        } else {
//                            cardCloseFaultEndTime = brkTimeNetwork; //use the networktime because brktime has the card reader fault time
//                        }
//
//                    } else {
//                        if (i != atmDetails.size() - 1) {
//                            cstEmptyEndTime = atmDetails.get(i + 1).split("~")[2];
//                            cstFaultEndTime = atmDetails.get(i + 1).split("~")[2];
//                            pinFaultEndTime = atmDetails.get(i + 1).split("~")[2];
//                            cardFaultEndTime = atmDetails.get(i + 1).split("~")[2];
//                            moneyDrwFaultEndTime = atmDetails.get(i + 1).split("~")[2];
//                            downColseFaultEndTime = atmDetails.get(i + 1).split("~")[6];
//                            downFaultEndTime = atmDetails.get(i + 1).split("~")[6];
//                            cardCloseFaultEndTime = atmDetails.get(i + 1).split("~")[6];//use the networktime because brktime has the card reader fault time
//                        } else {
//                            cstEmptyEndTime = brkTime;
//                            cstFaultEndTime = brkTime;
//                            pinFaultEndTime = brkTime;
//                            cardFaultEndTime = brkTime;
//                            moneyDrwFaultEndTime = brkTime;
//                            downColseFaultEndTime = brkTimeNetwork;
//                            downFaultEndTime = brkTimeNetwork;
//                            cardCloseFaultEndTime = brkTimeNetwork;//use the networktime because brktime has the card reader fault time
//                        }
//                    }
//
//                    //Calculate
//                    //Not HITACHI CRM
//                    if (!type.equals("NDCCRM")) {
//                        //cassette Empty
//                        if (cstEmpty.equals("2 2 2 2") || cstEmpty.equals("3 3 3 3")) {
//                            if (cstEmpty.equals(NextCstEmpty)) {
//                                CstEmptyCount++;
//                                if (CstEmptyCount == 1) {
//                                    cstEmptyStartTimeFirst = cstEmptyStartTime;
//                                }
//                            } else {
//                                if (CstEmptyCount == 0) {
//                                    cstEmptyStartTimeFirst = cstEmptyStartTime;
//                                }
//
//                                if ((AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60) > downTimeTimeDiff) {
//                                    cstEmptyStr += cstEmptyStartTimeFirst.substring(2, cstEmptyStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
//                                            + cstEmptyEndTime.substring(2, cstEmptyEndTime.length() - 2).replaceAll("-", "/") + "="
//                                            + df.format(AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60) + "(min)~";
//                                    totErrors++;
//                                    totcstEmpty++;
//                                    totcstEmptyTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60));
//                                }
//
//                                CstEmptyCount = 0;
//                                cstEmptyStartTimeFirst = "N/A";
//
//                            }
//                        }
//
//                        //cassette Fault
//                        if (cstFault.equals("9 9 9 9")) {
//                            if (cstFault.equals(NextCstFault)) {
//                                CstFaultCount++;
//                                if (CstFaultCount == 1) {
//                                    cstFaultStartTimeFirst = cstFaultStartTime;
//                                }
//                            } else {
//                                if (CstFaultCount == 0) {
//                                    cstFaultStartTimeFirst = cstFaultStartTime;
//                                }
//
//                                if ((AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60) > downTimeTimeDiff) {
//                                    cstFaultStr += cstFaultStartTimeFirst.substring(2, cstFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
//                                            + cstFaultEndTime.substring(2, cstFaultEndTime.length() - 2).replaceAll("-", "/") + "="
//                                            + df.format(AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60) + "(min)~";
//                                    totErrors++;
//                                    totcstFault++;
//                                    totcstFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60));
//                                }
//
//                                CstFaultCount = 0;
//                                cstFaultStartTimeFirst = "N/A";
//                            }
//                        }
//
//                        //PinPad Fault
//                        if (pinFault.equals("1")) {
//                            if (pinFault.equals(NextPinFault)) {
//                                pinFaultCount++;
//                                if (pinFaultCount == 1) {
//                                    pinFaultStartTimeFirst = pinFaultStartTime;
//                                }
//                            } else {
//                                if (pinFaultCount == 0) {
//                                    pinFaultStartTimeFirst = pinFaultStartTime;
//                                }
//                                if ((AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60) > downTimeTimeDiff) {
//                                    pinFaultStr += pinFaultStartTimeFirst.substring(2, pinFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
//                                            + pinFaultEndTime.substring(2, pinFaultEndTime.length() - 2).replaceAll("-", "/") + "="
//                                            + df.format(AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60) + "(min)~";
//                                    totErrors++;
//                                    totpinFault++;
//                                    totpinFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60));
//                                }
//
//                                pinFaultCount = 0;
//                                pinFaultStartTimeFirst = "N/A";
//                            }
//                        }
////                        
//                        //CardReader Fault
//                        if (!cardFault.equals("0")) {
//                            if (cardFault.equals(NextCardFault)) {
//                                cardFaultCount++;
//                                if (cardFaultCount == 1) {
//                                    cardFaultStartTimeFirst = cardFaultStartTime;
//                                }
//                            } else {
//                                if (cardFaultCount == 0) {
//                                    cardFaultStartTimeFirst = cardFaultStartTime;
//                                }
//                                if ((AllDownTimeQuery.getHourDifference(cardFaultStartTimeFirst, cardFaultEndTime) * 60) > downTimeTimeDiff) {
//                                    cardFaultStr += cardFaultStartTimeFirst.substring(2, cardFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
//                                            + cardFaultEndTime.substring(2, cardFaultEndTime.length() - 2).replaceAll("-", "/") + "="
//                                            + df.format(AllDownTimeQuery.getHourDifference(cardFaultStartTimeFirst, cardFaultEndTime) * 60) + "(min)~";
//                                    totErrors++;
//                                    totcardFault++;
//                                    totcardFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cardFaultStartTimeFirst, cardFaultEndTime) * 60));
//                                }
//
//                                cardFaultCount = 0;
//                                cardFaultStartTimeFirst = "N/A";
//                            }
//                        }
//
//                        //MoneyDrawer Fault
//                        if (((moneyDrwFault.equals("1") || moneyDrwFault.equals("2")) && type.equals("DDCATM"))
//                                || (!(moneyDrwFault.equals("0") || moneyDrwFault.equals("1")) && (type.equals("DDCCDM") || type.equals("DDCCRM")))) {
//                            if (moneyDrwFault.equals(NextMoneyDrwFault)) {
//                                moneyDrwFaultCount++;
//                                if (moneyDrwFaultCount == 1) {
//                                    moneyDrwFaultStartTimeFirst = moneyDrwFaultStartTime;
//                                }
//                            } else {
//                                if (moneyDrwFaultCount == 0) {
//                                    moneyDrwFaultStartTimeFirst = moneyDrwFaultStartTime;
//                                }
//
//                                if ((AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60) > downTimeTimeDiff) {
//                                    moneyDrwFaultStr += moneyDrwFaultStartTimeFirst.substring(2, moneyDrwFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
//                                            + moneyDrwFaultEndTime.substring(2, moneyDrwFaultEndTime.length() - 2).replaceAll("-", "/") + "="
//                                            + df.format(AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60) + "(min)~";
//                                    totErrors++;
//                                    totmoneyDrwFault++;
//                                    totmoneyDrwFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60));
//                                }
//
//                                moneyDrwFaultCount = 0;
//                                moneyDrwFaultStartTimeFirst = "N/A";
//                            }
//                        }
//
//                        //Down/Close Fault
//                        if (downFault.equals("0") || closeFault.equals("0")) {
//                            if (NextDownColseFault.equals("0 0") || NextDownColseFault.equals("0 1") || NextDownColseFault.equals("1 0")) {
//                                downColseFaultCount++;
//                                if (downColseFaultCount == 1) {
//                                    downColseFaultStartTimeFirst = downColseFaultStartTime;
//                                }
//                            } else {
//                                if (downColseFaultCount == 0) {
//                                    downColseFaultStartTimeFirst = downColseFaultStartTime;
//                                }
//
//                                //Taking only morethan 20 min
//                                if ((AllDownTimeQuery.getHourDifference(downColseFaultStartTimeFirst, downColseFaultEndTime) * 60) > downTimeTimeDiff) {
//                                    downColseFaultStr += downColseFaultStartTimeFirst.substring(2, downColseFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
//                                            + downColseFaultEndTime.substring(2, downColseFaultEndTime.length() - 2).replaceAll("-", "/") + "="
//                                            + df.format(AllDownTimeQuery.getHourDifference(downColseFaultStartTimeFirst, downColseFaultEndTime) * 60) + "(min)~";
//                                    totErrors++;
//                                    totdownColseFault++;
//                                    totdownColseFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(downColseFaultStartTimeFirst, downColseFaultEndTime) * 60));
//
//                                }
//                                downColseFaultCount = 0;
//                                downColseFaultStartTimeFirst = "N/A";
//
//                            }
//                        }
//
//                        //Down Fault
//                        if (downFault.equals("0")) {
//                            if (downFault.equals(NextDownFault)) {
//                                downFaultCount++;
//                                if (downFaultCount == 1) {
//                                    downFaultStartTimeFirst = downFaultStartTime;
//                                }
//                            } else {
//                                if (downFaultCount == 0) {
//                                    downFaultStartTimeFirst = downFaultStartTime;
//                                }
//
//                                //Taking only morethan 20 min
//                                if ((AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60) > downTimeTimeDiff) {
//                                    downFaultStr += downFaultStartTimeFirst.substring(2, downFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
//                                            + downFaultEndTime.substring(2, downFaultEndTime.length() - 2).replaceAll("-", "/") + "="
//                                            + df.format(AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60) + "(min)~";
//                                    totErrors++;
//                                    totdownFault++;
//                                    totdownFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60));
//
//                                }
//                                downFaultCount = 0;
//                                downFaultStartTimeFirst = "N/A";
//
//                            }
//                        }
//
//                        //CardReader Fault + close
//                        if (!cardFault.equals("0") && closeFault.equals("0")) {
//                            if (cardFault.equals(NextCardFault) && closeFault.equals(NextCloseFault)) {
//                                cardCloseFaultCount++;
//                                if (cardCloseFaultCount == 1) {
//                                    cardCloseFaultStartTimeFirst = cardCloseFaultStartTime;
//                                }
//                            } else {
//                                if (cardCloseFaultCount == 0) {
//                                    cardCloseFaultStartTimeFirst = cardCloseFaultStartTime;
//                                }
//                                if ((AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60) > downTimeTimeDiff) {
//                                    cardCloseFaultStr += cardCloseFaultStartTimeFirst.substring(2, cardCloseFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
//                                            + cardCloseFaultEndTime.substring(2, cardCloseFaultEndTime.length() - 2).replaceAll("-", "/") + "="
//                                            + df.format(AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60) + "(min)~";
//                                    totErrors++;
//                                    totcardCloseFault++;
//                                    totcardCloseFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60));
//                                }
//
//                                cardCloseFaultCount = 0;
//                                cardCloseFaultStartTimeFirst = "N/A";
//                            }
//                        }
//
//                    } //HITACHI CRM
//                    else if (type.equals("NDCCRM")) {
//                        //cassette Empty
//                        if (cstEmpty.equals("3 3 3 3")) {
//                            if (cstEmpty.equals(NextCstEmpty)) {
//                                CstEmptyCount++;
//                                if (CstEmptyCount == 1) {
//                                    cstEmptyStartTimeFirst = cstEmptyStartTime;
//                                }
//                            } else {
//                                if (CstEmptyCount == 0) {
//                                    cstEmptyStartTimeFirst = cstEmptyStartTime;
//                                }
//                                if ((AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60) > downTimeTimeDiff) {
//                                    cstEmptyStr += cstEmptyStartTimeFirst.substring(2, cstEmptyStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
//                                            + cstEmptyEndTime.substring(2, cstEmptyEndTime.length() - 2).replaceAll("-", "/") + "="
//                                            + df.format(AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60) + "(min)~";
//                                    totErrors++;
//                                    totcstEmpty++;
//                                    totcstEmptyTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60));
//                                }
//
//                                CstEmptyCount = 0;
//                                cstEmptyStartTimeFirst = "N/A";
//                            }
//                        }
//
//                        //cassette Fault
//                        if (cstFault.equals("0 0 0 0")) {
//                            if (cstFault.equals(NextCstFault)) {
//                                CstFaultCount++;
//                                if (CstFaultCount == 1) {
//                                    cstFaultStartTimeFirst = cstFaultStartTime;
//                                }
//                            } else {
//                                if (CstFaultCount == 0) {
//                                    cstFaultStartTimeFirst = cstFaultStartTime;
//                                }
//                                if ((AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60) > downTimeTimeDiff) {
//                                    cstFaultStr += cstFaultStartTimeFirst.substring(2, cstFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
//                                            + cstFaultEndTime.substring(2, cstFaultEndTime.length() - 2).replaceAll("-", "/") + "="
//                                            + df.format(AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60) + "(min)~";
//                                    totErrors++;
//                                    totcstFault++;
//                                    totcstFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60));
//                                }
//
//                                CstFaultCount = 0;
//                                cstFaultStartTimeFirst = "N/A";
//                            }
//                        }
//
//                        //PinPad Fault
//                        if (pinFault.equals("4")) {
//                            if (pinFault.equals(NextPinFault)) {
//                                pinFaultCount++;
//                                if (pinFaultCount == 1) {
//                                    pinFaultStartTimeFirst = pinFaultStartTime;
//                                }
//                            } else {
//                                if (pinFaultCount == 0) {
//                                    pinFaultStartTimeFirst = pinFaultStartTime;
//                                }
//
//                                if ((AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60) > downTimeTimeDiff) {
//                                    pinFaultStr += pinFaultStartTimeFirst.substring(2, pinFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
//                                            + pinFaultEndTime.substring(2, pinFaultEndTime.length() - 2).replaceAll("-", "/") + "="
//                                            + df.format(AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60) + "(min)~";
//                                    totErrors++;
//                                    totpinFault++;
//                                    totpinFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60));
//                                }
//
//                                pinFaultCount = 0;
//                                pinFaultStartTimeFirst = "N/A";
//                            }
//                        }
////                        
//                        //CardReader Fault
//                        if (!cardFault.equals("0")) {
//                            if (cardFault.equals(NextCardFault)) {
//                                cardFaultCount++;
//                                if (cardFaultCount == 1) {
//                                    cardFaultStartTimeFirst = cardFaultStartTime;
//                                }
//                            } else {
//                                if (cardFaultCount == 0) {
//                                    cardFaultStartTimeFirst = cardFaultStartTime;
//                                }
//                                if ((AllDownTimeQuery.getHourDifference(cardFaultStartTimeFirst, cardFaultEndTime) * 60) > downTimeTimeDiff) {
//                                    cardFaultStr += cardFaultStartTimeFirst.substring(2, cardFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
//                                            + cardFaultEndTime.substring(2, cardFaultEndTime.length() - 2).replaceAll("-", "/") + "="
//                                            + df.format(AllDownTimeQuery.getHourDifference(cardFaultStartTimeFirst, cardFaultEndTime) * 60) + "(min)~";
//                                    totErrors++;
//                                    totcardFault++;
//                                    totcardFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cardFaultStartTimeFirst, cardFaultEndTime) * 60));
//                                }
//
//                                cardFaultCount = 0;
//                                cardFaultStartTimeFirst = "N/A";
//                            }
//                        }
//
//                        //MoneyDrawer Fault
//                        if ((moneyDrwFault.equals("140") || moneyDrwFault.equals("440"))) {
//                            if (moneyDrwFault.equals(NextMoneyDrwFault)) {
//                                moneyDrwFaultCount++;
//                                if (moneyDrwFaultCount == 1) {
//                                    moneyDrwFaultStartTimeFirst = moneyDrwFaultStartTime;
//                                }
//                            } else {
//                                if (moneyDrwFaultCount == 0) {
//                                    moneyDrwFaultStartTimeFirst = moneyDrwFaultStartTime;
//                                }
//                                if ((AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60) > downTimeTimeDiff) {
//                                    moneyDrwFaultStr += moneyDrwFaultStartTimeFirst.substring(2, moneyDrwFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
//                                            + moneyDrwFaultEndTime.substring(2, moneyDrwFaultEndTime.length() - 2).replaceAll("-", "/") + "="
//                                            + df.format(AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60) + "(min)~";
//                                    totErrors++;
//                                    totmoneyDrwFault++;
//                                    totmoneyDrwFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60));
//                                }
//
//                                moneyDrwFaultCount = 0;
//                                moneyDrwFaultStartTimeFirst = "N/A";
//                            }
//                        }
//
//                        //Down/Close Fault
//                        if (downFault.equals("0") || closeFault.equals("0")) {
//                            if (NextDownColseFault.equals("0 0") || NextDownColseFault.equals("0 1") || NextDownColseFault.equals("1 0")) {
//                                downColseFaultCount++;
//                                if (downColseFaultCount == 1) {
//                                    downColseFaultStartTimeFirst = downColseFaultStartTime;
//                                }
//                            } else {
//                                if (downColseFaultCount == 0) {
//                                    downColseFaultStartTimeFirst = downColseFaultStartTime;
//                                }
//
//                                //Taking only morethan 20 min
//                                if ((AllDownTimeQuery.getHourDifference(downColseFaultStartTimeFirst, downColseFaultEndTime) * 60) > downTimeTimeDiff) {
//                                    downColseFaultStr += downColseFaultStartTimeFirst.substring(2, downColseFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
//                                            + downColseFaultEndTime.substring(2, downColseFaultEndTime.length() - 2).replaceAll("-", "/") + "="
//                                            + df.format(AllDownTimeQuery.getHourDifference(downColseFaultStartTimeFirst, downColseFaultEndTime) * 60) + "(min)~";
//                                    totErrors++;
//                                    totdownColseFault++;
//                                    totdownColseFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(downColseFaultStartTimeFirst, downColseFaultEndTime) * 60));
//
//                                }
//                                downColseFaultCount = 0;
//                                downColseFaultStartTimeFirst = "N/A";
//
//                            }
//                        }
//
//                        //Down Fault
//                        if (downFault.equals("0")) {
//                            if (NextDownFault.equals("0")) {
//                                downFaultCount++;
//                                if (downFaultCount == 1) {
//                                    downFaultStartTimeFirst = downFaultStartTime;
//                                }
//                            } else {
//                                if (downFaultCount == 0) {
//                                    downFaultStartTimeFirst = downFaultStartTime;
//                                }
//
//                                //Taking only morethan 20 min
//                                if ((AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60) > networkTimeDiff) {
//                                    downFaultStr += downFaultStartTimeFirst.substring(2, downFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
//                                            + downFaultEndTime.substring(2, downFaultEndTime.length() - 2).replaceAll("-", "/") + "="
//                                            + df.format(AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60) + "(min)~";
//                                    totErrors++;
//                                    totdownFault++;
//                                    totdownFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60));
//
//                                }
//                                downFaultCount = 0;
//                                downFaultStartTimeFirst = "N/A";
//
//                            }
//                        }
//
//                        //CardReader Fault + close
//                        if (!cardFault.equals("0") && closeFault.equals("0")) {
//                            if (cardFault.equals(NextCardFault) && closeFault.equals("0")) {
//                                cardCloseFaultCount++;
//                                if (cardCloseFaultCount == 1) {
//                                    cardCloseFaultStartTimeFirst = cardCloseFaultStartTime;
//                                }
//                            } else {
//                                if (cardCloseFaultCount == 0) {
//                                    cardCloseFaultStartTimeFirst = cardCloseFaultStartTime;
//                                }
//                                if ((AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60) > downTimeTimeDiff) {
//                                    cardCloseFaultStr += cardCloseFaultStartTimeFirst.substring(2, cardCloseFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
//                                            + cardCloseFaultEndTime.substring(2, cardCloseFaultEndTime.length() - 2).replaceAll("-", "/") + "="
//                                            + df.format(AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60) + "(min)~";
//                                    totErrors++;
//                                    totcardCloseFault++;
//                                    totcardCloseFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60));
//                                }
//
//                                cardCloseFaultCount = 0;
//                                cardCloseFaultStartTimeFirst = "N/A";
//
//                            }
//                        }
//
//                    }
//
//                    //********************************************************
//                    // If atm change Reset all values and add to array else assign last value 
//                    if (!atm.equals(atmNext)) {
//
//                        if (totcstEmpty == 0) {
//                            cstEmptyStr = "N/A";
//                        }
//                        if (totcstFault == 0) {
//                            cstFaultStr = "N/A";
//                        }
//                        if (totpinFault == 0) {
//                            pinFaultStr = "N/A";
//                        }
//                        if (totcardFault == 0) {
//                            cardFaultStr = "N/A";
//                        }
//                        if (totmoneyDrwFault == 0) {
//                            moneyDrwFaultStr = "N/A";
//                        }
//                        if (totdownColseFault == 0) {
//                            downColseFaultStr = "N/A";
//                        }
//                        if (totdownFault == 0) {
//                            downFaultStr = "N/A";
//                        }
//                        if (totcardCloseFault == 0) {
//                            cardCloseFaultStr = "N/A";
//                        }
//
//                        atmDetailsEdit.add(atm + "#" + brcode + "#" + location + "#" + div + "#" + vender
//                                + "#" + cstEmptyStr + totcstEmptyTime
//                                + "#" + cstFaultStr + totcstFaultTime
//                                + "#" + pinFaultStr + totpinFaultTime
//                                + "#" + cardFaultStr + totcardFaultTime
//                                + "#" + moneyDrwFaultStr + totmoneyDrwFaultTime
//                                + "#" + downColseFaultStr + totdownColseFaultTime
//                                + "#" + downFaultStr + totdownFaultTime
//                                + "#" + cardCloseFaultStr + totcardCloseFaultTime
//                                + totErrors);
//
////                        atmDetailsEdit.add(atm + "#" + brcode + "#" + location
////                                + "#" + cstEmptyStr + totcstEmpty
////                                + "#" + cstFaultStr + totcstFault
////                                + "#" + pinFaultStr + totpinFault
////                                + "#" + cardFaultStr + totcardFault
////                                + "#" + moneyDrwFaultStr + totmoneyDrwFault
////                                + "#" + downColseFaultStr + totdownColseFault
////                                + totErrors);
//                        startindex = 0;
//                        totErrors = 0;
//
//                        //cassette Empty
//                        NextCstEmpty = "";
//                        CstEmptyCount = 0;
//                        cstEmptyStartTimeFirst = "N/A";
//                        cstEmptyStr = "";
//                        totcstEmpty = 0;
//                        totcstEmptyTime = 0;
//
//                        //cassette Fault
//                        NextCstFault = "";
//                        CstFaultCount = 0;
//                        cstFaultStartTimeFirst = "N/A";
//                        cstFaultStr = "";
//                        totcstFault = 0;
//                        totcstFaultTime = 0;
////                        
//                        //PinPad Fault
//                        NextPinFault = "";
//                        pinFaultCount = 0;
//                        pinFaultStartTimeFirst = "N/A";
//                        pinFaultStr = "";
//                        totpinFault = 0;
//                        totpinFaultTime = 0;
////                        
//                        //CardReader Fault
//                        NextCardFault = "";
//                        cardFaultCount = 0;
//                        cardFaultStartTimeFirst = "N/A";
//                        cardFaultStr = "";
//                        totcardFault = 0;
//                        totcardFaultTime = 0;
//
//                        //MoneyDrawer Fault
//                        NextMoneyDrwFault = "";
//                        moneyDrwFaultCount = 0;
//                        moneyDrwFaultStartTimeFirst = "N/A";
//                        moneyDrwFaultStr = "";
//                        totmoneyDrwFault = 0;
//                        totmoneyDrwFaultTime = 0;
//
//                        //DownClosed Fault
//                        NextDownColseFault = "";
//                        downColseFaultCount = 0;
//                        downColseFaultStartTimeFirst = "N/A";
//                        downColseFaultStr = "";
//                        totdownColseFault = 0;
//                        totdownColseFaultTime = 0;
//
//                        //Down Fault
//                        NextDownFault = "";
//                        downFaultCount = 0;
//                        downFaultStartTimeFirst = "N/A";
//                        downFaultStr = "";
//                        totdownFault = 0;
//                        totdownFaultTime = 0;
//
//                        //Card Fault + Close
//                        NextCardCloseFault = "";
//                        cardCloseFaultCount = 0;
//                        cardCloseFaultStartTimeFirst = "N/A";
//                        cardCloseFaultStr = "";
//                        totcardCloseFault = 0;
//                        totcardCloseFaultTime = 0;
//
//                    } else if (i == atmDetails.size() - 1) {
//                        if (totcstEmpty == 0) {
//                            cstEmptyStr = "N/A";
//                        }
//                        if (totcstFault == 0) {
//                            cstFaultStr = "N/A";
//                        }
//                        if (totpinFault == 0) {
//                            pinFaultStr = "N/A";
//                        }
//                        if (totcardFault == 0) {
//                            cardFaultStr = "N/A";
//                        }
//                        if (totmoneyDrwFault == 0) {
//                            moneyDrwFaultStr = "N/A";
//                        }
//                        if (totdownColseFault == 0) {
//                            downColseFaultStr = "N/A";
//                        }
//                        if (totdownFault == 0) {
//                            downFaultStr = "N/A";
//                        }
//
//                        if (totcardCloseFault == 0) {
//                            cardCloseFaultStr = "N/A";
//                        }
//
//                        atmDetailsEdit.add(atm + "#" + brcode + "#" + location + "#" + div + "#" + vender
//                                + "#" + cstEmptyStr + totcstEmptyTime
//                                + "#" + cstFaultStr + totcstFaultTime
//                                + "#" + pinFaultStr + totpinFaultTime
//                                + "#" + cardFaultStr + totcardFaultTime
//                                + "#" + moneyDrwFaultStr + totmoneyDrwFaultTime
//                                + "#" + downColseFaultStr + totdownColseFaultTime
//                                + "#" + downFaultStr + totdownFaultTime
//                                + "#" + cardCloseFaultStr + totcardCloseFaultTime
//                                + totErrors);
//                    } else {
//                        startindex++;
//                    }
//
//                } catch (Exception e) {
//                    System.out.println(e.toString());
//                }
//            }
////            printAll(atmDetailsEdit);
//            ResultJson = "[";
//            for (int i = 0; i < atmDetailsEdit.size(); i++) {
//
//                try {
//                    String atm = atmDetailsEdit.get(i).split("#")[0].trim();
//                    String brcode = atmDetailsEdit.get(i).split("#")[1].trim();
//                    String location = atmDetailsEdit.get(i).split("#")[2].trim();
//                    String div = atmDetailsEdit.get(i).split("#")[3].trim();
//                    String vender = atmDetailsEdit.get(i).split("#")[4].trim();
//                    String cstEmptyStrR = atmDetailsEdit.get(i).split("#")[5].trim();
//                    String cstFaultStrR = atmDetailsEdit.get(i).split("#")[6].trim();
//                    String pinFaultStrR = atmDetailsEdit.get(i).split("#")[7].trim();
//                    String cardFaultStrR = atmDetailsEdit.get(i).split("#")[8].trim();
//                    String moneyDrwFaultStrR = atmDetailsEdit.get(i).split("#")[9].trim();
//                    String downColseFaultStrR = atmDetailsEdit.get(i).split("#")[10].trim();
//                    String downFaultStrR = atmDetailsEdit.get(i).split("#")[11].trim();
//                    String cardCloseFaultStrR = atmDetailsEdit.get(i).split("#")[12].trim();
//
//                    ResultJson += "{\"ATM\":\"" + atm + "\",\"BRCODE\":" + brcode + ",\"LOCAION\":\"" + location
//                            + "\",\"div\":\"" + div + "\",\"vender\":\"" + vender
//                            + "\",\"cstEmpty\":\"" + cstEmptyStrR + "\",\"cstFault\":\"" + cstFaultStrR + "\",\"pinFault\":\"" + pinFaultStrR
//                            + "\",\"cardFault\":\"" + cardFaultStrR + "\",\"moneyDrwFault\":\"" + moneyDrwFaultStrR + "\",\"downColseFault\":\"" + downColseFaultStrR
//                            + "\",\"downFault\":\"" + downFaultStrR
//                            + "\",\"cardCloseFault\":\"" + cardCloseFaultStrR
//                            + "\"},";
////                            + "\"},";
////                    ResultJson += "{\"ATM\":\"" + atm + "\",\"BRCODE\":" + brcode + ",\"LOCAION\":\"" + location + "\",\"cstEmpty\":\"" + cstEmptyStrR
////                            + "\",\"cstFault\":\"" + cstFaultStrR + "\",\"pinFault\":\"" + pinFaultStrR + "\",\"cardFault\":\"" + cardFaultStrR
////                            + "\",\"moneyDrwFault\":\"" + moneyDrwFaultStrR + "\",\"downColseFault\":\"" + downColseFaultStrR + "\"},";
////                    System.out.println(atmDetailsEdit.get(i).trim());
//                } catch (Exception e) {
//                    System.out.println(e.toString());
//                }
//
//            }
//            ResultJson += "]";
//            ResultJson = ResultJson.replaceAll("},]", "}]");
////            System.out.println(ResultJson);
//
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//
//        return ResultJson;
//
//    }
//
//    
    public String getDownTimeFaultTypeWise(String fromDate, String toDate, int userLvl, int uBr) {

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
                        + "order by s.station,s.updated_time";
            } else {
                sql = "select s.*,A.DIV_TYPE,V.VENDER_NAME,A.BR_CODE,A.LOCATION,A.PROTOCOL from ATM_STS_DESC_SUMMARY s,ATM_INFO a,VENDER_DETAILS v "
                        + "where s.station = a.atm_name and a.vender = v.vender_id and "
                        + "s.flt_date between TO_DATE( '" + fromDate + "', 'yyyy-mm-dd') and TO_DATE('" + endDate + "', 'yyyy-mm-dd') "
                        + "and a.BR_CODE = " + uBr + " "
                        + "order by s.station,s.updated_time";
            }
            stmnt = connection.createStatement();
            rs = stmnt.executeQuery(sql);

            ArrayList<String> atmDetails = new ArrayList<String>();

            while (rs.next()) {
                String atm = rs.getString(1).trim();
                String err = rs.getString(2).trim();
                String breakTime = rs.getString(3).trim();
                String networkTime = rs.getString(4).trim();
                String div = rs.getString(6).trim();
                String vender = rs.getString(7).trim();
                String brcode = rs.getString(8).trim();
                String location = "";
                if (rs.getString(9) != null) {
                    location = rs.getString(9);
                } else {
                    location = "__________";
                }
                int protocol = Integer.parseInt(rs.getString(10).trim());

                if (div.equals("CRM") && protocol == 2) {
                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "NDCCRM" + "~" + brcode + "~" + location + "~" + networkTime + "~" + div + "~" + vender);

                } else if ((div.equals("CRM")) && protocol == 1) {
                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "DDCCRM" + "~" + brcode + "~" + location + "~" + networkTime + "~" + div + "~" + vender);

                } else if ((div.equals("CDM")) && protocol == 1) {
                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "DDCCDM" + "~" + brcode + "~" + location + "~" + networkTime + "~" + div + "~" + vender);

                } else if ((div.equals("ATM")) && protocol == 1) {
                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "DDCATM" + "~" + brcode + "~" + location + "~" + networkTime + "~" + div + "~" + vender);
                }
            }
            rs.close();
            stmnt.close();
            obj.releaseCon(connection);

            Date date = new Date();
            ArrayList<String> atmDetailsEdit = new ArrayList<String>();
            ReportsQuery AllDownTimeQuery = new ReportsQuery();

            String atmNext = "";
            int startindex = 0;
            int totErrors = 0;

            int totcstEmpty = 0;
            int totcstFault = 0;
            int totpinFault = 0;
            int totcardFault = 0;
            int totmoneyDrwFault = 0;
            int totdownColseFault = 0;
            int totdownFault = 0;
            int totcardCloseFault = 0;

            float totalcstEmptyTime = 0;
            float totalcstFaultTime = 0;
            float totalpinFaultTime = 0;
            float totalcardFaultTime = 0;
            float totalmoneyDrwFaultTime = 0;
            float totaldownColseFaultTime = 0;
            float totaldownFaultTime = 0;
            float totalcardCloseFaultTime = 0;

            float totcstEmptyTime = 0;
            float totcstFaultTime = 0;
            float totpinFaultTime = 0;
            float totcardFaultTime = 0;
            float totmoneyDrwFaultTime = 0;
            float totdownColseFaultTime = 0;
            float totdownFaultTime = 0;
            float totcardCloseFaultTime = 0;

            String cstEmpty;
            String cstEmptyStartTime = "";
            String cstEmptyEndTime = "";
            String cstEmptyStartTimeFirst = "";
            String NextCstEmpty = "";
            String cstEmptyStr = "";
            int CstEmptyCount = 0;

            String cstFault;
            String cstFaultStartTime = "";
            String cstFaultEndTime = "";
            String cstFaultStartTimeFirst = "";
            String NextCstFault = "";
            String cstFaultStr = "";
            int CstFaultCount = 0;

            String pinFault;
            String pinFaultStartTime = "";
            String pinFaultEndTime = "";
            String pinFaultStartTimeFirst = "";
            String NextPinFault = "";
            String pinFaultStr = "";
            int pinFaultCount = 0;

            String cardFault;
            String cardFaultStartTime = "";
            String cardFaultEndTime = "";
            String cardFaultStartTimeFirst = "";
            String NextCardFault = "";
            String cardFaultStr = "";
            int cardFaultCount = 0;

            String moneyDrwFault;
            String moneyDrwFaultStartTime = "";
            String moneyDrwFaultEndTime = "";
            String moneyDrwFaultStartTimeFirst = "";
            String NextMoneyDrwFault = "";
            String moneyDrwFaultStr = "";
            int moneyDrwFaultCount = 0;

            String downColseFault;
            String downColseFaultStartTime = "";
            String downColseFaultEndTime = "";
            String downColseFaultStartTimeFirst = "";
            String NextDownColseFault = "";
            String downColseFaultStr = "";
            int downColseFaultCount = 0;

            String downFault;
            String downFaultStartTime = "";
            String downFaultEndTime = "";
            String downFaultStartTimeFirst = "";
            String NextDownFault = "";
            String downFaultStr = "";
            int downFaultCount = 0;

            String cardCloseFault;
            String cardCloseFaultStartTime = "";
            String cardCloseFaultEndTime = "";
            String cardCloseFaultStartTimeFirst = "";
            String NextCardCloseFault = "";
            String cardCloseFaultStr = "";
            int cardCloseFaultCount = 0;

            String closeFault;
            String closeFaultStartTime = "";
            String closeFaultEndTime = "";
            String closeFaultStartTimeFirst = "";
            String NextCloseFault = "";
            String closeFaultStr = "";
            int closeFaultCount = 0;

//            String stat;
//            String state;
//            int networkTimeDiff = 20;
//            int downTimeTimeDiff = 10;
            int networkTimeDiff = 5;
            int downTimeTimeDiff = 5;

            int totalcstEmpty = 0;
            int totalcstFault = 0;
            int totalpinFault = 0;
            int totalcardFault = 0;
            int totalmoneyDrwFault = 0;
            int totaldownColseFault = 0;
            int totaldownFault = 0;
            int totalcardCloseFault = 0;

            for (int i = 0; i < atmDetails.size(); i++) {
                try {
                    String atm = atmDetails.get(i).split("~")[0];
                    String errCode = atmDetails.get(i).split("~")[1];
                    String brkTime = atmDetails.get(i).split("~")[2];
                    String type = atmDetails.get(i).split("~")[3];
                    Integer brcode = Integer.parseInt(atmDetails.get(i).split("~")[4]);
                    String location = atmDetails.get(i).split("~")[5];
                    String brkTimeNetwork = atmDetails.get(i).split("~")[6];
                    String div = atmDetails.get(i).split("~")[7];
                    String vender = atmDetails.get(i).split("~")[8];

                    cstEmpty = errCode.substring(0, 7).trim();
                    cstFault = errCode.substring(0, 7).trim();
                    downFault = errCode.split(" ")[4];
                    closeFault = errCode.split(" ")[5];
                    pinFault = errCode.split(" ")[8];
                    moneyDrwFault = errCode.split(" ")[9];
                    cardFault = errCode.split(" ")[10];

                    downColseFault = downFault + " " + closeFault;

                    if (i != atmDetails.size() - 1) {
                        atmNext = atmDetails.get(i + 1).split("~")[0];
                        NextCstEmpty = atmDetails.get(i + 1).split("~")[1].substring(0, 7).trim();
                        NextCstFault = atmDetails.get(i + 1).split("~")[1].substring(0, 7).trim();
                        NextPinFault = atmDetails.get(i + 1).split("~")[1].split(" ")[8];
                        NextCardFault = atmDetails.get(i + 1).split("~")[1].split(" ")[10];
                        NextMoneyDrwFault = atmDetails.get(i + 1).split("~")[1].split(" ")[9];
                        NextDownColseFault = atmDetails.get(i + 1).split("~")[1].split(" ")[4] + " " + atmDetails.get(i + 1).split("~")[1].split(" ")[5]; // down or closed
                        NextDownFault = atmDetails.get(i + 1).split("~")[1].split(" ")[4]; // down
                        NextCardCloseFault = atmDetails.get(i + 1).split("~")[1].split(" ")[10] + " " + atmDetails.get(i + 1).split("~")[1].split(" ")[5]; // card fault and closed
                        NextCloseFault = atmDetails.get(i + 1).split("~")[1].split(" ")[5]; // down
                    } else if (i == atmDetails.size() - 1) {
                        atmNext = "";
                        NextCstEmpty = "";
                        NextCstFault = "";
                        NextPinFault = "";
                        NextCardFault = "";
                        NextMoneyDrwFault = "";
                        NextDownColseFault = "";
                        NextDownFault = "";
                        NextCardCloseFault = "";
                        NextCloseFault = "";
                    }

                    //Start Time
                    if (startindex == 0) {
                        //All Cassette Empty
                        if ((cstEmpty.equals("2 2 2 2") && !type.equals("NDCCRM")) || (cstEmpty.equals("3 3 3 3") && type.equals("NDCCRM"))) {
//                            cstEmptyStartTime = fromDate + " 00:00:00";
                            cstEmptyStartTime = brkTime;
                        } else {
                            cstEmptyStartTime = brkTime;
                        }

                        //All Cassette Fault
                        if ((cstFault.equals("9 9 9 9") && !type.equals("NDCCRM")) || (cstFault.equals("0 0 0 0") && type.equals("NDCCRM"))) {
//                            cstFaultStartTime = fromDate + " 00:00:00";
                            cstFaultStartTime = brkTime;
                        } else {
                            cstFaultStartTime = brkTime;
                        }

                        //PinPad Fault
                        if ((pinFault.equals("1") && !type.equals("NDCCRM")) || (pinFault.equals("4") && type.equals("NDCCRM"))) {
//                            pinFaultStartTime = fromDate + " 00:00:00";
                            pinFaultStartTime = brkTime;
                        } else {
                            pinFaultStartTime = brkTime;
                        }

                        //CardReader Fault
                        if ((!cardFault.equals("0") && !type.equals("NDCCRM")) || (!cardFault.equals("0") && type.equals("NDCCRM"))) {
//                            cardFaultStartTime = fromDate + " 00:00:00";
                            cardFaultStartTime = brkTime;
                        } else {
                            cardFaultStartTime = brkTime;
                        }

                        //ModeyDrawer Fault
                        if (((moneyDrwFault.equals("1") || moneyDrwFault.equals("2")) && type.equals("DDCATM"))
                                || (!(moneyDrwFault.equals("0") || moneyDrwFault.equals("1")) && (type.equals("DDCCDM") || type.equals("DDCCRM")))
                                || ((moneyDrwFault.equals("140") || moneyDrwFault.equals("440")) && type.equals("NDCCRM"))) {
//                            moneyDrwFaultStartTime = fromDate + " 00:00:00";
                            moneyDrwFaultStartTime = brkTime;
                        } else {
                            moneyDrwFaultStartTime = brkTime;
                        }

                        //Down/Close Fault
                        if (downColseFault.equals("0 0") || downColseFault.equals("0 1") || downColseFault.equals("1 0")) {
//                            downColseFaultStartTime = fromDate + " 00:00:00";
                            downColseFaultStartTime = brkTimeNetwork;
                        } else {
                            downColseFaultStartTime = brkTimeNetwork;
                        }

                        //Down Fault
                        if (downFault.equals("0")) {
//                            downFaultStartTime = fromDate + " 00:00:00";
                            downFaultStartTime = brkTimeNetwork;
                        } else {
                            downFaultStartTime = brkTimeNetwork;
                        }

                        //CardReader Fault + Closed
                        if (((!cardFault.equals("0") && !type.equals("NDCCRM")) || (!cardFault.equals("0") && type.equals("NDCCRM"))) && closeFault.equals("0")) {
//                            cardCloseFaultStartTime = fromDate + " 00:00:00";
                            cardCloseFaultStartTime = brkTimeNetwork;
                        } else {
                            cardCloseFaultStartTime = brkTimeNetwork; //use the networktime because brktime has the card reader fault time
                        }

                    } else {
                        cstEmptyStartTime = brkTime;
                        cstFaultStartTime = brkTime;
                        pinFaultStartTime = brkTime;
                        cardFaultStartTime = brkTime;
                        moneyDrwFaultStartTime = brkTime;
                        downColseFaultStartTime = brkTimeNetwork;
                        downFaultStartTime = brkTimeNetwork;
                        cardCloseFaultStartTime = brkTimeNetwork; //use the networktime because brktime has the card reader fault time
                    }

                    //End Time
                    if (!atm.equals(atmNext)) {
                        //cassette empty
                        if ((cstEmpty.equals("2 2 2 2") && !type.equals("NDCCRM")) || (cstEmpty.equals("3 3 3 3") && type.equals("NDCCRM"))) {
                            if (sdf.format(date).equals(toDate)) {
                                cstEmptyEndTime = toDate + " " + sdf2.format(date);
                            } else {
                                cstEmptyEndTime = toDate + " 24:00:00";
                            }

                        } else {
                            cstEmptyEndTime = brkTime;
                        }

                        //cassete fault
                        if ((cstFault.equals("9 9 9 9") && !type.equals("NDCCRM")) || (cstFault.equals("0 0 0 0") && type.equals("NDCCRM"))) {
                            if (sdf.format(date).equals(toDate)) {
                                cstFaultEndTime = toDate + " " + sdf2.format(date);
                            } else {
                                cstFaultEndTime = toDate + " 24:00:00";
                            }
                        } else {
                            cstFaultEndTime = brkTime;
                        }
//                        
                        //PinPad fault
                        if ((pinFault.equals("1") && !type.equals("NDCCRM")) || (pinFault.equals("4") && type.equals("NDCCRM"))) {
                            if (sdf.format(date).equals(toDate)) {
                                pinFaultEndTime = toDate + " " + sdf2.format(date);
                            } else {
                                pinFaultEndTime = toDate + " 24:00:00";
                            }
                        } else {
                            pinFaultEndTime = brkTime;
                        }
//                        
                        //CardReader fault
                        if ((!cardFault.equals("0") && !type.equals("NDCCRM")) || (!cardFault.equals("0") && type.equals("NDCCRM"))) {
                            if (sdf.format(date).equals(toDate)) {
                                cardFaultEndTime = toDate + " " + sdf2.format(date);
                            } else {
                                cardFaultEndTime = toDate + " 24:00:00";
                            }
                        } else {
                            cardFaultEndTime = brkTime;
                        }

                        //MoneyDrawer fault
                        if (((moneyDrwFault.equals("1") || moneyDrwFault.equals("2")) && type.equals("DDCATM"))
                                || (!(moneyDrwFault.equals("0") || moneyDrwFault.equals("1")) && (type.equals("DDCCDM") || type.equals("DDCCRM")))
                                || ((moneyDrwFault.equals("140") || moneyDrwFault.equals("440")) && type.equals("NDCCRM"))) {
                            if (sdf.format(date).equals(toDate)) {
                                moneyDrwFaultEndTime = toDate + " " + sdf2.format(date);
                            } else {
                                moneyDrwFaultEndTime = toDate + " 24:00:00";
                            }
                        } else {
                            moneyDrwFaultEndTime = brkTime;
                        }

                        //Down/Closed fault
                        if (downFault.equals("0") || closeFault.equals("0")) {
                            if (sdf.format(date).equals(toDate)) {
                                downColseFaultEndTime = toDate + " " + sdf2.format(date);
                            } else {
                                downColseFaultEndTime = toDate + " 24:00:00";
                            }
                        } else {
                            downColseFaultEndTime = brkTimeNetwork;
                        }

                        //Down fault
                        if (downFault.equals("0")) {
                            if (sdf.format(date).equals(toDate)) {
                                downFaultEndTime = toDate + " " + sdf2.format(date);
                            } else {
                                downFaultEndTime = toDate + " 24:00:00";
                            }
                        } else {
                            downFaultEndTime = brkTimeNetwork;
                        }

                        //CardReader fault + closed
                        if (((!cardFault.equals("0") && !type.equals("NDCCRM")) || (!cardFault.equals("0") && type.equals("NDCCRM"))) && closeFault.equals("0")) {
                            if (sdf.format(date).equals(toDate)) {
                                cardCloseFaultEndTime = toDate + " " + sdf2.format(date);
                            } else {
                                cardCloseFaultEndTime = toDate + " 24:00:00";
                            }
                        } else {
                            cardCloseFaultEndTime = brkTimeNetwork; //use the networktime because brktime has the card reader fault time
                        }

                    } else {
                        if (i != atmDetails.size() - 1) {
                            cstEmptyEndTime = atmDetails.get(i + 1).split("~")[2];
                            cstFaultEndTime = atmDetails.get(i + 1).split("~")[2];
                            pinFaultEndTime = atmDetails.get(i + 1).split("~")[2];
                            cardFaultEndTime = atmDetails.get(i + 1).split("~")[2];
                            moneyDrwFaultEndTime = atmDetails.get(i + 1).split("~")[2];
                            downColseFaultEndTime = atmDetails.get(i + 1).split("~")[6];
                            downFaultEndTime = atmDetails.get(i + 1).split("~")[6];
                            cardCloseFaultEndTime = atmDetails.get(i + 1).split("~")[6];//use the networktime because brktime has the card reader fault time
                        } else {
                            cstEmptyEndTime = brkTime;
                            cstFaultEndTime = brkTime;
                            pinFaultEndTime = brkTime;
                            cardFaultEndTime = brkTime;
                            moneyDrwFaultEndTime = brkTime;
                            downColseFaultEndTime = brkTimeNetwork;
                            downFaultEndTime = brkTimeNetwork;
                            cardCloseFaultEndTime = brkTimeNetwork;//use the networktime because brktime has the card reader fault time
                        }
                    }

                    //Calculate
                    //Not HITACHI CRM
                    if (!type.equals("NDCCRM")) {
                        //cassette Empty
                        if (cstEmpty.equals("2 2 2 2") || cstEmpty.equals("3 3 3 3")) {
                            if (cstEmpty.equals(NextCstEmpty)) {
                                CstEmptyCount++;
                                if (CstEmptyCount == 1) {
                                    cstEmptyStartTimeFirst = cstEmptyStartTime;
                                }
                            } else {
                                if (CstEmptyCount == 0) {
                                    cstEmptyStartTimeFirst = cstEmptyStartTime;
                                }

                                if ((AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60) > downTimeTimeDiff) {
                                    cstEmptyStr += cstEmptyStartTimeFirst.substring(2, cstEmptyStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + cstEmptyEndTime.substring(2, cstEmptyEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totcstEmpty++;
                                    totalcstEmptyTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60));
                                }

                                CstEmptyCount = 0;
                                cstEmptyStartTimeFirst = "N/A";

                            }
                        }

                        //cassette Fault
                        if (cstFault.equals("9 9 9 9")) {
                            if (cstFault.equals(NextCstFault)) {
                                CstFaultCount++;
                                if (CstFaultCount == 1) {
                                    cstFaultStartTimeFirst = cstFaultStartTime;
                                }
                            } else {
                                if (CstFaultCount == 0) {
                                    cstFaultStartTimeFirst = cstFaultStartTime;
                                }

                                if ((AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60) > downTimeTimeDiff) {
                                    cstFaultStr += cstFaultStartTimeFirst.substring(2, cstFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + cstFaultEndTime.substring(2, cstFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totcstFault++;
                                    totalcstFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60));
                                }

                                CstFaultCount = 0;
                                cstFaultStartTimeFirst = "N/A";
                            }
                        }

                        //PinPad Fault
                        if (pinFault.equals("1")) {
                            if (pinFault.equals(NextPinFault)) {
                                pinFaultCount++;
                                if (pinFaultCount == 1) {
                                    pinFaultStartTimeFirst = pinFaultStartTime;
                                }
                            } else {
                                if (pinFaultCount == 0) {
                                    pinFaultStartTimeFirst = pinFaultStartTime;
                                }
                                if ((AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60) > downTimeTimeDiff) {
                                    pinFaultStr += pinFaultStartTimeFirst.substring(2, pinFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + pinFaultEndTime.substring(2, pinFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totpinFault++;
                                    totalpinFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60));
                                }

                                pinFaultCount = 0;
                                pinFaultStartTimeFirst = "N/A";
                            }
                        }
//                        
                        //CardReader Fault
                        if (!cardFault.equals("0")) {
                            if (cardFault.equals(NextCardFault)) {
                                cardFaultCount++;
                                if (cardFaultCount == 1) {
                                    cardFaultStartTimeFirst = cardFaultStartTime;
                                }
                            } else {
                                if (cardFaultCount == 0) {
                                    cardFaultStartTimeFirst = cardFaultStartTime;
                                }
                                if ((AllDownTimeQuery.getHourDifference(cardFaultStartTimeFirst, cardFaultEndTime) * 60) > downTimeTimeDiff) {
                                    cardFaultStr += cardFaultStartTimeFirst.substring(2, cardFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + cardFaultEndTime.substring(2, cardFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(cardFaultStartTimeFirst, cardFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totcardFault++;
                                    totalcardFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cardFaultStartTimeFirst, cardFaultEndTime) * 60));
                                }

                                cardFaultCount = 0;
                                cardFaultStartTimeFirst = "N/A";
                            }
                        }

                        //MoneyDrawer Fault
                        if (((moneyDrwFault.equals("1") || moneyDrwFault.equals("2")) && type.equals("DDCATM"))
                                || (!(moneyDrwFault.equals("0") || moneyDrwFault.equals("1")) && (type.equals("DDCCDM") || type.equals("DDCCRM")))) {
                            if (moneyDrwFault.equals(NextMoneyDrwFault)) {
                                moneyDrwFaultCount++;
                                if (moneyDrwFaultCount == 1) {
                                    moneyDrwFaultStartTimeFirst = moneyDrwFaultStartTime;
                                }
                            } else {
                                if (moneyDrwFaultCount == 0) {
                                    moneyDrwFaultStartTimeFirst = moneyDrwFaultStartTime;
                                }

                                if ((AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60) > downTimeTimeDiff) {
                                    moneyDrwFaultStr += moneyDrwFaultStartTimeFirst.substring(2, moneyDrwFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + moneyDrwFaultEndTime.substring(2, moneyDrwFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totmoneyDrwFault++;
                                    totalmoneyDrwFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60));
                                }

                                moneyDrwFaultCount = 0;
                                moneyDrwFaultStartTimeFirst = "N/A";
                            }
                        }

                        //Down/Close Fault
                        if (downFault.equals("0") || closeFault.equals("0")) {
                            if (NextDownColseFault.equals("0 0") || NextDownColseFault.equals("0 1") || NextDownColseFault.equals("1 0")) {
                                downColseFaultCount++;
                                if (downColseFaultCount == 1) {
                                    downColseFaultStartTimeFirst = downColseFaultStartTime;
                                }
                            } else {
                                if (downColseFaultCount == 0) {
                                    downColseFaultStartTimeFirst = downColseFaultStartTime;
                                }

                                //Taking only morethan 20 min
                                if ((AllDownTimeQuery.getHourDifference(downColseFaultStartTimeFirst, downColseFaultEndTime) * 60) > downTimeTimeDiff) {
                                    downColseFaultStr += downColseFaultStartTimeFirst.substring(2, downColseFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + downColseFaultEndTime.substring(2, downColseFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(downColseFaultStartTimeFirst, downColseFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totdownColseFault++;
                                    totaldownColseFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(downColseFaultStartTimeFirst, downColseFaultEndTime) * 60));

                                }
                                downColseFaultCount = 0;
                                downColseFaultStartTimeFirst = "N/A";

                            }
                        }

                        //Down Fault
                        if (downFault.equals("0")) {
                            if (downFault.equals(NextDownFault)) {
                                downFaultCount++;
                                if (downFaultCount == 1) {
                                    downFaultStartTimeFirst = downFaultStartTime;
                                }
                            } else {
                                if (downFaultCount == 0) {
                                    downFaultStartTimeFirst = downFaultStartTime;
                                }

                                //Taking only morethan 20 min
                                if ((AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60) > downTimeTimeDiff) {
                                    downFaultStr += downFaultStartTimeFirst.substring(2, downFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + downFaultEndTime.substring(2, downFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totdownFault++;
                                    totaldownFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60));

                                }
                                downFaultCount = 0;
                                downFaultStartTimeFirst = "N/A";

                            }
                        }

                        //CardReader Fault + close
                        if (!cardFault.equals("0") && closeFault.equals("0")) {
                            if (cardFault.equals(NextCardFault) && closeFault.equals(NextCloseFault)) {
                                cardCloseFaultCount++;
                                if (cardCloseFaultCount == 1) {
                                    cardCloseFaultStartTimeFirst = cardCloseFaultStartTime;
                                }
                            } else {
                                if (cardCloseFaultCount == 0) {
                                    cardCloseFaultStartTimeFirst = cardCloseFaultStartTime;
                                }
                                if ((AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60) > downTimeTimeDiff) {
                                    cardCloseFaultStr += cardCloseFaultStartTimeFirst.substring(2, cardCloseFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + cardCloseFaultEndTime.substring(2, cardCloseFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totcardCloseFault++;
                                    totalcardCloseFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60));
                                }

                                cardCloseFaultCount = 0;
                                cardCloseFaultStartTimeFirst = "N/A";
                            }
                        }

                    } //HITACHI CRM
                    else if (type.equals("NDCCRM")) {
                        //cassette Empty
                        if (cstEmpty.equals("3 3 3 3")) {
                            if (cstEmpty.equals(NextCstEmpty)) {
                                CstEmptyCount++;
                                if (CstEmptyCount == 1) {
                                    cstEmptyStartTimeFirst = cstEmptyStartTime;
                                }
                            } else {
                                if (CstEmptyCount == 0) {
                                    cstEmptyStartTimeFirst = cstEmptyStartTime;
                                }
                                if ((AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60) > downTimeTimeDiff) {
                                    cstEmptyStr += cstEmptyStartTimeFirst.substring(2, cstEmptyStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + cstEmptyEndTime.substring(2, cstEmptyEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totcstEmpty++;
                                    totalcstEmptyTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60));
                                }

                                CstEmptyCount = 0;
                                cstEmptyStartTimeFirst = "N/A";
                            }
                        }

                        //cassette Fault
                        if (cstFault.equals("0 0 0 0")) {
                            if (cstFault.equals(NextCstFault)) {
                                CstFaultCount++;
                                if (CstFaultCount == 1) {
                                    cstFaultStartTimeFirst = cstFaultStartTime;
                                }
                            } else {
                                if (CstFaultCount == 0) {
                                    cstFaultStartTimeFirst = cstFaultStartTime;
                                }
                                if ((AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60) > downTimeTimeDiff) {
                                    cstFaultStr += cstFaultStartTimeFirst.substring(2, cstFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + cstFaultEndTime.substring(2, cstFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totcstFault++;
                                    totalcstFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60));
                                }

                                CstFaultCount = 0;
                                cstFaultStartTimeFirst = "N/A";
                            }
                        }

                        //PinPad Fault
                        if (pinFault.equals("4")) {
                            if (pinFault.equals(NextPinFault)) {
                                pinFaultCount++;
                                if (pinFaultCount == 1) {
                                    pinFaultStartTimeFirst = pinFaultStartTime;
                                }
                            } else {
                                if (pinFaultCount == 0) {
                                    pinFaultStartTimeFirst = pinFaultStartTime;
                                }

                                if ((AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60) > downTimeTimeDiff) {
                                    pinFaultStr += pinFaultStartTimeFirst.substring(2, pinFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + pinFaultEndTime.substring(2, pinFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totpinFault++;
                                    totalpinFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60));
                                }

                                pinFaultCount = 0;
                                pinFaultStartTimeFirst = "N/A";
                            }
                        }
//                        
                        //CardReader Fault
                        if (!cardFault.equals("0")) {
                            if (cardFault.equals(NextCardFault)) {
                                cardFaultCount++;
                                if (cardFaultCount == 1) {
                                    cardFaultStartTimeFirst = cardFaultStartTime;
                                }
                            } else {
                                if (cardFaultCount == 0) {
                                    cardFaultStartTimeFirst = cardFaultStartTime;
                                }
                                if ((AllDownTimeQuery.getHourDifference(cardFaultStartTimeFirst, cardFaultEndTime) * 60) > downTimeTimeDiff) {
                                    cardFaultStr += cardFaultStartTimeFirst.substring(2, cardFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + cardFaultEndTime.substring(2, cardFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(cardFaultStartTimeFirst, cardFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totcardFault++;
                                    totalcardFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cardFaultStartTimeFirst, cardFaultEndTime) * 60));
                                }

                                cardFaultCount = 0;
                                cardFaultStartTimeFirst = "N/A";
                            }
                        }

                        //MoneyDrawer Fault
                        if ((moneyDrwFault.equals("140") || moneyDrwFault.equals("440"))) {
                            if (moneyDrwFault.equals(NextMoneyDrwFault)) {
                                moneyDrwFaultCount++;
                                if (moneyDrwFaultCount == 1) {
                                    moneyDrwFaultStartTimeFirst = moneyDrwFaultStartTime;
                                }
                            } else {
                                if (moneyDrwFaultCount == 0) {
                                    moneyDrwFaultStartTimeFirst = moneyDrwFaultStartTime;
                                }
                                if ((AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60) > downTimeTimeDiff) {
                                    moneyDrwFaultStr += moneyDrwFaultStartTimeFirst.substring(2, moneyDrwFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + moneyDrwFaultEndTime.substring(2, moneyDrwFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totmoneyDrwFault++;
                                    totalmoneyDrwFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60));
                                }

                                moneyDrwFaultCount = 0;
                                moneyDrwFaultStartTimeFirst = "N/A";
                            }
                        }

                        //Down/Close Fault
                        if (downFault.equals("0") || closeFault.equals("0")) {
                            if (NextDownColseFault.equals("0 0") || NextDownColseFault.equals("0 1") || NextDownColseFault.equals("1 0")) {
                                downColseFaultCount++;
                                if (downColseFaultCount == 1) {
                                    downColseFaultStartTimeFirst = downColseFaultStartTime;
                                }
                            } else {
                                if (downColseFaultCount == 0) {
                                    downColseFaultStartTimeFirst = downColseFaultStartTime;
                                }

                                //Taking only morethan 20 min
                                if ((AllDownTimeQuery.getHourDifference(downColseFaultStartTimeFirst, downColseFaultEndTime) * 60) > downTimeTimeDiff) {
                                    downColseFaultStr += downColseFaultStartTimeFirst.substring(2, downColseFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + downColseFaultEndTime.substring(2, downColseFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(downColseFaultStartTimeFirst, downColseFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totdownColseFault++;
                                    totaldownColseFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(downColseFaultStartTimeFirst, downColseFaultEndTime) * 60));

                                }
                                downColseFaultCount = 0;
                                downColseFaultStartTimeFirst = "N/A";

                            }
                        }

                        //Down Fault
                        if (downFault.equals("0")) {
                            if (NextDownFault.equals("0")) {
                                downFaultCount++;
                                if (downFaultCount == 1) {
                                    downFaultStartTimeFirst = downFaultStartTime;
                                }
                            } else {
                                if (downFaultCount == 0) {
                                    downFaultStartTimeFirst = downFaultStartTime;
                                }

                                //Taking only morethan 20 min
                                if ((AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60) > networkTimeDiff) {
                                    downFaultStr += downFaultStartTimeFirst.substring(2, downFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + downFaultEndTime.substring(2, downFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totdownFault++;
                                    totaldownFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60));
                                }
                                downFaultCount = 0;
                                downFaultStartTimeFirst = "N/A";

                            }
                        }

                        //CardReader Fault + close
                        if (!cardFault.equals("0") && closeFault.equals("0")) {
                            if (cardFault.equals(NextCardFault) && closeFault.equals("0")) {
                                cardCloseFaultCount++;
                                if (cardCloseFaultCount == 1) {
                                    cardCloseFaultStartTimeFirst = cardCloseFaultStartTime;
                                }
                            } else {
                                if (cardCloseFaultCount == 0) {
                                    cardCloseFaultStartTimeFirst = cardCloseFaultStartTime;
                                }
                                if ((AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60) > downTimeTimeDiff) {
                                    cardCloseFaultStr += cardCloseFaultStartTimeFirst.substring(2, cardCloseFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                            + cardCloseFaultEndTime.substring(2, cardCloseFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                            + df.format(AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60) + "(min)~";
                                    totErrors++;
                                    totcardCloseFault++;
                                    totalcardCloseFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60));
                                }

                                cardCloseFaultCount = 0;
                                cardCloseFaultStartTimeFirst = "N/A";

                            }
                        }

                    }

                    //********************************************************
                    // If atm change Reset all values and add to array else assign last value 
                    if (!atm.equals(atmNext)) {

//                        totalcstEmptyTime += totcstEmptyTime;
//                        totalcstFaultTime += totcstFaultTime;
//                        totalpinFaultTime += totpinFaultTime;
//                        totalcardFaultTime += totcardFaultTime;
//                        totalmoneyDrwFaultTime += totmoneyDrwFaultTime;
//                        totaldownColseFaultTime += totdownColseFaultTime;
//                        totaldownFaultTime += totdownFaultTime;
//                        totalcardCloseFaultTime += totcardCloseFaultTime;
                        if (totcstEmpty == 0) {
                            cstEmptyStr = "N/A";
                        }
                        if (totcstFault == 0) {
                            cstFaultStr = "N/A";
                        }
                        if (totpinFault == 0) {
                            pinFaultStr = "N/A";
                        }
                        if (totcardFault == 0) {
                            cardFaultStr = "N/A";
                        }
                        if (totmoneyDrwFault == 0) {
                            moneyDrwFaultStr = "N/A";
                        }
                        if (totdownColseFault == 0) {
                            downColseFaultStr = "N/A";
                        }
                        if (totdownFault == 0) {
                            downFaultStr = "N/A";
                        }
                        if (totcardCloseFault == 0) {
                            cardCloseFaultStr = "N/A";
                        }

                        if (totcstEmpty > 0) {
                            totalcstEmpty++;
                        }
                        if (totcstFault > 0) {
                            totalcstFault++;
                        }
                        if (totpinFault > 0) {
                            totalpinFault++;
                        }
                        if (totmoneyDrwFault > 0) {
                            totalmoneyDrwFault++;
                        }
                        if (totdownFault > 0) {
                            totaldownFault++;
                        }
                        if (totcardCloseFault > 0) {
                            totalcardCloseFault++;
                        }

//                        atmDetailsEdit.add(atm + "#" + brcode + "#" + location + "#" + div + "#" + vender
//                                + "#" + cstEmptyStr + totcstEmptyTime
//                                + "#" + cstFaultStr + totcstFaultTime
//                                + "#" + pinFaultStr + totpinFaultTime
//                                + "#" + cardFaultStr + totcardFaultTime
//                                + "#" + moneyDrwFaultStr + totmoneyDrwFaultTime
//                                + "#" + downColseFaultStr + totdownColseFaultTime
//                                + "#" + downFaultStr + totdownFaultTime
//                                + "#" + cardCloseFaultStr + totcardCloseFaultTime
//                                + totErrors);
//                        atmDetailsEdit.add(atm + "#" + brcode + "#" + location
//                                + "#" + cstEmptyStr + totcstEmpty
//                                + "#" + cstFaultStr + totcstFault
//                                + "#" + pinFaultStr + totpinFault
//                                + "#" + cardFaultStr + totcardFault
//                                + "#" + moneyDrwFaultStr + totmoneyDrwFault
//                                + "#" + downColseFaultStr + totdownColseFault
//                                + totErrors);
                        startindex = 0;
                        totErrors = 0;

                        //cassette Empty
                        NextCstEmpty = "";
                        CstEmptyCount = 0;
                        cstEmptyStartTimeFirst = "N/A";
                        cstEmptyStr = "";
                        totcstEmpty = 0;
                        totcstEmptyTime = 0;

                        //cassette Fault
                        NextCstFault = "";
                        CstFaultCount = 0;
                        cstFaultStartTimeFirst = "N/A";
                        cstFaultStr = "";
                        totcstFault = 0;
                        totcstFaultTime = 0;
//                        
                        //PinPad Fault
                        NextPinFault = "";
                        pinFaultCount = 0;
                        pinFaultStartTimeFirst = "N/A";
                        pinFaultStr = "";
                        totpinFault = 0;
                        totpinFaultTime = 0;
//                        
                        //CardReader Fault
                        NextCardFault = "";
                        cardFaultCount = 0;
                        cardFaultStartTimeFirst = "N/A";
                        cardFaultStr = "";
                        totcardFault = 0;
                        totcardFaultTime = 0;

                        //MoneyDrawer Fault
                        NextMoneyDrwFault = "";
                        moneyDrwFaultCount = 0;
                        moneyDrwFaultStartTimeFirst = "N/A";
                        moneyDrwFaultStr = "";
                        totmoneyDrwFault = 0;
                        totmoneyDrwFaultTime = 0;

                        //DownClosed Fault
                        NextDownColseFault = "";
                        downColseFaultCount = 0;
                        downColseFaultStartTimeFirst = "N/A";
                        downColseFaultStr = "";
                        totdownColseFault = 0;
                        totdownColseFaultTime = 0;

                        //Down Fault
                        NextDownFault = "";
                        downFaultCount = 0;
                        downFaultStartTimeFirst = "N/A";
                        downFaultStr = "";
                        totdownFault = 0;
                        totdownFaultTime = 0;

                        //Card Fault + Close
                        NextCardCloseFault = "";
                        cardCloseFaultCount = 0;
                        cardCloseFaultStartTimeFirst = "N/A";
                        cardCloseFaultStr = "";
                        totcardCloseFault = 0;
                        totcardCloseFaultTime = 0;

                    } else if (i == atmDetails.size() - 1) {
                        if (totcstEmpty == 0) {
                            cstEmptyStr = "N/A";
                        }
                        if (totcstFault == 0) {
                            cstFaultStr = "N/A";
                        }
                        if (totpinFault == 0) {
                            pinFaultStr = "N/A";
                        }
                        if (totcardFault == 0) {
                            cardFaultStr = "N/A";
                        }
                        if (totmoneyDrwFault == 0) {
                            moneyDrwFaultStr = "N/A";
                        }
                        if (totdownColseFault == 0) {
                            downColseFaultStr = "N/A";
                        }
                        if (totdownFault == 0) {
                            downFaultStr = "N/A";
                        }

                        if (totcardCloseFault == 0) {
                            cardCloseFaultStr = "N/A";
                        }

                        if (totcstEmpty > 0) {
                            totalcstEmpty++;
                        }
                        if (totcstFault > 0) {
                            totalcstFault++;
                        }
                        if (totpinFault > 0) {
                            totalpinFault++;
                        }
                        if (totmoneyDrwFault > 0) {
                            totalmoneyDrwFault++;
                        }
                        if (totdownFault > 0) {
                            totaldownFault++;
                        }
                        if (totcardCloseFault > 0) {
                            totalcardCloseFault++;
                        }
//
//                        atmDetailsEdit.add(atm + "#" + brcode + "#" + location + "#" + div + "#" + vender
//                                + "#" + cstEmptyStr + totcstEmptyTime
//                                + "#" + cstFaultStr + totcstFaultTime
//                                + "#" + pinFaultStr + totpinFaultTime
//                                + "#" + cardFaultStr + totcardFaultTime
//                                + "#" + moneyDrwFaultStr + totmoneyDrwFaultTime
//                                + "#" + downColseFaultStr + totdownColseFaultTime
//                                + "#" + downFaultStr + totdownFaultTime
//                                + "#" + cardCloseFaultStr + totcardCloseFaultTime
//                                + totErrors);
                    } else {
                        startindex++;
                    }

                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
//            printAll(atmDetailsEdit);
            ResultJson = "[";
//            ResultJson += "{\"CSTEMPTY\":\"" + totalcstEmptyTime + "\",\"CSTFAULT\":\"" + totalcstFaultTime + "\",\"PINFAULT\":\"" + totalpinFaultTime
//                    + "\",\"CARDFAULT\":\"" + totalcardFaultTime + "\",\"MONEYDRAWFAULT\":\"" + totalmoneyDrwFaultTime
//                    + "\",\"DOWNCLOSEFAULT\":\"" + totaldownColseFaultTime + "\",\"DOWNFAULT\":\"" + totaldownFaultTime
//                    + "\",\"CARDCLOSEFAULT\":\"" + totalcardCloseFaultTime
//                    + "\"},";

//            ResultJson += "{\"CSTEMPTY\":\"" + totalcstEmptyTime + "\",\"CSTFAULT\":\"" + totalcstFaultTime + "\",\"PINFAULT\":\"" + totalpinFaultTime
//                    + "\",\"CARDCLOSEFAULT\":\"" + totalcardCloseFaultTime + "\",\"MONEYDRAWFAULT\":\"" + totalmoneyDrwFaultTime
//                    + "\",\"DOWNFAULT\":\"" + totaldownFaultTime
//                    + "\"},";
            ResultJson += "{\"CSTEMPTY\":\"" + totalcstEmptyTime + "~" + totalcstEmpty + "\",\"CSTFAULT\":\"" + totalcstFaultTime + "~" + totalcstFault + "\",\"PINFAULT\":\"" + totalpinFaultTime + "~" + totalpinFault
                    + "\",\"CARDCLOSEFAULT\":\"" + totalcardCloseFaultTime + "~" + totalcardCloseFault + "\",\"MONEYDRAWFAULT\":\"" + totalmoneyDrwFaultTime + "~" + totalmoneyDrwFault
                    + "\",\"DOWNFAULT\":\"" + totaldownFaultTime + "~" + totaldownFault
                    + "\"},";
//            for (int i = 0; i < atmDetailsEdit.size(); i++) {
//
//                try {
//                    String atm = atmDetailsEdit.get(i).split("#")[0].trim();
//                    String brcode = atmDetailsEdit.get(i).split("#")[1].trim();
//                    String location = atmDetailsEdit.get(i).split("#")[2].trim();
//                    String div = atmDetailsEdit.get(i).split("#")[3].trim();
//                    String vender = atmDetailsEdit.get(i).split("#")[4].trim();
//                    String cstEmptyStrR = atmDetailsEdit.get(i).split("#")[5].trim();
//                    String cstFaultStrR = atmDetailsEdit.get(i).split("#")[6].trim();
//                    String pinFaultStrR = atmDetailsEdit.get(i).split("#")[7].trim();
//                    String cardFaultStrR = atmDetailsEdit.get(i).split("#")[8].trim();
//                    String moneyDrwFaultStrR = atmDetailsEdit.get(i).split("#")[9].trim();
//                    String downColseFaultStrR = atmDetailsEdit.get(i).split("#")[10].trim();
//                    String downFaultStrR = atmDetailsEdit.get(i).split("#")[11].trim();
//                    String cardCloseFaultStrR = atmDetailsEdit.get(i).split("#")[12].trim();
//
//                    ResultJson += "{\"ATM\":\"" + atm + "\",\"BRCODE\":" + brcode + ",\"LOCAION\":\"" + location
//                            + "\",\"div\":\"" + div + "\",\"vender\":\"" + vender
//                            + "\",\"cstEmpty\":\"" + cstEmptyStrR + "\",\"cstFault\":\"" + cstFaultStrR + "\",\"pinFault\":\"" + pinFaultStrR
//                            + "\",\"cardFault\":\"" + cardFaultStrR + "\",\"moneyDrwFault\":\"" + moneyDrwFaultStrR + "\",\"downColseFault\":\"" + downColseFaultStrR
//                            + "\",\"downFault\":\"" + downFaultStrR
//                            + "\",\"cardCloseFault\":\"" + cardCloseFaultStrR
//                            + "\"},";
////                            + "\"},";
////                    ResultJson += "{\"ATM\":\"" + atm + "\",\"BRCODE\":" + brcode + ",\"LOCAION\":\"" + location + "\",\"cstEmpty\":\"" + cstEmptyStrR
////                            + "\",\"cstFault\":\"" + cstFaultStrR + "\",\"pinFault\":\"" + pinFaultStrR + "\",\"cardFault\":\"" + cardFaultStrR
////                            + "\",\"moneyDrwFault\":\"" + moneyDrwFaultStrR + "\",\"downColseFault\":\"" + downColseFaultStrR + "\"},";
////                    System.out.println(atmDetailsEdit.get(i).trim());
//                } catch (Exception e) {
//                    System.out.println(e.toString());
//                }
//
//            }
            ResultJson += "]";
            ResultJson = ResultJson.replaceAll("},]", "}]");
//            System.out.println(ResultJson);

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return ResultJson;

    }

    public String getDownTimeIndividual(String fromDate, String toDate, String option, int userLvl, int uBr) {

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
                        + "order by s.station,s.updated_time";
            } else {
                sql = "select s.*,A.DIV_TYPE,V.VENDER_NAME,A.BR_CODE,A.LOCATION,A.PROTOCOL from ATM_STS_DESC_SUMMARY s,ATM_INFO a,VENDER_DETAILS v "
                        + "where s.station = a.atm_name and a.vender = v.vender_id and "
                        + "s.flt_date between TO_DATE( '" + fromDate + "', 'yyyy-mm-dd') and TO_DATE('" + endDate + "', 'yyyy-mm-dd') "
                        + "and a.BR_CODE = " + uBr + " "
                        + "order by s.station,s.updated_time";
            }

            stmnt = connection.createStatement();
            rs = stmnt.executeQuery(sql);

            ArrayList<String> atmDetails = new ArrayList<String>();

            while (rs.next()) {
                String atm = rs.getString(1).trim();
                String err = rs.getString(2).trim();
                String breakTime = rs.getString(3).trim();
                String networkTime = rs.getString(4).trim();
                String div = rs.getString(6).trim();
                String vender = rs.getString(7).trim();
                String brcode = rs.getString(8).trim();
                String location = "";
                if (rs.getString(9) != null) {
                    location = rs.getString(9);
                } else {
                    location = "__________";
                }
                int protocol = Integer.parseInt(rs.getString(10).trim());

                if (div.equals("CRM") && protocol == 2) {
                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "NDCCRM" + "~" + brcode + "~" + location + "~" + networkTime + "~" + div + "~" + vender);

                } else if ((div.equals("CRM")) && protocol == 1) {
                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "DDCCRM" + "~" + brcode + "~" + location + "~" + networkTime + "~" + div + "~" + vender);

                } else if ((div.equals("CDM")) && protocol == 1) {
                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "DDCCDM" + "~" + brcode + "~" + location + "~" + networkTime + "~" + div + "~" + vender);

                } else if ((div.equals("ATM")) && protocol == 1) {
                    atmDetails.add(atm + "~" + err + "~" + breakTime + "~" + "DDCATM" + "~" + brcode + "~" + location + "~" + networkTime + "~" + div + "~" + vender);
                }
            }
            rs.close();
            stmnt.close();
            obj.releaseCon(connection);

            Date date = new Date();
            ArrayList<String> atmDetailsEdit = new ArrayList<String>();
            ReportsQuery AllDownTimeQuery = new ReportsQuery();

            String atmNext = "";
            int startindex = 0;
            int totErrors = 0;

            int totcstEmpty = 0;
            int totcstFault = 0;
            int totpinFault = 0;
            int totcardFault = 0;
            int totmoneyDrwFault = 0;
            int totdownColseFault = 0;
            int totdownFault = 0;
            int totcardCloseFault = 0;

            float totcstEmptyTime = 0;
            float totcstFaultTime = 0;
            float totpinFaultTime = 0;
            float totcardFaultTime = 0;
            float totmoneyDrwFaultTime = 0;
            float totdownColseFaultTime = 0;
            float totdownFaultTime = 0;
            float totcardCloseFaultTime = 0;

            String cstEmpty;
            String cstEmptyStartTime = "";
            String cstEmptyEndTime = "";
            String cstEmptyStartTimeFirst = "";
            String NextCstEmpty = "";
            String cstEmptyStr = "";
            int CstEmptyCount = 0;

            String cstFault;
            String cstFaultStartTime = "";
            String cstFaultEndTime = "";
            String cstFaultStartTimeFirst = "";
            String NextCstFault = "";
            String cstFaultStr = "";
            int CstFaultCount = 0;

            String pinFault;
            String pinFaultStartTime = "";
            String pinFaultEndTime = "";
            String pinFaultStartTimeFirst = "";
            String NextPinFault = "";
            String pinFaultStr = "";
            int pinFaultCount = 0;

            String cardFault;
            String cardFaultStartTime = "";
            String cardFaultEndTime = "";
            String cardFaultStartTimeFirst = "";
            String NextCardFault = "";
            String cardFaultStr = "";
            int cardFaultCount = 0;

            String moneyDrwFault;
            String moneyDrwFaultStartTime = "";
            String moneyDrwFaultEndTime = "";
            String moneyDrwFaultStartTimeFirst = "";
            String NextMoneyDrwFault = "";
            String moneyDrwFaultStr = "";
            int moneyDrwFaultCount = 0;

            String downColseFault;
            String downColseFaultStartTime = "";
            String downColseFaultEndTime = "";
            String downColseFaultStartTimeFirst = "";
            String NextDownColseFault = "";
            String downColseFaultStr = "";
            int downColseFaultCount = 0;

            String downFault;
            String downFaultStartTime = "";
            String downFaultEndTime = "";
            String downFaultStartTimeFirst = "";
            String NextDownFault = "";
            String downFaultStr = "";
            int downFaultCount = 0;

            String cardCloseFault;
            String cardCloseFaultStartTime = "";
            String cardCloseFaultEndTime = "";
            String cardCloseFaultStartTimeFirst = "";
            String NextCardCloseFault = "";
            String cardCloseFaultStr = "";
            int cardCloseFaultCount = 0;

            String closeFault;
            String closeFaultStartTime = "";
            String closeFaultEndTime = "";
            String closeFaultStartTimeFirst = "";
            String NextCloseFault = "";
            String closeFaultStr = "";
            int closeFaultCount = 0;

//            String stat;
//            String state;
            int networkTimeDiff = 5;
            int downTimeTimeDiff = 5;

            for (int i = 0; i < atmDetails.size(); i++) {
                try {
                    String atm = atmDetails.get(i).split("~")[0];
                    String errCode = atmDetails.get(i).split("~")[1];
                    String brkTime = atmDetails.get(i).split("~")[2];
                    String type = atmDetails.get(i).split("~")[3];
                    Integer brcode = Integer.parseInt(atmDetails.get(i).split("~")[4]);
                    String location = atmDetails.get(i).split("~")[5];
                    String brkTimeNetwork = atmDetails.get(i).split("~")[6];
                    String div = atmDetails.get(i).split("~")[7];
                    String vender = atmDetails.get(i).split("~")[8];

                    cstEmpty = errCode.substring(0, 7).trim();
                    cstFault = errCode.substring(0, 7).trim();
                    downFault = errCode.split(" ")[4];
                    closeFault = errCode.split(" ")[5];
                    pinFault = errCode.split(" ")[8];
                    moneyDrwFault = errCode.split(" ")[9];
                    cardFault = errCode.split(" ")[10];

                    downColseFault = downFault + " " + closeFault;

                    if (i != atmDetails.size() - 1) {
                        atmNext = atmDetails.get(i + 1).split("~")[0];
                        NextCstEmpty = atmDetails.get(i + 1).split("~")[1].substring(0, 7).trim();
                        NextCstFault = atmDetails.get(i + 1).split("~")[1].substring(0, 7).trim();
                        NextPinFault = atmDetails.get(i + 1).split("~")[1].split(" ")[8];
                        NextCardFault = atmDetails.get(i + 1).split("~")[1].split(" ")[10];
                        NextMoneyDrwFault = atmDetails.get(i + 1).split("~")[1].split(" ")[9];
                        NextDownColseFault = atmDetails.get(i + 1).split("~")[1].split(" ")[4] + " " + atmDetails.get(i + 1).split("~")[1].split(" ")[5]; // down or closed
                        NextDownFault = atmDetails.get(i + 1).split("~")[1].split(" ")[4]; // down
                        NextCardCloseFault = atmDetails.get(i + 1).split("~")[1].split(" ")[10] + " " + atmDetails.get(i + 1).split("~")[1].split(" ")[5]; // card fault and closed
                        NextCloseFault = atmDetails.get(i + 1).split("~")[1].split(" ")[5]; // down
                    } else if (i == atmDetails.size() - 1) {
                        atmNext = "";
                        NextCstEmpty = "";
                        NextCstFault = "";
                        NextPinFault = "";
                        NextCardFault = "";
                        NextMoneyDrwFault = "";
                        NextDownColseFault = "";
                        NextDownFault = "";
                        NextCardCloseFault = "";
                        NextCloseFault = "";
                    }

                    //Start Time
                    if (startindex == 0) {

                        //All Cassette Empty
                        if (option.trim().toLowerCase().equals("cstempty")) {
                            if ((cstEmpty.equals("2 2 2 2") && !type.equals("NDCCRM")) || (cstEmpty.equals("3 3 3 3") && type.equals("NDCCRM"))) {
//                                cstEmptyStartTime = fromDate + " 00:00:00";
                                cstEmptyStartTime = brkTime;
                            } else {
                                cstEmptyStartTime = brkTime;
                            }
                        }

                        //All Cassette Fault
                        if (option.trim().toLowerCase().equals("cstfault")) {
                            if ((cstFault.equals("9 9 9 9") && !type.equals("NDCCRM")) || (cstFault.equals("0 0 0 0") && type.equals("NDCCRM"))) {
//                                cstFaultStartTime = fromDate + " 00:00:00";
                                cstFaultStartTime = brkTime;
                            } else {
                                cstFaultStartTime = brkTime;
                            }
                        }

                        //PinPad Fault
                        if (option.trim().toLowerCase().equals("pinfault")) {
                            if ((pinFault.equals("1") && !type.equals("NDCCRM")) || (pinFault.equals("4") && type.equals("NDCCRM"))) {
//                                pinFaultStartTime = fromDate + " 00:00:00";
                                pinFaultStartTime = brkTime;
                            } else {
                                pinFaultStartTime = brkTime;
                            }
                        }

                        //ModeyDrawer Fault
                        if (option.trim().toLowerCase().equals("currencyfault")) {
                            if (((moneyDrwFault.equals("1") || moneyDrwFault.equals("2")) && type.equals("DDCATM"))
                                    || (!(moneyDrwFault.equals("0") || moneyDrwFault.equals("1")) && (type.equals("DDCCDM") || type.equals("DDCCRM")))
                                    || ((moneyDrwFault.equals("140") || moneyDrwFault.equals("440")) && type.equals("NDCCRM"))) {
//                                moneyDrwFaultStartTime = fromDate + " 00:00:00";
                                moneyDrwFaultStartTime = brkTime;
                            } else {
                                moneyDrwFaultStartTime = brkTime;
                            }
                        }

                        if (option.trim().toLowerCase().equals("downfault")) {
                            //Down Fault
                            if (downFault.equals("0")) {
//                                downFaultStartTime = fromDate + " 00:00:00";
                                downFaultStartTime = brkTimeNetwork;
                            } else {
                                downFaultStartTime = brkTimeNetwork;
                            }
                        }

                        //CardReader Fault + Closed
                        if (option.trim().toLowerCase().equals("cardfault")) {
                            if (((!cardFault.equals("0") && !type.equals("NDCCRM")) || (!cardFault.equals("0") && type.equals("NDCCRM"))) && closeFault.equals("0")) {
//                                cardCloseFaultStartTime = fromDate + " 00:00:00";
                                cardCloseFaultStartTime = brkTimeNetwork;
                            } else {
                                cardCloseFaultStartTime = brkTimeNetwork; //use the networktime because brktime has the card reader fault time
                            }
                        }

                    } else {
                        if (option.trim().toLowerCase().equals("cstempty")) {
                            cstEmptyStartTime = brkTime;
                        }
                        if (option.trim().toLowerCase().equals("cstfault")) {
                            cstFaultStartTime = brkTime;
                        }
                        if (option.trim().toLowerCase().equals("pinfault")) {
                            pinFaultStartTime = brkTime;
                        }
                        if (option.trim().toLowerCase().equals("currencyfault")) {
                            moneyDrwFaultStartTime = brkTime;
                        }
                        if (option.trim().toLowerCase().equals("downfault")) {
                            downFaultStartTime = brkTimeNetwork;
                        }
                        if (option.trim().toLowerCase().equals("cardfault")) {
                            cardCloseFaultStartTime = brkTimeNetwork; //use the networktime because brktime has the card reader fault time
                        }

                    }

                    //End Time
                    if (!atm.equals(atmNext)) {
                        //cassette empty
                        if (option.trim().toLowerCase().equals("cstempty")) {
                            if ((cstEmpty.equals("2 2 2 2") && !type.equals("NDCCRM")) || (cstEmpty.equals("3 3 3 3") && type.equals("NDCCRM"))) {
                                if (sdf.format(date).equals(toDate)) {
                                    cstEmptyEndTime = toDate + " " + sdf2.format(date);
                                } else {
                                    cstEmptyEndTime = toDate + " 24:00:00";
                                }

                            } else {
                                cstEmptyEndTime = brkTime;
                            }
                        }

                        //cassete fault
                        if (option.trim().toLowerCase().equals("cstfault")) {
                            if ((cstFault.equals("9 9 9 9") && !type.equals("NDCCRM")) || (cstFault.equals("0 0 0 0") && type.equals("NDCCRM"))) {
                                if (sdf.format(date).equals(toDate)) {
                                    cstFaultEndTime = toDate + " " + sdf2.format(date);
                                } else {
                                    cstFaultEndTime = toDate + " 24:00:00";
                                }
                            } else {
                                cstFaultEndTime = brkTime;
                            }
                        }
//                        
                        //PinPad fault
                        if (option.trim().toLowerCase().equals("pinfault")) {
                            if ((pinFault.equals("1") && !type.equals("NDCCRM")) || (pinFault.equals("4") && type.equals("NDCCRM"))) {
                                if (sdf.format(date).equals(toDate)) {
                                    pinFaultEndTime = toDate + " " + sdf2.format(date);
                                } else {
                                    pinFaultEndTime = toDate + " 24:00:00";
                                }
                            } else {
                                pinFaultEndTime = brkTime;
                            }
                        }
//                     
                        if (option.trim().toLowerCase().equals("currencyfault")) {
                            //MoneyDrawer fault
                            if (((moneyDrwFault.equals("1") || moneyDrwFault.equals("2")) && type.equals("DDCATM"))
                                    || (!(moneyDrwFault.equals("0") || moneyDrwFault.equals("1")) && (type.equals("DDCCDM") || type.equals("DDCCRM")))
                                    || ((moneyDrwFault.equals("140") || moneyDrwFault.equals("440")) && type.equals("NDCCRM"))) {
                                if (sdf.format(date).equals(toDate)) {
                                    moneyDrwFaultEndTime = toDate + " " + sdf2.format(date);
                                } else {
                                    moneyDrwFaultEndTime = toDate + " 24:00:00";
                                }
                            } else {
                                moneyDrwFaultEndTime = brkTime;
                            }
                        }

                        //Down fault
                        if (option.trim().toLowerCase().equals("downfault")) {
                            if (downFault.equals("0")) {
                                if (sdf.format(date).equals(toDate)) {
                                    downFaultEndTime = toDate + " " + sdf2.format(date);
                                } else {
                                    downFaultEndTime = toDate + " 24:00:00";
                                }
                            } else {
                                downFaultEndTime = brkTimeNetwork;
                            }
                        }

                        //CardReader fault + closed
                        if (option.trim().toLowerCase().equals("cardfault")) {
                            if (((!cardFault.equals("0") && !type.equals("NDCCRM")) || (!cardFault.equals("0") && type.equals("NDCCRM"))) && closeFault.equals("0")) {
                                if (sdf.format(date).equals(toDate)) {
                                    cardCloseFaultEndTime = toDate + " " + sdf2.format(date);
                                } else {
                                    cardCloseFaultEndTime = toDate + " 24:00:00";
                                }
                            } else {
                                cardCloseFaultEndTime = brkTimeNetwork; //use the networktime because brktime has the card reader fault time
                            }
                        }

                    } else {
                        if (i != atmDetails.size() - 1) {
                            if (option.trim().toLowerCase().equals("cstempty")) {
                                cstEmptyEndTime = atmDetails.get(i + 1).split("~")[2];
                            }
                            if (option.trim().toLowerCase().equals("cstfault")) {
                                cstFaultEndTime = atmDetails.get(i + 1).split("~")[2];
                            }
                            if (option.trim().toLowerCase().equals("pinfault")) {
                                pinFaultEndTime = atmDetails.get(i + 1).split("~")[2];
                            }
                            if (option.trim().toLowerCase().equals("currencyfault")) {
                                moneyDrwFaultEndTime = atmDetails.get(i + 1).split("~")[2];
                            }
                            if (option.trim().toLowerCase().equals("downfault")) {
                                downFaultEndTime = atmDetails.get(i + 1).split("~")[6];
                            }
                            if (option.trim().toLowerCase().equals("cardfault")) {
                                cardCloseFaultEndTime = atmDetails.get(i + 1).split("~")[6];
                            }//use the networktime because brktime has the card reader fault time
                        } else {
                            if (option.trim().toLowerCase().equals("cstempty")) {
                                cstEmptyEndTime = brkTime;
                            }
                            if (option.trim().toLowerCase().equals("cstfault")) {
                                cstFaultEndTime = brkTime;
                            }
                            if (option.trim().toLowerCase().equals("pinfault")) {
                                pinFaultEndTime = brkTime;
                            }
                            if (option.trim().toLowerCase().equals("currencyfault")) {
                                moneyDrwFaultEndTime = brkTime;
                            }
                            if (option.trim().toLowerCase().equals("downfault")) {
                                downFaultEndTime = brkTimeNetwork;
                            }
                            if (option.trim().toLowerCase().equals("cardfault")) {
                                cardCloseFaultEndTime = brkTimeNetwork;
                            }//use the networktime because brktime has the card reader fault time
                        }
                    }

                    //Calculate
                    //Not HITACHI CRM
                    if (!type.equals("NDCCRM")) {
                        //cassette Empty
                        if (option.trim().toLowerCase().equals("cstempty")) {
                            if (cstEmpty.equals("2 2 2 2") || cstEmpty.equals("3 3 3 3")) {
                                if (cstEmpty.equals(NextCstEmpty)) {
                                    CstEmptyCount++;
                                    if (CstEmptyCount == 1) {
                                        cstEmptyStartTimeFirst = cstEmptyStartTime;
                                    }
                                } else {
                                    if (CstEmptyCount == 0) {
                                        cstEmptyStartTimeFirst = cstEmptyStartTime;
                                    }

                                    if ((AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60) > downTimeTimeDiff) {
                                        cstEmptyStr += cstEmptyStartTimeFirst.substring(2, cstEmptyStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                                + cstEmptyEndTime.substring(2, cstEmptyEndTime.length() - 2).replaceAll("-", "/") + "="
                                                + df.format(AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60) + "(min)~";
                                        totErrors++;
                                        totcstEmpty++;
                                        totcstEmptyTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60));
                                    }

                                    CstEmptyCount = 0;
                                    cstEmptyStartTimeFirst = "N/A";

                                }
                            }
                        }

                        //cassette Fault
                        if (option.trim().toLowerCase().equals("cstfault")) {
                            if (cstFault.equals("9 9 9 9")) {
                                if (cstFault.equals(NextCstFault)) {
                                    CstFaultCount++;
                                    if (CstFaultCount == 1) {
                                        cstFaultStartTimeFirst = cstFaultStartTime;
                                    }
                                } else {
                                    if (CstFaultCount == 0) {
                                        cstFaultStartTimeFirst = cstFaultStartTime;
                                    }

                                    if ((AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60) > downTimeTimeDiff) {
                                        cstFaultStr += cstFaultStartTimeFirst.substring(2, cstFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                                + cstFaultEndTime.substring(2, cstFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                                + df.format(AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60) + "(min)~";
                                        totErrors++;
                                        totcstFault++;
                                        totcstFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60));
                                    }

                                    CstFaultCount = 0;
                                    cstFaultStartTimeFirst = "N/A";
                                }
                            }
                        }

                        //PinPad Fault
                        if (option.trim().toLowerCase().equals("pinfault")) {
                            if (pinFault.equals("1")) {
                                if (pinFault.equals(NextPinFault)) {
                                    pinFaultCount++;
                                    if (pinFaultCount == 1) {
                                        pinFaultStartTimeFirst = pinFaultStartTime;
                                    }
                                } else {
                                    if (pinFaultCount == 0) {
                                        pinFaultStartTimeFirst = pinFaultStartTime;
                                    }
                                    if ((AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60) > downTimeTimeDiff) {
                                        pinFaultStr += pinFaultStartTimeFirst.substring(2, pinFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                                + pinFaultEndTime.substring(2, pinFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                                + df.format(AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60) + "(min)~";
                                        totErrors++;
                                        totpinFault++;
                                        totpinFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60));
                                    }

                                    pinFaultCount = 0;
                                    pinFaultStartTimeFirst = "N/A";
                                }
                            }
                        }
                        if (option.trim().toLowerCase().equals("currencyfault")) {
                            //MoneyDrawer Fault
                            if (((moneyDrwFault.equals("1") || moneyDrwFault.equals("2")) && type.equals("DDCATM"))
                                    || (!(moneyDrwFault.equals("0") || moneyDrwFault.equals("1")) && (type.equals("DDCCDM") || type.equals("DDCCRM")))) {
                                if (moneyDrwFault.equals(NextMoneyDrwFault)) {
                                    moneyDrwFaultCount++;
                                    if (moneyDrwFaultCount == 1) {
                                        moneyDrwFaultStartTimeFirst = moneyDrwFaultStartTime;
                                    }
                                } else {
                                    if (moneyDrwFaultCount == 0) {
                                        moneyDrwFaultStartTimeFirst = moneyDrwFaultStartTime;
                                    }

                                    if ((AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60) > downTimeTimeDiff) {
                                        moneyDrwFaultStr += moneyDrwFaultStartTimeFirst.substring(2, moneyDrwFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                                + moneyDrwFaultEndTime.substring(2, moneyDrwFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                                + df.format(AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60) + "(min)~";
                                        totErrors++;
                                        totmoneyDrwFault++;
                                        totmoneyDrwFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60));
                                    }

                                    moneyDrwFaultCount = 0;
                                    moneyDrwFaultStartTimeFirst = "N/A";
                                }
                            }
                        }

                        if (option.trim().toLowerCase().equals("downfault")) {
                            //Down Fault
                            if (downFault.equals("0")) {
                                if (downFault.equals(NextDownFault)) {
                                    downFaultCount++;
                                    if (downFaultCount == 1) {
                                        downFaultStartTimeFirst = downFaultStartTime;
                                    }
                                } else {
                                    if (downFaultCount == 0) {
                                        downFaultStartTimeFirst = downFaultStartTime;
                                    }

                                    //Taking only morethan 20 min
                                    if ((AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60) > downTimeTimeDiff) {
                                        downFaultStr += downFaultStartTimeFirst.substring(2, downFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                                + downFaultEndTime.substring(2, downFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                                + df.format(AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60) + "(min)~";
                                        totErrors++;
                                        totdownFault++;
                                        totdownFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60));

                                    }
                                    downFaultCount = 0;
                                    downFaultStartTimeFirst = "N/A";

                                }
                            }
                        }

                        //CardReader Fault + close
                        if (option.trim().toLowerCase().equals("cardfault")) {
                            if (!cardFault.equals("0") && closeFault.equals("0")) {
                                if (cardFault.equals(NextCardFault) && closeFault.equals(NextCloseFault)) {
                                    cardCloseFaultCount++;
                                    if (cardCloseFaultCount == 1) {
                                        cardCloseFaultStartTimeFirst = cardCloseFaultStartTime;
                                    }
                                } else {
                                    if (cardCloseFaultCount == 0) {
                                        cardCloseFaultStartTimeFirst = cardCloseFaultStartTime;
                                    }
                                    if ((AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60) > downTimeTimeDiff) {
                                        cardCloseFaultStr += cardCloseFaultStartTimeFirst.substring(2, cardCloseFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                                + cardCloseFaultEndTime.substring(2, cardCloseFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                                + df.format(AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60) + "(min)~";
                                        totErrors++;
                                        totcardCloseFault++;
                                        totcardCloseFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60));
                                    }

                                    cardCloseFaultCount = 0;
                                    cardCloseFaultStartTimeFirst = "N/A";
                                }
                            }
                        }

                    } //HITACHI CRM
                    else if (type.equals("NDCCRM")) {
                        //cassette Empty
                        if (option.trim().toLowerCase().equals("cstempty")) {
                            if (cstEmpty.equals("3 3 3 3")) {
                                if (cstEmpty.equals(NextCstEmpty)) {
                                    CstEmptyCount++;
                                    if (CstEmptyCount == 1) {
                                        cstEmptyStartTimeFirst = cstEmptyStartTime;
                                    }
                                } else {
                                    if (CstEmptyCount == 0) {
                                        cstEmptyStartTimeFirst = cstEmptyStartTime;
                                    }
                                    if ((AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60) > downTimeTimeDiff) {
                                        cstEmptyStr += cstEmptyStartTimeFirst.substring(2, cstEmptyStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                                + cstEmptyEndTime.substring(2, cstEmptyEndTime.length() - 2).replaceAll("-", "/") + "="
                                                + df.format(AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60) + "(min)~";
                                        totErrors++;
                                        totcstEmpty++;
                                        totcstEmptyTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cstEmptyStartTimeFirst, cstEmptyEndTime) * 60));
                                    }

                                    CstEmptyCount = 0;
                                    cstEmptyStartTimeFirst = "N/A";
                                }
                            }
                        }

                        //cassette Fault
                        if (option.trim().toLowerCase().equals("cstfault")) {
                            if (cstFault.equals("0 0 0 0")) {
                                if (cstFault.equals(NextCstFault)) {
                                    CstFaultCount++;
                                    if (CstFaultCount == 1) {
                                        cstFaultStartTimeFirst = cstFaultStartTime;
                                    }
                                } else {
                                    if (CstFaultCount == 0) {
                                        cstFaultStartTimeFirst = cstFaultStartTime;
                                    }
                                    if ((AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60) > downTimeTimeDiff) {
                                        cstFaultStr += cstFaultStartTimeFirst.substring(2, cstFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                                + cstFaultEndTime.substring(2, cstFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                                + df.format(AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60) + "(min)~";
                                        totErrors++;
                                        totcstFault++;
                                        totcstFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cstFaultStartTimeFirst, cstFaultEndTime) * 60));
                                    }

                                    CstFaultCount = 0;
                                    cstFaultStartTimeFirst = "N/A";
                                }
                            }
                        }

                        //PinPad Fault
                        if (option.trim().toLowerCase().equals("pinfault")) {
                            if (pinFault.equals("4")) {
                                if (pinFault.equals(NextPinFault)) {
                                    pinFaultCount++;
                                    if (pinFaultCount == 1) {
                                        pinFaultStartTimeFirst = pinFaultStartTime;
                                    }
                                } else {
                                    if (pinFaultCount == 0) {
                                        pinFaultStartTimeFirst = pinFaultStartTime;
                                    }

                                    if ((AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60) > downTimeTimeDiff) {
                                        pinFaultStr += pinFaultStartTimeFirst.substring(2, pinFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                                + pinFaultEndTime.substring(2, pinFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                                + df.format(AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60) + "(min)~";
                                        totErrors++;
                                        totpinFault++;
                                        totpinFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(pinFaultStartTimeFirst, pinFaultEndTime) * 60));
                                    }

                                    pinFaultCount = 0;
                                    pinFaultStartTimeFirst = "N/A";
                                }
                            }
                        }
//                       
                        //MoneyDrawer Fault
                        if (option.trim().toLowerCase().equals("currencyfault")) {
                            if ((moneyDrwFault.equals("140") || moneyDrwFault.equals("440"))) {
                                if (moneyDrwFault.equals(NextMoneyDrwFault)) {
                                    moneyDrwFaultCount++;
                                    if (moneyDrwFaultCount == 1) {
                                        moneyDrwFaultStartTimeFirst = moneyDrwFaultStartTime;
                                    }
                                } else {
                                    if (moneyDrwFaultCount == 0) {
                                        moneyDrwFaultStartTimeFirst = moneyDrwFaultStartTime;
                                    }
                                    if ((AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60) > downTimeTimeDiff) {
                                        moneyDrwFaultStr += moneyDrwFaultStartTimeFirst.substring(2, moneyDrwFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                                + moneyDrwFaultEndTime.substring(2, moneyDrwFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                                + df.format(AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60) + "(min)~";
                                        totErrors++;
                                        totmoneyDrwFault++;
                                        totmoneyDrwFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(moneyDrwFaultStartTimeFirst, moneyDrwFaultEndTime) * 60));
                                    }

                                    moneyDrwFaultCount = 0;
                                    moneyDrwFaultStartTimeFirst = "N/A";
                                }
                            }
                        }

                        //Down Fault
                        if (option.trim().toLowerCase().equals("downfault")) {
                            if (downFault.equals("0")) {
                                if (NextDownFault.equals("0")) {
                                    downFaultCount++;
                                    if (downFaultCount == 1) {
                                        downFaultStartTimeFirst = downFaultStartTime;
                                    }
                                } else {
                                    if (downFaultCount == 0) {
                                        downFaultStartTimeFirst = downFaultStartTime;
                                    }

                                    //Taking only morethan 20 min
                                    if ((AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60) > networkTimeDiff) {
                                        downFaultStr += downFaultStartTimeFirst.substring(2, downFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                                + downFaultEndTime.substring(2, downFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                                + df.format(AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60) + "(min)~";
                                        totErrors++;
                                        totdownFault++;
                                        totdownFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(downFaultStartTimeFirst, downFaultEndTime) * 60));

                                    }
                                    downFaultCount = 0;
                                    downFaultStartTimeFirst = "N/A";

                                }
                            }
                        }

                        //CardReader Fault + close
                        if (option.trim().toLowerCase().equals("cardfault")) {
                            if (!cardFault.equals("0") && closeFault.equals("0")) {
                                if (cardFault.equals(NextCardFault) && closeFault.equals("0")) {
                                    cardCloseFaultCount++;
                                    if (cardCloseFaultCount == 1) {
                                        cardCloseFaultStartTimeFirst = cardCloseFaultStartTime;
                                    }
                                } else {
                                    if (cardCloseFaultCount == 0) {
                                        cardCloseFaultStartTimeFirst = cardCloseFaultStartTime;
                                    }
                                    if ((AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60) > downTimeTimeDiff) {
                                        cardCloseFaultStr += cardCloseFaultStartTimeFirst.substring(2, cardCloseFaultStartTimeFirst.length() - 2).replaceAll("-", "/") + " - "
                                                + cardCloseFaultEndTime.substring(2, cardCloseFaultEndTime.length() - 2).replaceAll("-", "/") + "="
                                                + df.format(AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60) + "(min)~";
                                        totErrors++;
                                        totcardCloseFault++;
                                        totcardCloseFaultTime += Float.parseFloat(df.format(AllDownTimeQuery.getHourDifference(cardCloseFaultStartTimeFirst, cardCloseFaultEndTime) * 60));
                                    }

                                    cardCloseFaultCount = 0;
                                    cardCloseFaultStartTimeFirst = "N/A";

                                }
                            }

                        }
                    }

                    //********************************************************
                    // If atm change Reset all values and add to array else assign last value 
                    if (!atm.equals(atmNext)) {

                        if (totcstEmpty == 0) {
                            cstEmptyStr = "N/A";
                        }
                        if (totcstFault == 0) {
                            cstFaultStr = "N/A";
                        }
                        if (totpinFault == 0) {
                            pinFaultStr = "N/A";
                        }
                        if (totmoneyDrwFault == 0) {
                            moneyDrwFaultStr = "N/A";
                        }
                        if (totdownFault == 0) {
                            downFaultStr = "N/A";
                        }
                        if (totcardCloseFault == 0) {
                            cardCloseFaultStr = "N/A";
                        }

                        if (option.trim().toLowerCase().equals("cstempty") && totcstEmptyTime > 0) {
                            atmDetailsEdit.add(atm + "#" + brcode + "#" + location + "#" + div + "#" + vender
                                    + "#" + cstEmptyStr + totcstEmptyTime);
                        }
                        if (option.trim().toLowerCase().equals("cstfault") && totcstFaultTime > 0) {
                            atmDetailsEdit.add(atm + "#" + brcode + "#" + location + "#" + div + "#" + vender
                                    + "#" + cstFaultStr + totcstFaultTime);
                        }
                        if (option.trim().toLowerCase().equals("pinfault") && totpinFaultTime > 0) {
                            atmDetailsEdit.add(atm + "#" + brcode + "#" + location + "#" + div + "#" + vender
                                    + "#" + pinFaultStr + totpinFaultTime);
                        }
                        if (option.trim().toLowerCase().equals("currencyfault") && totmoneyDrwFaultTime > 0) {
                            atmDetailsEdit.add(atm + "#" + brcode + "#" + location + "#" + div + "#" + vender
                                    + "#" + moneyDrwFaultStr + totmoneyDrwFaultTime);
                        }
                        if (option.trim().toLowerCase().equals("downfault") && totdownFaultTime > 0) {
                            atmDetailsEdit.add(atm + "#" + brcode + "#" + location + "#" + div + "#" + vender
                                    + "#" + downFaultStr + totdownFaultTime);
                        }
                        if (option.trim().toLowerCase().equals("cardfault") && totcardCloseFaultTime > 0) {
                            atmDetailsEdit.add(atm + "#" + brcode + "#" + location + "#" + div + "#" + vender
                                    + "#" + cardCloseFaultStr + totcardCloseFaultTime);
                        }

                        startindex = 0;
                        totErrors = 0;

                        //cassette Empty
                        NextCstEmpty = "";
                        CstEmptyCount = 0;
                        cstEmptyStartTimeFirst = "N/A";
                        cstEmptyStr = "";
                        totcstEmpty = 0;
                        totcstEmptyTime = 0;

                        //cassette Fault
                        NextCstFault = "";
                        CstFaultCount = 0;
                        cstFaultStartTimeFirst = "N/A";
                        cstFaultStr = "";
                        totcstFault = 0;
                        totcstFaultTime = 0;
//                        
                        //PinPad Fault
                        NextPinFault = "";
                        pinFaultCount = 0;
                        pinFaultStartTimeFirst = "N/A";
                        pinFaultStr = "";
                        totpinFault = 0;
                        totpinFaultTime = 0;
//                        
                        //MoneyDrawer Fault
                        NextMoneyDrwFault = "";
                        moneyDrwFaultCount = 0;
                        moneyDrwFaultStartTimeFirst = "N/A";
                        moneyDrwFaultStr = "";
                        totmoneyDrwFault = 0;
                        totmoneyDrwFaultTime = 0;

                        //Down Fault
                        NextDownFault = "";
                        downFaultCount = 0;
                        downFaultStartTimeFirst = "N/A";
                        downFaultStr = "";
                        totdownFault = 0;
                        totdownFaultTime = 0;

                        //Card Fault + Close
                        NextCardCloseFault = "";
                        cardCloseFaultCount = 0;
                        cardCloseFaultStartTimeFirst = "N/A";
                        cardCloseFaultStr = "";
                        totcardCloseFault = 0;
                        totcardCloseFaultTime = 0;

                    } else if (i == atmDetails.size() - 1) {
                        if (totcstEmpty == 0) {
                            cstEmptyStr = "N/A";
                        }
                        if (totcstFault == 0) {
                            cstFaultStr = "N/A";
                        }
                        if (totpinFault == 0) {
                            pinFaultStr = "N/A";
                        }
                        if (totcardFault == 0) {
                            cardFaultStr = "N/A";
                        }
                        if (totmoneyDrwFault == 0) {
                            moneyDrwFaultStr = "N/A";
                        }
                        if (totdownColseFault == 0) {
                            downColseFaultStr = "N/A";
                        }
                        if (totdownFault == 0) {
                            downFaultStr = "N/A";
                        }

                        if (totcardCloseFault == 0) {
                            cardCloseFaultStr = "N/A";
                        }

                        if (option.trim().toLowerCase().equals("cstempty") && totcstEmptyTime > 0) {
                            atmDetailsEdit.add(atm + "#" + brcode + "#" + location + "#" + div + "#" + vender
                                    + "#" + cstEmptyStr + totcstEmptyTime);
                        }
                        if (option.trim().toLowerCase().equals("cstfault") && totcstFaultTime > 0) {
                            atmDetailsEdit.add(atm + "#" + brcode + "#" + location + "#" + div + "#" + vender
                                    + "#" + cstFaultStr + totcstFaultTime);
                        }
                        if (option.trim().toLowerCase().equals("pinfault") && totpinFaultTime > 0) {
                            atmDetailsEdit.add(atm + "#" + brcode + "#" + location + "#" + div + "#" + vender
                                    + "#" + pinFaultStr + totpinFaultTime);
                        }
                        if (option.trim().toLowerCase().equals("currencyfault") && totmoneyDrwFaultTime > 0) {
                            atmDetailsEdit.add(atm + "#" + brcode + "#" + location + "#" + div + "#" + vender
                                    + "#" + moneyDrwFaultStr + totmoneyDrwFaultTime);
                        }
                        if (option.trim().toLowerCase().equals("downfault") && totdownFaultTime > 0) {
                            atmDetailsEdit.add(atm + "#" + brcode + "#" + location + "#" + div + "#" + vender
                                    + "#" + downFaultStr + totdownFaultTime);
                        }
                        if (option.trim().toLowerCase().equals("cardfault") && totcardCloseFaultTime > 0) {
                            atmDetailsEdit.add(atm + "#" + brcode + "#" + location + "#" + div + "#" + vender
                                    + "#" + cardCloseFaultStr + totcardCloseFaultTime);
                        }

                    } else {
                        startindex++;
                    }

                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
//            printAll(atmDetailsEdit);
            ResultJson = "[";
            for (int i = 0; i < atmDetailsEdit.size(); i++) {

                try {
                    String atm = atmDetailsEdit.get(i).split("#")[0].trim();
                    String brcode = atmDetailsEdit.get(i).split("#")[1].trim();
                    String location = atmDetailsEdit.get(i).split("#")[2].trim();
                    String div = atmDetailsEdit.get(i).split("#")[3].trim();
                    String vender = atmDetailsEdit.get(i).split("#")[4].trim();
                    String err = atmDetailsEdit.get(i).split("#")[5].trim();

                    ResultJson += "{\"ATM\":\"" + atm + "\",\"BRCODE\":" + brcode + ",\"LOCAION\":\"" + location
                            + "\",\"div\":\"" + div + "\",\"vender\":\"" + vender
                            + "\",\"err\":\"" + err
                            + "\"},";
//                            + "\"},";
//                    ResultJson += "{\"ATM\":\"" + atm + "\",\"BRCODE\":" + brcode + ",\"LOCAION\":\"" + location + "\",\"cstEmpty\":\"" + cstEmptyStrR
//                            + "\",\"cstFault\":\"" + cstFaultStrR + "\",\"pinFault\":\"" + pinFaultStrR + "\",\"cardFault\":\"" + cardFaultStrR
//                            + "\",\"moneyDrwFault\":\"" + moneyDrwFaultStrR + "\",\"downColseFault\":\"" + downColseFaultStrR + "\"},";
//                    System.out.println(atmDetailsEdit.get(i).trim());
                } catch (Exception e) {
                    System.out.println(e.toString());
                }

            }
            ResultJson += "]";
            ResultJson = ResultJson.replaceAll("},]", "}]");
//            System.out.println(ResultJson);

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

    public String getFaultReportUsers() {

        String ResultJson = "\"\"";

        try {
            String sql = "";
            sql = "select distinct(CREATED_USER),USER_NAME,FIRST_NAME from ATM_FAULT_ENTRY  f,USER_DETAILS u where F.CREATED_USER = u.USER_ID";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String getATMFaultReport(String qry) {

        String ResultJson = "\"\"";

        try {
            String sql = "";

            sql = "select f.*,u.USER_NAME,u.FIRST_NAME from ATM_FAULT_ENTRY  f,USER_DETAILS u "
                    + qry + " AND F.CREATED_USER = u.USER_ID order by fault_entry_date desc";
            DBConnect obj = new DBConnect();
            ResultJson = obj.getDBData(sql);

        } catch (Exception e) {
            System.out.println("Error when executing querry.\n" + e);
        }

        return ResultJson;
    }

    public String getCashLow(int cashlimit) {

        String ResultJson = "\"\"";

        try {

            DBConnect obj = new DBConnect();
            connection = obj.dbConnect_Local();

            String sql = "select atm,billval,endcash,br_code,location from ATM_CASHPO c, "
                    + "ATM_INFO a where C.ATM = a.atm_name and a.status = 1 order by br_code";

            stmnt = connection.createStatement();
            rs = stmnt.executeQuery(sql);

            ArrayList<String> atmDetails = new ArrayList<String>();

            while (rs.next()) {
                String atm = rs.getString(1).trim();
                String billVal = rs.getString(2).trim();
                String endcash = rs.getString(3).trim();
                int brcode = Integer.parseInt(rs.getString(4).trim());
                String location = rs.getString(5).trim();

                int cst1 = Integer.parseInt(endcash.split("~")[0]);
                int cst2 = Integer.parseInt(endcash.split("~")[1]);
                int cst3 = Integer.parseInt(endcash.split("~")[2]);
                int cst4 = Integer.parseInt(endcash.split("~")[3]);

                int denom1 = Integer.parseInt(billVal.split("~")[0]);
                int denom2 = Integer.parseInt(billVal.split("~")[1]);
                int denom3 = Integer.parseInt(billVal.split("~")[2]);
                int denom4 = Integer.parseInt(billVal.split("~")[3]);

                int totalCash = (denom1 * cst1) + (denom2 * cst2) + (denom3 * cst3) + (denom4 * cst4);

                if (totalCash < cashlimit) {
                    atmDetails.add(atm + "#" + billVal + "#" + endcash + "#" + brcode + "#" + location);
                }
            }

            rs.close();
            stmnt.close();
            obj.releaseCon(connection);

            ResultJson = "[";
            for (int i = 0; i < atmDetails.size(); i++) {

                try {
                    String atm = atmDetails.get(i).split("#")[0].trim();
                    String billval = atmDetails.get(i).split("#")[1].trim();
                    String endcash = atmDetails.get(i).split("#")[2].trim();
                    String brcode = atmDetails.get(i).split("#")[3].trim();
                    String location = atmDetails.get(i).split("#")[4].trim();

                    ResultJson += "{\"ATM\":\"" + atm + "\",\"BRCODE\":" + brcode + ",\"LOCAION\":\"" + location
                            + "\",\"BILL\":\"" + billval + "\",\"END\":\"" + endcash
                            + "\"},";

                } catch (Exception e) {
                    System.out.println(e.toString());
                }

            }
            ResultJson += "]";
            ResultJson = ResultJson.replaceAll("},]", "}]");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return ResultJson;

    }
}
