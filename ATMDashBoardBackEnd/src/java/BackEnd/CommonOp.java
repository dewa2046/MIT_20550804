/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BackEnd;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Administrator
 */
public class CommonOp {

    public String localDownloadPath = "E:\\ProjectLogs\\";

    public String getlocalDownloadPath() {
        return this.localDownloadPath;
    }

    // Create Directory
    public void createdir(String dir) {

        try {
            File file = new File(dir);
            boolean isdir = file.exists();
            if (!isdir) {
                isdir = file.mkdir();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////
    //get Current Date for given pattern
    public String getCurDate(String pattern) {

        String curDate = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        curDate = dtf.format(now);
        return curDate;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //check already downloaded
    public boolean checkFileExists(String fName) {

        boolean status = false;

        try {
            File tmpFile = new File(fName);
            if (tmpFile.exists()) {
                status = true;
            } else {
                status = false;
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return status;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //delete File
    public boolean deleteFile(String fName) {

        boolean status = false;

        try {
            File tmpFile = new File(fName);
            if (tmpFile.delete()) {
                status = true;
            } else {
                status = false;
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return status;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //check file is not zero size
    public boolean checkFileSize(String fName) {

        boolean status = false;

        try {
            File tmpFile = new File(fName);
            if (tmpFile.exists()) {
                double bytes = tmpFile.length();
                double kilobytes = (bytes / 1024);
                if (kilobytes > 0) {
                    status = true;
                }
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return status;
    }

}
