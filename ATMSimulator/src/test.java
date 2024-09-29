
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author it207440
 */
public class test {

    public static void main(String[] args) {
        test tst = new test();
        tst.testfun();
    }

    public void testfun() {

        try {
            sqlop sql = new sqlop();
//            int maxId = sql.getMaxSMSID();
//            System.out.println(sql.checkPreErr("X","9 9 9 9 0 0 0 0 1 1"));

//            System.out.println(sql.genarateSMSTxt("ACT1", "2 2 1 1 1 0 440 0 1 1", "City Office",2,"CRM"));
//            ArrayList<String> OperatorsContacts = new ArrayList<String>();
//
//            OperatorsContacts = sql.getOperatorContacts("AECHO1");
//            System.out.println(OperatorsContacts.size());
//
//            if (OperatorsContacts.size() > 0) {
//                for (int i = 0; i < OperatorsContacts.size(); i++) {
//                    System.out.println(OperatorsContacts.get(i));
//                }
//            }

            System.out.println(sql.insertSMS("X","1 1 1 1 0 0 0 0 1 1",1,"Test","ATM"));
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

}
