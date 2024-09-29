/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Users;

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
 *
 * @author Administrator
 */
@Path("user")
public class UserResource {

    SetOutPut sp = new SetOutPut();

    @Context
    private javax.servlet.http.HttpServletRequest reqCheckLogin;

    @POST
    @Path("checkLogin/")
    @Produces(MediaType.APPLICATION_JSON)
    public String CheckLogin(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        UserQuery sqlOp = new UserQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            String user = jsonObj.getString("UName").trim();
            String pwd = jsonObj.getString("Pwd").trim();
            beforeResp = System.currentTimeMillis();

            System.out.print("Check Login - " + reqCheckLogin.getRemoteAddr()
                    + "(" + jsonObj.getString("UName") + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.CheckLogin(user, pwd, reqCheckLogin.getRemoteAddr()) + "}";
//            System.out.println(Result);
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
    private javax.servlet.http.HttpServletRequest reqCheckAllowedFunc;

    @POST
    @Path("checkAllowedFunc/")
    @Produces(MediaType.APPLICATION_JSON)
    public String CheckAllowedFunc(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        UserQuery sqlOp = new UserQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            int user_lvl = Integer.parseInt(jsonObj.getString("ULvl").trim());
            beforeResp = System.currentTimeMillis();

            System.out.print("Get Allowed Functions - " + reqCheckAllowedFunc.getRemoteAddr()
                    + "(" + jsonObj.getString("ULvl") + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.getAllowedFun(user_lvl) + "}";

            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }
//
    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetSystemUsers;

    @GET
    @Path("getSystemUsers/")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetSystemUsers() {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        UserQuery sqlOp = new UserQuery();

        try {

            beforeResp = System.currentTimeMillis();

            System.out.print("Get System Users - " + reqGetSystemUsers.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");
            beforeResp = System.currentTimeMillis();
            Result = "{\"data\":" + sqlOp.getSystemUsers() + "}";
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
    private javax.servlet.http.HttpServletRequest reqGetAllUserFunctions;

    @GET
    @Path("getAllUserFunctions/")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetAllUserFunctions() {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        UserQuery sqlOp = new UserQuery();

        try {

            beforeResp = System.currentTimeMillis();

            System.out.print("Get All User Functions - " + reqGetAllUserFunctions.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");
            beforeResp = System.currentTimeMillis();
            Result = "{\"data\":" + sqlOp.getAllUserFunctions() + "}";
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
    private javax.servlet.http.HttpServletRequest reqGetUserLvlFunction;

    @POST
    @Path("getUserLvlFunctions/")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetUserLvlFunctions(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        UserQuery sqlOp = new UserQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            String userId = jsonObj.getString("UId").trim();
            beforeResp = System.currentTimeMillis();

            System.out.print("Get User Level Functions - " + reqGetUserLvlFunction.getRemoteAddr()
                    + "(" + jsonObj.getString("UId") + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.getUserLvlFunctions(userId) + "}";
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
    private javax.servlet.http.HttpServletRequest reqUpdateUserFunction;

    @POST
    @Path("updateUserFunc/")
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdateUserFunc(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        UserQuery sqlOp = new UserQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            String userId = jsonObj.getString("UId").trim();
            String funcId = jsonObj.getString("UFunc").trim();
            String updatedUser = jsonObj.getString("UpdatedUser").trim();
            String userGroupDesc = jsonObj.getString("UserGroupNew").trim();
            beforeResp = System.currentTimeMillis();

            System.out.print("Update User Functions - " + reqUpdateUserFunction.getRemoteAddr()
                    + "(" + jsonObj.getString("UId") + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.updateUserFunc(userId, funcId, updatedUser,userGroupDesc) + "}";
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
    private javax.servlet.http.HttpServletRequest reqAddNewUserGroup;

    @POST
    @Path("addNewUserGroup/")
    @Produces(MediaType.APPLICATION_JSON)
    public String AddNewUserGroup(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        UserQuery sqlOp = new UserQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            
            beforeResp = System.currentTimeMillis();

            System.out.print("Create New User Group - " + reqAddNewUserGroup.getRemoteAddr()
                    + "(" + jsonObj.getString("UGroup") + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.addNewUserGroup(jsonObj) + "}";
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
    private javax.servlet.http.HttpServletRequest reqUpdateUserDetails;

    @POST
    @Path("updateUserDetails/")
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdateUserDetails(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        UserQuery sqlOp = new UserQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            beforeResp = System.currentTimeMillis();

            System.out.print("Update User Functions - " + reqUpdateUserDetails.getRemoteAddr()
                    + "(" + jsonObj.getString("userName") + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.updateUserDet(jsonObj) + "}";
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
    private javax.servlet.http.HttpServletRequest reqCreateNewUser;

    @POST
    @Path("createNewUser/")
    @Produces(MediaType.APPLICATION_JSON)
    public String CreateNewUser(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        UserQuery sqlOp = new UserQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            beforeResp = System.currentTimeMillis();

            System.out.print("Create New User - " + reqCreateNewUser.getRemoteAddr()
                    + "(" + jsonObj.getString("userName") + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.createNewUser(jsonObj) + "}";
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
    private javax.servlet.http.HttpServletRequest reqGetPassword;

    @POST
    @Path("getPassword/")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetPassword(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        UserQuery sqlOp = new UserQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            String user = jsonObj.getString("UId").trim();
            beforeResp = System.currentTimeMillis();

            System.out.print("Get Password - " + reqGetPassword.getRemoteAddr()
                    + "(" + jsonObj.getString("UId") + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.getPassword(user) + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }
//
    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqUpdatePassword;

    @POST
    @Path("updatePassword/")
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdatePassword(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        UserQuery sqlOp = new UserQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            beforeResp = System.currentTimeMillis();

            System.out.print("Update Password - " + reqUpdatePassword.getRemoteAddr()
                    + "(" + jsonObj.getString("uId") + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.changePassword(jsonObj) + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }
//
    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqResetPassword;

    @POST
    @Path("resetPassword/")
    @Produces(MediaType.APPLICATION_JSON)
    public String ResetPassword(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        UserQuery sqlOp = new UserQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            beforeResp = System.currentTimeMillis();

            System.out.print("Reset Password - " + reqResetPassword.getRemoteAddr()
                    + "(" + jsonObj.getString("resetUserName") + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.resetPassword(jsonObj) + "}";
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
    private javax.servlet.http.HttpServletRequest reqGetIndicator;

    @POST
    @Path("getIndicator/")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetIndicator(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        UserQuery sqlOp = new UserQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);
            String ind = jsonObj.getString("Ind").trim();
            beforeResp = System.currentTimeMillis();

            System.out.print("Get Indicator - " + reqGetIndicator.getRemoteAddr()
                    + "(" + jsonObj.getString("Ind") + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            Result = "{\"data\":" + sqlOp.getIndicator(ind) + "}";
            afterResp = System.currentTimeMillis();

            System.out.println((afterResp - beforeResp) / 1000 + " seconds");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        sp.closeSout();
        return Result;
    }
//
    //*******************************************************************
    @Context
    private javax.servlet.http.HttpServletRequest reqGetUserLvl;

    @GET
    @Path("getUserLvl/")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetUserLvl() {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        UserQuery sqlOp = new UserQuery();

        try {

            beforeResp = System.currentTimeMillis();

            System.out.print("Get All User Levels - " + reqGetUserLvl.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");
            beforeResp = System.currentTimeMillis();
            Result = "{\"data\":" + sqlOp.getUserLvl() + "}";
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
    private javax.servlet.http.HttpServletRequest reqGetUserBranch;

    @GET
    @Path("getUserBranch/")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetUserBranch() {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        UserQuery sqlOp = new UserQuery();

        try {

            beforeResp = System.currentTimeMillis();

            System.out.print("Get All User Branch - " + reqGetUserBranch.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");
            beforeResp = System.currentTimeMillis();
            Result = "{\"data\":" + sqlOp.getUserBranch() + "}";
//            System.out.println(Result);
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
    private javax.servlet.http.HttpServletRequest reqGetAllBranches;

    @GET
    @Path("getAllBranches/")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetAllBranches() {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        UserQuery sqlOp = new UserQuery();

        try {

            beforeResp = System.currentTimeMillis();

            System.out.print("Get All Branches - " + reqGetAllBranches.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");
            beforeResp = System.currentTimeMillis();
            Result = "{\"data\":" + sqlOp.getAllBranches() + "}";
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
    private javax.servlet.http.HttpServletRequest reqGetAllProvinces;

    @GET
    @Path("getAllProvinces/")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetAllProvinces() {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        UserQuery sqlOp = new UserQuery();

        try {

            beforeResp = System.currentTimeMillis();

            System.out.print("Get All Provinces - " + reqGetAllProvinces.getRemoteAddr()
                    + "(" + dtf.format(now) + ")" + " Response Time - ");
            beforeResp = System.currentTimeMillis();
            Result = "{\"data\":" + sqlOp.getAllProvinces() + "}";
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
    private javax.servlet.http.HttpServletRequest reqAddUpdateBranchDetails;

    @POST
    @Path("addUpdateBranchetails/")
    @Produces(MediaType.APPLICATION_JSON)
    public String AddUpdateBranchDetails(String ServiceRecordjasonobject) throws JSONException, FileNotFoundException {
        sp.setSout();
        String Result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        long beforeResp = 0;
        long afterResp = 0;
        UserQuery sqlOp = new UserQuery();

        try {
            JSONObject jsonObj = new JSONObject(ServiceRecordjasonobject);

            beforeResp = System.currentTimeMillis();

            System.out.print("Add or Update Branch details - " + reqAddUpdateBranchDetails.getRemoteAddr()
                    + "(" + jsonObj.getString("brname") + ")("
                    + dtf.format(now) + ")" + " Response Time - ");

            int result = sqlOp.AddUpdateBranchDetails(jsonObj);

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
}
