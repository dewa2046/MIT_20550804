/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BackEnd;

/**
 *
 * @author Administrator
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SetOutPut {

    public static String fileName;
    public static PrintStream out;

    public void setSout() {

        try {
            String filePath = "D:\\Project\\ProjectLogs\\";
            //createdir(filePath);   
            String curDate = getCurDate();
            fileName = filePath + "/" + curDate + ".txt";
            out = new PrintStream(new FileOutputStream(fileName, true));
            System.setOut(out);

        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    public String getCurDate() {

        String curDate = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        LocalDateTime now = LocalDateTime.now();
        curDate = dtf.format(now);
        return curDate;
    }

    public void closeSout() {
        out.close();
    }

}
