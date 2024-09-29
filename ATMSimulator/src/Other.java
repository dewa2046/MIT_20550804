
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author it207440
 */
public class Other {

    static String atm;
    static String location;
    static String div;
    static int protocol;
    static String incCash;
    static String outCash;

    public static void setOutCash(String out) {
        outCash = out;
    }

    public static String getOutCash() {
        return outCash;
    }
    
    public static void setIncCash(String inc) {
        incCash = inc;
    }

    public static String getIncCash() {
        return incCash;
    }

    public static void setAtm(String atmName) {
        atm = atmName;
    }

    public static String getAtm() {
        return atm;
    }

    public static void setLocation(String Location) {
        location = Location;
    }

    public static String getLocation() {
        return location;
    }

    public static void setDiv(String divtype) {
        div = divtype;
    }

    public static String getDiv() {
        return div;
    }

    public static void setProtocol(int prot) {
        protocol = prot;
    }

    public static int getProtocol() {
        return protocol;
    }

    public void selectButton(String value, ButtonGroup buttonGroup1) {
        for (Enumeration<AbstractButton> buttons = buttonGroup1.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.getText().equals(value)) {
                button.setSelected(true);
                return;
            }
        }
    }
    
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
