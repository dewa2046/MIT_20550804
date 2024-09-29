/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package Services;

import BackEnd.SetOutPut;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Administrator
 */
@Path("services")
public class ServicesResource {

    SetOutPut sp = new SetOutPut();

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetATMModels;

    @GET
    @Path("getATMModels/")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetATMModels() {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {

            beforeResp = System.currentTimeMillis();

            System.out.print("Get ATM Models - " + reqGetATMModels.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");
            beforeResp = System.currentTimeMillis();
            Result = "{\"data\":" + sqlOp.getATMModels() + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetATMVenders;

    @GET
    @Path("getATMVenders/")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetATMVenders() {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {

            beforeResp = System.currentTimeMillis();

            System.out.print("Get ATM Venders - " + reqGetATMModels.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");
            beforeResp = System.currentTimeMillis();
            Result = "{\"data\":" + sqlOp.getATMVenders() + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqAddUpdateATMDetails;

    @POST
    @Path("addUpdateATMDetails/")
    @Produces(MediaType.APPLICATION_JSON)
    public String AddUpdateATMDetails(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);

            beforeResp = System.currentTimeMillis();

            System.out.print("Add or Update ATM details - " + reqAddUpdateATMDetails.getRemoteAddr()
                    + "(" + jsonObj.getString("atmname") + ")"
                    + "(" + jsonObj.getString("uid") + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            int result = sqlOp.addUpdateATMDetails(jsonObj);

            String ResultJson = "\"\"";

            if (result > 0) {
                Result = "{\"data\":" + "[{\"Result\":" + result + "}]" + "}";
            } else {
                Result = ResultJson;
            }

            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetATMDetails;

    @POST
    @Path("getATMDetails/")
    @Produces(MediaType.APPLICATION_JSON)

    public String GetATMDetails(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            String atm = jsonObj.getString("ATM").trim();
            beforeResp = System.currentTimeMillis();

            System.out.print("GET ATM Info- " + reqGetATMDetails.getRemoteAddr()
                    + "(" + atm + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.GetATMDetails(atm) + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetAllATMDetails;

    @POST
    @Path("getAllATMDetails/")
    @Produces(MediaType.APPLICATION_JSON)

    public String GetAllATMDetails(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            beforeResp = System.currentTimeMillis();

            System.out.print("Get All ATM Models - " + reqGetATMModels.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.getAllATMDetails(jsonObj) + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetATMContactDetails;

    @POST
    @Path("getATMContactDetails/")
    @Produces(MediaType.APPLICATION_JSON)

    public String GetATMContactDetails(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            String atm = jsonObj.getString("ATM").trim();
            beforeResp = System.currentTimeMillis();

            System.out.print("GET ATM Contact Details- " + reqGetATMContactDetails.getRemoteAddr()
                    + "(" + atm + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.getATMContactDetails(atm) + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqAddUpdateATMContacts;

    @POST
    @Path("addUpdateATMContacts/")
    @Produces(MediaType.APPLICATION_JSON)
    public String AddUpdateATMContacts(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);

            beforeResp = System.currentTimeMillis();

            System.out.print("Add or Update ATM contact details - " + reqAddUpdateATMContacts.getRemoteAddr()
                    + "(" + jsonObj.getString("atmname") + ")"
                    + "(" + jsonObj.getString("uid") + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            int result = sqlOp.addUpdateATMContacts(jsonObj);

            String ResultJson = "\"\"";

            if (result > 0) {
                Result = "{\"data\":" + "[{\"Result\":" + result + "}]" + "}";
            } else {
                Result = ResultJson;
            }

            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetATMStatus;

    @POST
    @Path("getATMStatus/")
    @Produces(MediaType.APPLICATION_JSON)

    public String GetATMStatus(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            beforeResp = System.currentTimeMillis();

            System.out.print("Get ATM Status - " + reqGetATMStatus.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");
            beforeResp = System.currentTimeMillis();
            Result = "{\"data\":" + sqlOp.getATMStatus(jsonObj) + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetATMUpdatedTime;

    @GET
    @Path("getATMUpdatedTime")
    @Produces(MediaType.APPLICATION_JSON)

    public String GetATMUpdatedTime() {
        sp.setSout();
        String Result = new String();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ServicesQuery sqlOp = new ServicesQuery();
        long beforeResp = 0;
        long afterResp = 0;

        try {
            System.out.print("Get ATM Updated Time - " + reqGetATMUpdatedTime.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");
            beforeResp = System.currentTimeMillis();
            Result = "{\"data\":" + sqlOp.getATMUpdatedTime() + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        sp.closeSout();
        return Result;

    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetDashBoardDetail_1_2;

    @GET
    @Path("getDashBoardDetail_1_2")
    @Produces(MediaType.APPLICATION_JSON)

    public String GetDashBoardDetail_1_2() {
        sp.setSout();
        String Result = new String();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ServicesQuery sqlOp = new ServicesQuery();
        long beforeResp = 0;
        long afterResp = 0;

        try {
            System.out.print("Get Dash Board Details 1 and 2- " + reqGetDashBoardDetail_1_2.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");
            beforeResp = System.currentTimeMillis();

            String resOn = sqlOp.getDashBoardDetail_1_2("(substr(err_code,-1)=1 and SUBSTR(err_code, LENGTH(err_code) - 2, 1)=1)", "OnCnt").replaceAll("\\[", "").replaceAll("\\]", "");
            String resOff = sqlOp.getDashBoardDetail_1_2("(substr(err_code,-1)=0 or SUBSTR(err_code, LENGTH(err_code) - 2, 1)=0)", "OffCnt").replaceAll("\\[", "").replaceAll("\\]", "");
            String resOk = sqlOp.getDashBoardDetail_1_2("(substr(err_code,-1)=1 and SUBSTR(err_code, LENGTH(err_code) - 2, 1)=1) and FLT_COUNT=0", "OkCnt").replaceAll("\\[", "").replaceAll("\\]", "");
            String resFlt0 = sqlOp.getDashBoardDetail_1_2("(substr(err_code,-1)=1 and SUBSTR(err_code, LENGTH(err_code) - 2, 1)=1) and (CRITICALITY=0 and FLT_COUNT>0)", "Flt0Cnt").replaceAll("\\[", "").replaceAll("\\]", "");
            String resFlt = sqlOp.getDashBoardDetail_1_2("(substr(err_code,-1)=1 and SUBSTR(err_code, LENGTH(err_code) - 2, 1)=1) and CRITICALITY>0", "FltCnt").replaceAll("\\[", "").replaceAll("\\]", "");

            String resData = "[" + resOn + "," + resOff + "," + resOk + "," + resFlt0 + "," + resFlt + "]";
            Result = "{\"data\":" + resData + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        sp.closeSout();
        return Result;

    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetATMDeviceList;

    @GET
    @Path("getATMDeviceList/")
    @Produces(MediaType.APPLICATION_JSON)

    public String GetATMDeviceList() {
        sp.setSout();
        String Result = new String();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ServicesQuery sqlOp = new ServicesQuery();
        long beforeResp = 0;
        long afterResp = 0;

        try {
            System.out.print("Get ATM Device List- " + reqGetATMDeviceList.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");
            beforeResp = System.currentTimeMillis();
            Result = "{\"data\":" + sqlOp.getATMDeviceList() + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        sp.closeSout();
        return Result;

    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetDashboard_4;

    @GET
    @Path("getDashboard_4")
    @Produces(MediaType.APPLICATION_JSON)

    public String GetDashboard_4() {
        sp.setSout();
        String Result = new String();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ServicesQuery sqlOp = new ServicesQuery();
        long beforeResp = 0;
        long afterResp = 0;

        try {
            System.out.print("Get Dash Board Details 4 - " + reqGetDashboard_4.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");
            beforeResp = System.currentTimeMillis();

            String resCstEmpty = sqlOp.getDashBoardDetail_4("((protocol=2 and ((substr(err_code,1,1) in('3') and substr(err_code,3,1) in('3') and substr(err_code,5,1) in('3')) or "
                    + "(substr(err_code,1,1) in('3') and substr(err_code,3,1) in('3') and substr(err_code,7,1) in('3')) or "
                    + "(substr(err_code,3,1) in('3') and substr(err_code,5,1) in('3') and substr(err_code,7,1) in('3')) or "
                    + "(substr(err_code,1,1) in('3') and substr(err_code,5,1) in('3') and substr(err_code,7,1) in('3'))  )) "
                    + "or "
                    + "(protocol!=2 and ((substr(err_code,1,1) in('2') and substr(err_code,3,1) in('2') and substr(err_code,5,1) in('2')) or "
                    + "(substr(err_code,1,1) in('2') and substr(err_code,3,1) in('2') and substr(err_code,7,1) in('2')) or "
                    + "(substr(err_code,3,1) in('2') and substr(err_code,5,1) in('2') and substr(err_code,7,1) in('2')) or "
                    + "(substr(err_code,1,1) in('2') and substr(err_code,5,1) in('2') and substr(err_code,7,1) in('2'))  )))", "CstEmptCnt").replaceAll("\\[", "").replaceAll("\\]", "");

            String resCstFault = sqlOp.getDashBoardDetail_4("(protocol!=2 and substr(err_code,1,1) in('9') and substr(err_code,3,1) in('9') "
                    + "and substr(err_code,5,1) in('9') and substr(err_code,7,1) in('9'))", "CstFaultCnt").replaceAll("\\[", "").replaceAll("\\]", "");

            //changed 2023/03/18
//            String resRcpt = sqlOp.GetDashBoardQry4("((type='NDCCRM' and (substr(chk_err,9,1) in('3'))) or "
//                    + "(type!='NDCCRM' and (substr(chk_err,9,1) in('2','3'))))", "RcptCnt").replaceAll("\\[", "").replaceAll("\\]", "");
            String resRcpt = sqlOp.getDashBoardDetail_4("((protocol=2 and (substr(err_code,9,1) in('3'))) or "
                    + "(protocol=1 and div_type in ('CDM','CRM') and (substr(err_code,9,1) in('2','3'))))", "RcptCnt").replaceAll("\\[", "").replaceAll("\\]", "");

            String resPin = sqlOp.getDashBoardDetail_4("((protocol=2 and (substr(err_code,11,1) in('4'))) or "
                    + "(protocol!=2 and (substr(err_code,11,1) in('1'))))", "PinCnt").replaceAll("\\[", "").replaceAll("\\]", "");

            String resCa = sqlOp.getDashBoardDetail_4("((protocol=1 and div_type='ATM' and (substr(err_code,13,1) not in('0'))) or "
                    + "(protocol=1 and div_type in('CDM','CRM') and (substr(err_code,13,1) in('3'))) or "
                    + "(protocol=2 and (substr(err_code,13,3) in('140','440'))))", "CaCnt").replaceAll("\\[", "").replaceAll("\\]", "");

            String resCrd = sqlOp.getDashBoardDetail_4("((protocol=2 and (substr(err_code,17,1) not in('0'))) or "
                    + "(protocol!=2 and (substr(err_code,15,1) not in('0')))) ", "CrdCnt").replaceAll("\\[", "").replaceAll("\\]", "");

            String resData = "[" + resCstEmpty + "," + resCstFault + "," + resRcpt + "," + resPin + "," + resCa + "," + resCrd + "]";
            Result = "{\"data\":" + resData + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        sp.closeSout();
        return Result;

    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetDashboardSeparate;

    @POST
    @Path("getDashboardSeparate/")
    @Produces(MediaType.APPLICATION_JSON)

    public String GetDashboardSeparate(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            String qry = jsonObj.getString("Qry").trim();
            beforeResp = System.currentTimeMillis();

            System.out.print("GET Dashboard Separate List - " + reqGetDashboardSeparate.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.getDashboardSeparate(qry) + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetDashboardSeparate_3;

    @POST
    @Path("getDashboardSeparate_3/")
    @Produces(MediaType.APPLICATION_JSON)

    public String GetDashboardSeparate_3(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            String qry = jsonObj.getString("Qry").trim();
            beforeResp = System.currentTimeMillis();

            System.out.print("GET Dashboard Separate List 3 - " + reqGetDashboardSeparate_3.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.getDashboardSeparate_3(qry) + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetFaultAllTypes;

    @GET
    @Path("getFaultAllTypes/")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetFaultAllTypes() {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {

            beforeResp = System.currentTimeMillis();

            System.out.print("Get All Fault Types - " + reqGetFaultAllTypes.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");
            beforeResp = System.currentTimeMillis();
            Result = "{\"data\":" + sqlOp.getFaultAllTypes() + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetFaultTypeDetails;

    @POST
    @Path("getFaultTypeDetails/")
    @Produces(MediaType.APPLICATION_JSON)

    public String GetFaultTypeDetails(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            String fid = jsonObj.getString("FId").trim();
            beforeResp = System.currentTimeMillis();

            System.out.print("GET Fault Type Details- " + reqGetFaultTypeDetails.getRemoteAddr()
                    + "(" + fid + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.getFaultTypeDetails(fid) + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqUpdateFaultDetails;

    @POST
    @Path("updateFaultDetails/")
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdateFaultDetails(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            beforeResp = System.currentTimeMillis();

            System.out.print("Update Fault Details - " + reqUpdateFaultDetails.getRemoteAddr()
                    + "(" + jsonObj.getString("farID") + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.updateFaultDetails(jsonObj) + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqAddFaultDetails;

    @POST
    @Path("addFaultDetails/")
    @Produces(MediaType.APPLICATION_JSON)
    public String AddFaultDetails(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            beforeResp = System.currentTimeMillis();

            System.out.print("Add New Fault - " + reqUpdateFaultDetails.getRemoteAddr()
                    + "(" + jsonObj.getString("farDesc") + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.addFaultDetails(jsonObj) + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetATMforFaults;

    @GET
    @Path("getATMforFaults/")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetATMforFaults() {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {

            beforeResp = System.currentTimeMillis();

            System.out.print("Get ATM Details for ATM Fault Entry - " + reqGetATMforFaults.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");
            beforeResp = System.currentTimeMillis();
            Result = "{\"data\":" + sqlOp.getATMforFaults() + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetFaults;

    @GET
    @Path("getFaults/")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetFaults() {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {

            beforeResp = System.currentTimeMillis();

            System.out.print("Get Fault Details - " + reqGetFaults.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");
            beforeResp = System.currentTimeMillis();
            Result = "{\"data\":" + sqlOp.getFaults() + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqAddFaultEntry;

    @POST
    @Path("addFaultEntry/")
    @Produces(MediaType.APPLICATION_JSON)
    public String AddFaultEntry(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            beforeResp = System.currentTimeMillis();

            System.out.print("Add Fault Entry - " + reqAddFaultEntry.getRemoteAddr()
                    + "(" + jsonObj.getString("atm") + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.addFaultEntry(jsonObj) + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetEntry;

    @POST
    @Path("getEntry/")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetEntry(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            String type = jsonObj.getString("Type").trim();
            beforeResp = System.currentTimeMillis();

            System.out.print("GET Created Entries - " + reqGetEntry.getRemoteAddr()
                    + "(" + type + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.getEntry(type) + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }
    
    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetCashDetails;

    @POST
    @Path("getCashDetails/")
    @Produces(MediaType.APPLICATION_JSON)

    public String GetCashDetails(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            String atm = jsonObj.getString("ATM").trim();
            beforeResp = System.currentTimeMillis();

            System.out.print("GET ATM Cash Details- " + reqGetCashDetails.getRemoteAddr()
                    + "(" + atm + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.getCashDetails(atm) + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }
    
    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetSMSDetails;

    @POST
    @Path("getSMSDetails/")
    @Produces(MediaType.APPLICATION_JSON)

    public String getSMSDetails(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ServicesQuery sqlOp = new ServicesQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            String atm = jsonObj.getString("ATM").trim();
            beforeResp = System.currentTimeMillis();

            System.out.print("GET ATM SMS Details- " + reqGetSMSDetails.getRemoteAddr()
                    + "(" + atm + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.getSMSDetails(atm) + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }

}
