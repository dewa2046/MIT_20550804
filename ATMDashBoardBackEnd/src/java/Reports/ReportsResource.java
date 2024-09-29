/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package Reports;

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
@Path("reports")
public class ReportsResource {

    SetOutPut sp = new SetOutPut();

    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetProvinceDetails;

    @GET
    @Path("getProvinceDetails/")
    @Produces(MediaType.APPLICATION_JSON)
    public String getProvinceDetails() {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ReportsQuery sqlOp = new ReportsQuery();

        try {

            beforeResp = System.currentTimeMillis();

            System.out.print("Get Province Details - " + reqGetProvinceDetails.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");
            beforeResp = System.currentTimeMillis();
            Result = "{\"data\":" + sqlOp.getProvinceDetails() + "}";
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
    private javax.servlet.http.HttpServletRequest reqGetATMInfoReport;

    @POST
    @Path("getATMInfoReport/")
    @Produces(MediaType.APPLICATION_JSON)

    public String getATMInfoReport(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ReportsQuery sqlOp = new ReportsQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            String qry = jsonObj.getString("QRY").trim();
            beforeResp = System.currentTimeMillis();

            System.out.print("GET ATM Info Report- " + reqGetATMInfoReport.getRemoteAddr()
                    + "(" + qry + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.getATMInfoReport(qry) + "}";
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
    private javax.servlet.http.HttpServletRequest getAllDownReport;

    @POST
    @Path("getAllDownReport/")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetAllDownReport(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ReportsQuery sqlOp = new ReportsQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            String fromDate = jsonObj.getString("fromDate").trim();
            String toDate = jsonObj.getString("toDate").trim();
            Integer userLvl = Integer.parseInt(jsonObj.getString("uLvl").trim());
            Integer userBr = Integer.parseInt(jsonObj.getString("uBr").trim());
            beforeResp = System.currentTimeMillis();

            System.out.print("GET All Downtime Report - " + getAllDownReport.getRemoteAddr()
                    + "(" + fromDate + "-" + toDate + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.getAllDownTimeReport(fromDate, toDate, userLvl, userBr) + "}";
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
    private javax.servlet.http.HttpServletRequest getDownTimeFaultTypeWise;

    @POST
    @Path("getDownTimeFaultTypeWise/")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetDownTimeFaultTypeWise(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ReportsQuery sqlOp = new ReportsQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            String fromDate = jsonObj.getString("fromDate").trim();
            String toDate = jsonObj.getString("toDate").trim();
            Integer userLvl = Integer.parseInt(jsonObj.getString("uLvl").trim());
            Integer userBr = Integer.parseInt(jsonObj.getString("uBr").trim());
            beforeResp = System.currentTimeMillis();

            System.out.print("GET All Downtime Report - " + getDownTimeFaultTypeWise.getRemoteAddr()
                    + "(" + fromDate + "-" + toDate + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.getDownTimeFaultTypeWise(fromDate, toDate, userLvl, userBr) + "}";
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
    private javax.servlet.http.HttpServletRequest getDownTimeIndividual;

    @POST
    @Path("getDownTimeIndividual/")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetDownTimeIndividual(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ReportsQuery sqlOp = new ReportsQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            String fromDate = jsonObj.getString("fromDate").trim();
            String toDate = jsonObj.getString("toDate").trim();
            String option = jsonObj.getString("option").trim();
            Integer userLvl = Integer.parseInt(jsonObj.getString("uLvl").trim());
            Integer userBr = Integer.parseInt(jsonObj.getString("uBr").trim());
            beforeResp = System.currentTimeMillis();

            System.out.print("GET Individual Downtime Report - " + getDownTimeIndividual.getRemoteAddr()
                    + "(" + fromDate + "-" + toDate + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.getDownTimeIndividual(fromDate, toDate, option, userLvl, userBr) + "}";
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
    private javax.servlet.http.HttpServletRequest reqGetFaultReportUsers;

    @GET
    @Path("getFaultReportUsers/")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFaultReportUsers() {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ReportsQuery sqlOp = new ReportsQuery();

        try {

            beforeResp = System.currentTimeMillis();

            System.out.print("Get Fault Report Users - " + reqGetFaultReportUsers.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");
            beforeResp = System.currentTimeMillis();
            Result = "{\"data\":" + sqlOp.getFaultReportUsers() + "}";
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
    private javax.servlet.http.HttpServletRequest reqGetATMFaultReport;

    @POST
    @Path("getATMFaultReport/")
    @Produces(MediaType.APPLICATION_JSON)

    public String getATMFaultReport(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ReportsQuery sqlOp = new ReportsQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            String qry = jsonObj.getString("QRY").trim();
            beforeResp = System.currentTimeMillis();

            System.out.print("GET ATM Fault Report- " + reqGetATMFaultReport.getRemoteAddr()
                    + "(" + qry + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.getATMFaultReport(qry) + "}";
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
    private javax.servlet.http.HttpServletRequest reqgetCashLow;

    @POST
    @Path("getCashLow/")
    @Produces(MediaType.APPLICATION_JSON)

    public String getCashLow(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        ReportsQuery sqlOp = new ReportsQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            int cstLimit = Integer.parseInt(jsonObj.getString("LIMIT").trim());
            beforeResp = System.currentTimeMillis();

            System.out.print("GET ATM Cash Low Report- " + reqgetCashLow.getRemoteAddr()
                    + "(" + cstLimit + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.getCashLow(cstLimit) + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }
}
