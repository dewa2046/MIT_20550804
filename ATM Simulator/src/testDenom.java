/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author it207440
 */
public class testDenom {

    public static void main(String[] args) {
        testDenom tst = new testDenom();
        tst.testDispense();
    }

    public void testDispense() {

        try {
            int tranAmout = 600;
            int denom1 = 100;
            int denom2 = 500;
            int denom3 = 0;
            int denom4 = 0;

            int val1 = 10;
            int val2 = 10;
            int val3 = 0;
            int val4 = 0;

            int val1dispense = 0;
            int val2dispense = 0;
            int val3dispense = 0;
            int val4dispense = 0;

            if (val4 > 0) {
                if ((tranAmout / denom4) < val4) {
                    val4dispense = (tranAmout / denom4);
                    val4 -= (tranAmout / denom4);
                    tranAmout -= (denom4 * (tranAmout / denom4));                    
                }
            }
            if (val3 > 0) {
                if ((tranAmout / denom3) < val3) {
                    val3dispense = (tranAmout / denom3);
                    val3 -= (tranAmout / denom3);
                    tranAmout -= (denom3 * (tranAmout / denom3));
                    
                }
            }
            if (val2 > 0) {
                if ((tranAmout / denom2) < val2) {
                    val2dispense = (tranAmout / denom2);
                    val2 -= (tranAmout / denom2);
                    tranAmout -= (denom2 * (tranAmout / denom2));
                    
                }
            }
            if (val1 > 0) {
                if ((tranAmout / denom1) < val1) {
                    val1dispense = (tranAmout / denom1);
                    val1 -= (tranAmout / denom1);  
                    tranAmout -= (denom1 * (tranAmout / denom1));
                }
            }

            System.out.println(val4 + "~" + tranAmout + "~" + val4dispense);
            System.out.println(val3 + "~" + tranAmout + "~" + val3dispense);
            System.out.println(val2 + "~" + tranAmout + "~" + val2dispense);
            System.out.println(val1 + "~" + tranAmout + "~" + val1dispense);

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

}
