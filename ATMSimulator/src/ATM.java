
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author it207440
 */
public class ATM extends javax.swing.JFrame {

    /**
     * Creates new form ATM
     */
    public ATM() {
        initComponents();
        loadDetails();
    }

    public void loadDetails() {

        try {
            Other other = new Other();
            sqlop sql = new sqlop();
            String atm = other.getAtm();
            String location = other.getLocation();
            String div = other.getDiv();
            int protocol = other.getProtocol();
            if (location != "") {
                txtATM.setText(atm);
            }
            if (atm != "") {
                txtLocation.setText(location);
            }

            String atmStatus = sql.getATMStatus(atm);

            setDeviceDetails(atmStatus);
            setCash(sql.getATMCstDetails(atm));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setCash(String cashStatus) {

        try {

            Other other = new Other();

            if (cashStatus.length() > 0) {
                String denom = cashStatus.split("#")[0];
                String incr = cashStatus.split("#")[1];
                String out = cashStatus.split("#")[2];
                String end = cashStatus.split("#")[3];

                other.setIncCash(incr);
                other.setOutCash(out);

                lblDenom1Val.setText(denom.split("~")[0]);
                lblDenom2Val.setText(denom.split("~")[1]);
                lblDenom3Val.setText(denom.split("~")[2]);
                lblDenom4Val.setText(denom.split("~")[3]);
                txtDenomVal1.setText(end.split("~")[0]);
                txtDenomVal2.setText(end.split("~")[1]);
                txtDenomVal3.setText(end.split("~")[2]);
                txtDenomVal4.setText(end.split("~")[3]);

            } else {
                lblDenom1Val.setText("100");
                lblDenom2Val.setText("500");
                lblDenom3Val.setText("1000");
                lblDenom4Val.setText("5000");
                txtDenomVal1.setText("0");
                txtDenomVal2.setText("0");
                txtDenomVal3.setText("0");
                txtDenomVal4.setText("0");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void setDeviceDetails(String atmStatus) {

        Other other = new Other();
        try {
            int cst1 = 2; //no cash loaded
            int cst2 = 2;//no cash loaded
            int cst3 = 2;//no cash loaded
            int cst4 = 2;//no cash loaded
            int rcpt = 0;
            int pinPad = 0;
            int drawer = 1; //no cash loaded drawer fault
            int card = 0;
            int stat = 1;
            int state = 1;

            if (atmStatus.length() > 0) {
                String err = atmStatus.split("~")[1].trim();
                cst1 = Integer.parseInt(err.split(" ")[0]);
                cst2 = Integer.parseInt(err.split(" ")[1]);
                cst3 = Integer.parseInt(err.split(" ")[2]);
                cst4 = Integer.parseInt(err.split(" ")[3]);

                rcpt = Integer.parseInt(err.split(" ")[4]);
                pinPad = Integer.parseInt(err.split(" ")[5]);
                drawer = Integer.parseInt(err.split(" ")[6]);
                card = Integer.parseInt(err.split(" ")[7]);
                stat = Integer.parseInt(err.split(" ")[8]);
                state = Integer.parseInt(err.split(" ")[9]);

            }
            // CST1
            if (cst1 == 0) {
                other.selectButton("CST1 OK", BGCST1);
            }
            if (cst1 == 1) {
                other.selectButton("CST1 LOW", BGCST1);
            }
            if (cst1 == 2) {
                other.selectButton("CST1 EMPTY", BGCST1);
            }
            if (cst1 == 9) {
                other.selectButton("CST1 FAULT", BGCST1);
            }
            if (cst1 == 5) {
                other.selectButton("CST1 NOT PRESENT", BGCST1);
            }
            // CST2
            if (cst2 == 0) {
                other.selectButton("CST2 OK", BGCST2);
            }
            if (cst2 == 1) {
                other.selectButton("CST2 LOW", BGCST2);
            }
            if (cst2 == 2) {
                other.selectButton("CST2 EMPTY", BGCST2);
            }
            if (cst2 == 9) {
                other.selectButton("CST2 FAULT", BGCST2);
            }
            if (cst2 == 5) {
                other.selectButton("CST2 NOT PRESENT", BGCST2);
            }
            //CST3
            if (cst3 == 0) {
                other.selectButton("CST3 OK", BGCST3);
            }
            if (cst3 == 1) {
                other.selectButton("CST3 LOW", BGCST3);
            }
            if (cst3 == 2) {
                other.selectButton("CST3 EMPTY", BGCST3);
            }
            if (cst3 == 9) {
                other.selectButton("CST3 FAULT", BGCST3);
            }
            if (cst3 == 5) {
                other.selectButton("CST3 NOT PRESENT", BGCST3);
            }
            //CST4
            if (cst4 == 0) {
                other.selectButton("CST4 OK", BGCST4);
            }
            if (cst4 == 1) {
                other.selectButton("CST4 LOW", BGCST4);
            }
            if (cst4 == 2) {
                other.selectButton("CST4 EMPTY", BGCST4);
            }
            if (cst4 == 9) {
                other.selectButton("CST4 FAULT", BGCST4);
            }
            if (cst4 == 5) {
                other.selectButton("CST4 NOT PRESENT", BGCST4);
            }
            //Receipt Printer
            if (rcpt == 0) {
                other.selectButton("OK", BGRP);
            }
            if (rcpt == 1) {
                other.selectButton("LOW", BGRP);
            }
            if (rcpt == 2) {
                other.selectButton("EMPTY", BGRP);
            }
            if (rcpt == 3) {
                other.selectButton("FAULT", BGRP);
            }
            //Pin Pad
            if (pinPad == 0 || pinPad == 9) {
                other.selectButton("OK", BGPINPAD);
            }
            if (pinPad == 1) {
                other.selectButton("FAULT", BGPINPAD);
            }
            //Money Drawer
            if (drawer == 0) {
                other.selectButton("OK", BGDISPENSER);
            }
            if (drawer == 1) {
                other.selectButton("FAULT", BGDISPENSER);
            }
            if (drawer == 2) {
                other.selectButton("MONEY LEFT", BGDISPENSER);
            }
            //Card Reader
            if (card == 0) {
                other.selectButton("OK", BGCR);
            }
            if (card == 1) {
                other.selectButton("FAULT", BGCR);
            }
            //Stat
            if (stat == 1) {
                other.selectButton("UP", BGSTAT);
            }
            if (stat == 0) {
                other.selectButton("DOWN", BGSTAT);
            }
            //State
            if (state == 1) {
                other.selectButton("OPEN", BGSTATE);
            }
            if (state == 0) {
                other.selectButton("CLOSE", BGSTATE);
            }

//            System.out.println(atmStatus.split("~")[1] + "///" + cst1 + " " + cst2 + " " + cst3 + " " + cst4 + " "
//                    + rcpt + " " + pinPad + " " + drawer + " " + card + " " + stat + " " + state);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BGCR = new javax.swing.ButtonGroup();
        BGRP = new javax.swing.ButtonGroup();
        BGCST1 = new javax.swing.ButtonGroup();
        BGCST2 = new javax.swing.ButtonGroup();
        BGCST3 = new javax.swing.ButtonGroup();
        BGCST4 = new javax.swing.ButtonGroup();
        BGSTAT = new javax.swing.ButtonGroup();
        BGDISPENSER = new javax.swing.ButtonGroup();
        BGPINPAD = new javax.swing.ButtonGroup();
        BGSTATE = new javax.swing.ButtonGroup();
        PanelATMInfo = new javax.swing.JPanel();
        lblATM = new javax.swing.JLabel();
        txtATM = new javax.swing.JTextField();
        lblLocation = new javax.swing.JLabel();
        txtLocation = new javax.swing.JTextField();
        CRPanel = new javax.swing.JPanel();
        cbCROK = new javax.swing.JCheckBox();
        cbCRFault = new javax.swing.JCheckBox();
        RPPanel = new javax.swing.JPanel();
        cbRPOK = new javax.swing.JCheckBox();
        cbRPEmpty = new javax.swing.JCheckBox();
        cbRPFault = new javax.swing.JCheckBox();
        cbRPLow = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        CSTPanel = new javax.swing.JPanel();
        cbCST1OK = new javax.swing.JCheckBox();
        cbCST1Low = new javax.swing.JCheckBox();
        cbCST1Empty = new javax.swing.JCheckBox();
        cbCST1Fault = new javax.swing.JCheckBox();
        cbCST2OK = new javax.swing.JCheckBox();
        cbCST2Low = new javax.swing.JCheckBox();
        cbCST2Empty = new javax.swing.JCheckBox();
        cbCST2Fault = new javax.swing.JCheckBox();
        cbCST3OK = new javax.swing.JCheckBox();
        cbCST3Low = new javax.swing.JCheckBox();
        cbCST3Empty = new javax.swing.JCheckBox();
        cbCST3Fault = new javax.swing.JCheckBox();
        cbCST4OK = new javax.swing.JCheckBox();
        cbCST4Low = new javax.swing.JCheckBox();
        cbCST4Empty = new javax.swing.JCheckBox();
        cbCST4Fault = new javax.swing.JCheckBox();
        cbCST1NP = new javax.swing.JCheckBox();
        cbCST2NP = new javax.swing.JCheckBox();
        cbCST3NP = new javax.swing.JCheckBox();
        cbCST4NP = new javax.swing.JCheckBox();
        CSTDenomPanel = new javax.swing.JPanel();
        lblDenom1 = new javax.swing.JLabel();
        txtDenomVal1 = new javax.swing.JTextField();
        lblDenom2 = new javax.swing.JLabel();
        txtDenomVal2 = new javax.swing.JTextField();
        lblDenom3 = new javax.swing.JLabel();
        txtDenomVal3 = new javax.swing.JTextField();
        lblDenom4 = new javax.swing.JLabel();
        txtDenomVal4 = new javax.swing.JTextField();
        lblDenom1Val = new javax.swing.JLabel();
        lblDenom2Val = new javax.swing.JLabel();
        lblDenom3Val = new javax.swing.JLabel();
        lblDenom4Val = new javax.swing.JLabel();
        StatPanel = new javax.swing.JPanel();
        cbStatUp = new javax.swing.JCheckBox();
        cbStatDown = new javax.swing.JCheckBox();
        DispenserPanel = new javax.swing.JPanel();
        cbDispenserOk = new javax.swing.JCheckBox();
        cbDispenserFault = new javax.swing.JCheckBox();
        cbDispenserMoneyLeft = new javax.swing.JCheckBox();
        StatePanel = new javax.swing.JPanel();
        cbStateOpen = new javax.swing.JCheckBox();
        cbStateClose = new javax.swing.JCheckBox();
        btnATMStatus = new javax.swing.JButton();
        PinPadDenomPanel = new javax.swing.JPanel();
        cbPinPadOk = new javax.swing.JCheckBox();
        cbPinPadFault = new javax.swing.JCheckBox();
        CSTOtherPanel = new javax.swing.JPanel();
        lblDenom5 = new javax.swing.JLabel();
        txtDenom1Val = new javax.swing.JTextField();
        lblDenom6 = new javax.swing.JLabel();
        txtDenom2Val = new javax.swing.JTextField();
        lblDenom7 = new javax.swing.JLabel();
        txtDenom3Val = new javax.swing.JTextField();
        lblDenom8 = new javax.swing.JLabel();
        txtDenom4Val = new javax.swing.JTextField();
        btnWithdrawal = new javax.swing.JButton();
        txtWdAmount = new javax.swing.JTextField();
        lblDenom1Val1 = new javax.swing.JLabel();
        lblDenom2Val1 = new javax.swing.JLabel();
        lblDenom3Val1 = new javax.swing.JLabel();
        lblDenom4Val1 = new javax.swing.JLabel();
        btnLoad = new javax.swing.JButton();
        lblDenom9 = new javax.swing.JLabel();

        BGCR.add(cbCROK);
        BGCR.add(cbCRFault);

        BGRP.add(cbRPOK);
        BGRP.add(cbRPLow);
        BGRP.add(cbRPEmpty);
        BGRP.add(cbRPFault);

        BGCST1.add(cbCST1OK);
        BGCST1.add(cbCST1Low);
        BGCST1.add(cbCST1Empty);
        BGCST1.add(cbCST1Fault);
        BGCST1.add(cbCST1NP);

        BGCST2.add(cbCST2OK);
        BGCST2.add(cbCST2Low);
        BGCST2.add(cbCST2Empty);
        BGCST2.add(cbCST2Fault);
        BGCST2.add(cbCST2NP);

        BGCST3.add(cbCST3OK);
        BGCST3.add(cbCST3Low);
        BGCST3.add(cbCST3Empty);
        BGCST3.add(cbCST3Fault);
        BGCST3.add(cbCST3NP);

        BGCST4.add(cbCST4OK);
        BGCST4.add(cbCST4Low);
        BGCST4.add(cbCST4Empty);
        BGCST4.add(cbCST4Fault);
        BGCST4.add(cbCST4NP);

        BGSTAT.add(cbStatUp);
        BGSTAT.add(cbStatDown);

        BGDISPENSER.add(cbDispenserOk);
        BGDISPENSER.add(cbDispenserFault);
        BGDISPENSER.add(cbDispenserMoneyLeft);

        BGPINPAD.add(cbPinPadOk);
        BGPINPAD.add(cbPinPadFault);

        BGSTATE.add(cbStateOpen);
        BGSTATE.add(cbStateClose);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ATM Simulator");

        PanelATMInfo.setBackground(new java.awt.Color(204, 204, 204));

        lblATM.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblATM.setText("ATM NAME");

        txtATM.setEditable(false);
        txtATM.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        txtATM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtATMActionPerformed(evt);
            }
        });

        lblLocation.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblLocation.setText("LOCATION");

        txtLocation.setEditable(false);
        txtLocation.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N

        javax.swing.GroupLayout PanelATMInfoLayout = new javax.swing.GroupLayout(PanelATMInfo);
        PanelATMInfo.setLayout(PanelATMInfoLayout);
        PanelATMInfoLayout.setHorizontalGroup(
            PanelATMInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelATMInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblATM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtATM, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(lblLocation)
                .addGap(18, 18, 18)
                .addComponent(txtLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelATMInfoLayout.setVerticalGroup(
            PanelATMInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelATMInfoLayout.createSequentialGroup()
                .addGroup(PanelATMInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelATMInfoLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(lblATM))
                    .addGroup(PanelATMInfoLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(txtATM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelATMInfoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(PanelATMInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblLocation)
                            .addComponent(txtLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        CRPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CARD READER", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Century", 0, 10))); // NOI18N

        cbCROK.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCROK.setText("OK");

        cbCRFault.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCRFault.setText("FAULT");

        javax.swing.GroupLayout CRPanelLayout = new javax.swing.GroupLayout(CRPanel);
        CRPanel.setLayout(CRPanelLayout);
        CRPanelLayout.setHorizontalGroup(
            CRPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CRPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbCROK)
                .addGap(18, 18, 18)
                .addComponent(cbCRFault)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        CRPanelLayout.setVerticalGroup(
            CRPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CRPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cbCROK)
                .addComponent(cbCRFault))
        );

        RPPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "RECEIPT PRINTER", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Century", 0, 10))); // NOI18N

        cbRPOK.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbRPOK.setText("OK");

        cbRPEmpty.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbRPEmpty.setText("EMPTY");

        cbRPFault.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbRPFault.setText("FAULT");

        cbRPLow.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbRPLow.setText("LOW");

        javax.swing.GroupLayout RPPanelLayout = new javax.swing.GroupLayout(RPPanel);
        RPPanel.setLayout(RPPanelLayout);
        RPPanelLayout.setHorizontalGroup(
            RPPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RPPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbRPOK)
                .addGap(18, 18, 18)
                .addComponent(cbRPLow)
                .addGap(18, 18, 18)
                .addComponent(cbRPEmpty)
                .addGap(18, 18, 18)
                .addComponent(cbRPFault)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        RPPanelLayout.setVerticalGroup(
            RPPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RPPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cbRPOK)
                .addComponent(cbRPEmpty)
                .addComponent(cbRPFault)
                .addComponent(cbRPLow))
        );

        CSTPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CASSETTE", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Century", 0, 10))); // NOI18N

        cbCST1OK.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST1OK.setText("CST1 OK");

        cbCST1Low.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST1Low.setText("CST1 LOW");

        cbCST1Empty.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST1Empty.setText("CST1 EMPTY");

        cbCST1Fault.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST1Fault.setText("CST1 FAULT");

        cbCST2OK.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST2OK.setText("CST2 OK");

        cbCST2Low.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST2Low.setText("CST2 LOW");

        cbCST2Empty.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST2Empty.setText("CST2 EMPTY");

        cbCST2Fault.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST2Fault.setText("CST2 FAULT");

        cbCST3OK.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST3OK.setText("CST3 OK");
        cbCST3OK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCST3OKActionPerformed(evt);
            }
        });

        cbCST3Low.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST3Low.setText("CST3 LOW");

        cbCST3Empty.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST3Empty.setText("CST3 EMPTY");

        cbCST3Fault.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST3Fault.setText("CST3 FAULT");

        cbCST4OK.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST4OK.setText("CST4 OK");

        cbCST4Low.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST4Low.setText("CST4 LOW");

        cbCST4Empty.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST4Empty.setText("CST4 EMPTY");

        cbCST4Fault.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST4Fault.setText("CST4 FAULT");

        cbCST1NP.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST1NP.setText("CST1 NOT PRESENT");

        cbCST2NP.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST2NP.setText("CST2 NOT PRESENT");

        cbCST3NP.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST3NP.setText("CST3 NOT PRESENT");

        cbCST4NP.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbCST4NP.setText("CST4 NOT PRESENT");

        javax.swing.GroupLayout CSTPanelLayout = new javax.swing.GroupLayout(CSTPanel);
        CSTPanel.setLayout(CSTPanelLayout);
        CSTPanelLayout.setHorizontalGroup(
            CSTPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CSTPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CSTPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CSTPanelLayout.createSequentialGroup()
                        .addComponent(cbCST4OK)
                        .addGap(18, 18, 18)
                        .addComponent(cbCST4Low)
                        .addGap(18, 18, 18)
                        .addComponent(cbCST4Empty)
                        .addGap(18, 18, 18)
                        .addComponent(cbCST4Fault))
                    .addGroup(CSTPanelLayout.createSequentialGroup()
                        .addComponent(cbCST3OK)
                        .addGap(18, 18, 18)
                        .addComponent(cbCST3Low)
                        .addGap(18, 18, 18)
                        .addComponent(cbCST3Empty)
                        .addGap(18, 18, 18)
                        .addComponent(cbCST3Fault))
                    .addGroup(CSTPanelLayout.createSequentialGroup()
                        .addComponent(cbCST2OK)
                        .addGap(18, 18, 18)
                        .addComponent(cbCST2Low)
                        .addGap(18, 18, 18)
                        .addComponent(cbCST2Empty)
                        .addGap(18, 18, 18)
                        .addComponent(cbCST2Fault))
                    .addGroup(CSTPanelLayout.createSequentialGroup()
                        .addComponent(cbCST1OK)
                        .addGap(18, 18, 18)
                        .addComponent(cbCST1Low)
                        .addGap(18, 18, 18)
                        .addComponent(cbCST1Empty)
                        .addGap(18, 18, 18)
                        .addComponent(cbCST1Fault)))
                .addGap(18, 18, 18)
                .addGroup(CSTPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbCST4NP)
                    .addComponent(cbCST3NP)
                    .addComponent(cbCST2NP)
                    .addComponent(cbCST1NP))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        CSTPanelLayout.setVerticalGroup(
            CSTPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CSTPanelLayout.createSequentialGroup()
                .addGroup(CSTPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbCST1OK)
                    .addComponent(cbCST1Low)
                    .addComponent(cbCST1Empty)
                    .addComponent(cbCST1Fault))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CSTPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbCST2OK)
                    .addComponent(cbCST2Low)
                    .addComponent(cbCST2Empty)
                    .addComponent(cbCST2Fault))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CSTPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbCST3OK)
                    .addComponent(cbCST3Low)
                    .addComponent(cbCST3Empty)
                    .addComponent(cbCST3Fault))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CSTPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbCST4OK)
                    .addComponent(cbCST4Low)
                    .addComponent(cbCST4Empty)
                    .addComponent(cbCST4Fault)))
            .addGroup(CSTPanelLayout.createSequentialGroup()
                .addComponent(cbCST1NP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbCST2NP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbCST3NP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbCST4NP))
        );

        CSTDenomPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CASSETTE END CASH", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Century", 0, 10))); // NOI18N

        lblDenom1.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblDenom1.setText("DENOM");

        txtDenomVal1.setEditable(false);
        txtDenomVal1.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N

        lblDenom2.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblDenom2.setText("DENOM");

        txtDenomVal2.setEditable(false);
        txtDenomVal2.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        txtDenomVal2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDenomVal2ActionPerformed(evt);
            }
        });

        lblDenom3.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblDenom3.setText("DENOM");

        txtDenomVal3.setEditable(false);
        txtDenomVal3.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N

        lblDenom4.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblDenom4.setText("DENOM");

        txtDenomVal4.setEditable(false);
        txtDenomVal4.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N

        lblDenom1Val.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblDenom1Val.setText("100");

        lblDenom2Val.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblDenom2Val.setText("500");

        lblDenom3Val.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblDenom3Val.setText("1000");

        lblDenom4Val.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblDenom4Val.setText("5000");

        javax.swing.GroupLayout CSTDenomPanelLayout = new javax.swing.GroupLayout(CSTDenomPanel);
        CSTDenomPanel.setLayout(CSTDenomPanelLayout);
        CSTDenomPanelLayout.setHorizontalGroup(
            CSTDenomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CSTDenomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CSTDenomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CSTDenomPanelLayout.createSequentialGroup()
                        .addComponent(lblDenom1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblDenom1Val))
                    .addGroup(CSTDenomPanelLayout.createSequentialGroup()
                        .addComponent(lblDenom3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblDenom3Val)))
                .addGap(18, 18, 18)
                .addGroup(CSTDenomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDenomVal1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDenomVal3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(CSTDenomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CSTDenomPanelLayout.createSequentialGroup()
                        .addComponent(lblDenom2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDenom2Val))
                    .addGroup(CSTDenomPanelLayout.createSequentialGroup()
                        .addComponent(lblDenom4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDenom4Val)))
                .addGap(18, 18, 18)
                .addGroup(CSTDenomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDenomVal4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDenomVal2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        CSTDenomPanelLayout.setVerticalGroup(
            CSTDenomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CSTDenomPanelLayout.createSequentialGroup()
                .addGroup(CSTDenomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDenom1)
                    .addComponent(txtDenomVal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDenom2)
                    .addComponent(txtDenomVal2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDenom1Val)
                    .addComponent(lblDenom2Val))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CSTDenomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDenom3)
                    .addComponent(txtDenomVal3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDenom4)
                    .addComponent(txtDenomVal4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDenom3Val)
                    .addComponent(lblDenom4Val)))
        );

        StatPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "STAT", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Century", 0, 10))); // NOI18N

        cbStatUp.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbStatUp.setText("UP");

        cbStatDown.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbStatDown.setText("DOWN");

        javax.swing.GroupLayout StatPanelLayout = new javax.swing.GroupLayout(StatPanel);
        StatPanel.setLayout(StatPanelLayout);
        StatPanelLayout.setHorizontalGroup(
            StatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbStatUp)
                .addGap(18, 18, 18)
                .addComponent(cbStatDown)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        StatPanelLayout.setVerticalGroup(
            StatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cbStatUp)
                .addComponent(cbStatDown))
        );

        DispenserPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DISPENSER", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Century", 0, 10))); // NOI18N

        cbDispenserOk.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbDispenserOk.setText("OK");

        cbDispenserFault.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbDispenserFault.setText("FAULT");

        cbDispenserMoneyLeft.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbDispenserMoneyLeft.setText("MONEY LEFT");

        javax.swing.GroupLayout DispenserPanelLayout = new javax.swing.GroupLayout(DispenserPanel);
        DispenserPanel.setLayout(DispenserPanelLayout);
        DispenserPanelLayout.setHorizontalGroup(
            DispenserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DispenserPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbDispenserOk)
                .addGap(18, 18, 18)
                .addComponent(cbDispenserFault)
                .addGap(18, 18, 18)
                .addComponent(cbDispenserMoneyLeft)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DispenserPanelLayout.setVerticalGroup(
            DispenserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DispenserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cbDispenserOk)
                .addComponent(cbDispenserFault)
                .addComponent(cbDispenserMoneyLeft))
        );

        StatePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "STATE", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Century", 0, 10))); // NOI18N

        cbStateOpen.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbStateOpen.setText("OPEN");

        cbStateClose.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbStateClose.setText("CLOSE");

        btnATMStatus.setBackground(new java.awt.Color(51, 153, 255));
        btnATMStatus.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        btnATMStatus.setText("UPDATE STATUS");
        btnATMStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnATMStatusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout StatePanelLayout = new javax.swing.GroupLayout(StatePanel);
        StatePanel.setLayout(StatePanelLayout);
        StatePanelLayout.setHorizontalGroup(
            StatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbStateOpen)
                .addGap(18, 18, 18)
                .addComponent(cbStateClose)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnATMStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        StatePanelLayout.setVerticalGroup(
            StatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cbStateOpen)
                .addComponent(cbStateClose)
                .addComponent(btnATMStatus))
        );

        PinPadDenomPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PIN PAD", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Century", 0, 10))); // NOI18N

        cbPinPadOk.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbPinPadOk.setText("OK");

        cbPinPadFault.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        cbPinPadFault.setText("FAULT");

        javax.swing.GroupLayout PinPadDenomPanelLayout = new javax.swing.GroupLayout(PinPadDenomPanel);
        PinPadDenomPanel.setLayout(PinPadDenomPanelLayout);
        PinPadDenomPanelLayout.setHorizontalGroup(
            PinPadDenomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PinPadDenomPanelLayout.createSequentialGroup()
                .addComponent(cbPinPadOk)
                .addGap(18, 18, 18)
                .addComponent(cbPinPadFault)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PinPadDenomPanelLayout.setVerticalGroup(
            PinPadDenomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PinPadDenomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cbPinPadOk)
                .addComponent(cbPinPadFault))
        );

        CSTOtherPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "OTHER OPERATIONS", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Century", 0, 10))); // NOI18N

        lblDenom5.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblDenom5.setText("DENOM");

        txtDenom1Val.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N

        lblDenom6.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblDenom6.setText("DENOM");

        txtDenom2Val.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        txtDenom2Val.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDenom2ValActionPerformed(evt);
            }
        });

        lblDenom7.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblDenom7.setText("DENOM");

        txtDenom3Val.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N

        lblDenom8.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblDenom8.setText("DENOM");

        txtDenom4Val.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N

        btnWithdrawal.setBackground(new java.awt.Color(255, 102, 102));
        btnWithdrawal.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        btnWithdrawal.setText("WITHDRAWAL");
        btnWithdrawal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWithdrawalActionPerformed(evt);
            }
        });

        txtWdAmount.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        txtWdAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtWdAmountActionPerformed(evt);
            }
        });

        lblDenom1Val1.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblDenom1Val1.setText("100");

        lblDenom2Val1.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblDenom2Val1.setText("500");

        lblDenom3Val1.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblDenom3Val1.setText("1000");

        lblDenom4Val1.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblDenom4Val1.setText("5000");

        btnLoad.setBackground(new java.awt.Color(255, 255, 102));
        btnLoad.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        btnLoad.setText("CASH LOADING");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });

        lblDenom9.setFont(new java.awt.Font("Century", 0, 10)); // NOI18N
        lblDenom9.setText("WD AMOUNT");

        javax.swing.GroupLayout CSTOtherPanelLayout = new javax.swing.GroupLayout(CSTOtherPanel);
        CSTOtherPanel.setLayout(CSTOtherPanelLayout);
        CSTOtherPanelLayout.setHorizontalGroup(
            CSTOtherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CSTOtherPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CSTOtherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CSTOtherPanelLayout.createSequentialGroup()
                        .addComponent(lblDenom5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblDenom1Val1))
                    .addGroup(CSTOtherPanelLayout.createSequentialGroup()
                        .addComponent(lblDenom7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblDenom3Val1)))
                .addGap(18, 18, 18)
                .addGroup(CSTOtherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDenom1Val, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDenom3Val, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(CSTOtherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CSTOtherPanelLayout.createSequentialGroup()
                        .addComponent(lblDenom6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDenom2Val1))
                    .addGroup(CSTOtherPanelLayout.createSequentialGroup()
                        .addComponent(lblDenom8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDenom4Val1)))
                .addGap(18, 18, 18)
                .addGroup(CSTOtherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CSTOtherPanelLayout.createSequentialGroup()
                        .addComponent(txtDenom2Val, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblDenom9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtWdAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))
                    .addGroup(CSTOtherPanelLayout.createSequentialGroup()
                        .addComponent(txtDenom4Val, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(167, 167, 167)))
                .addGroup(CSTOtherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnWithdrawal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLoad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        CSTOtherPanelLayout.setVerticalGroup(
            CSTOtherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CSTOtherPanelLayout.createSequentialGroup()
                .addGroup(CSTOtherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDenom5)
                    .addComponent(txtDenom1Val, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDenom6)
                    .addComponent(txtDenom2Val, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnWithdrawal)
                    .addComponent(txtWdAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDenom1Val1)
                    .addComponent(lblDenom2Val1)
                    .addComponent(lblDenom9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CSTOtherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDenom7)
                    .addComponent(txtDenom3Val, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDenom8)
                    .addComponent(txtDenom4Val, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDenom3Val1)
                    .addComponent(lblDenom4Val1)
                    .addComponent(btnLoad)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelATMInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(CRPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(RPPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(CSTDenomPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(StatPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(StatePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PinPadDenomPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(CSTOtherPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(CSTPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE))
            .addComponent(DispenserPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PanelATMInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CRPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PinPadDenomPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RPPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(118, 118, 118))
                    .addComponent(CSTPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DispenserPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StatPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StatePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CSTDenomPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CSTOtherPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cbCST3OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCST3OKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbCST3OKActionPerformed

    private void txtDenomVal2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDenomVal2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDenomVal2ActionPerformed

    private void txtATMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtATMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtATMActionPerformed

    private void btnATMStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnATMStatusActionPerformed
        // TODO add your handling code here:

        try {
            Other other = new Other();
            String atm = txtATM.getText();
            String location = txtLocation.getText();
            int protocol = other.getProtocol();
            String div = other.getDiv();
            sqlop sql = new sqlop();
            DialogBox dg = new DialogBox();

            int cst1 = 0;
            int cst2 = 0;
            int cst3 = 0;
            int cst4 = 0;
            int rcpt = 0;
            int pinPad = 0;
            int drawer = 0;
            int card = 0;
            int stat = 0;
            int state = 0;

            int supervisor = 0;

            String err = "";
            String sep = " ";
            String errSum = "";

            int fltlvl = 0;
            int priority = 0;
            int cat_flt_lvl = 0;

            //CST1
            if (cbCST1OK.isSelected()) {
                cst1 = 0;
            }
            if (cbCST1Low.isSelected()) {
                cst1 = 1;
            }
            if (cbCST1Empty.isSelected()) {
                cst1 = 2;
            }
            if (cbCST1Fault.isSelected()) {
                cst1 = 9;
            }
            if (cbCST1NP.isSelected()) {
                cst1 = 5;
            }

            //CST2
            if (cbCST2OK.isSelected()) {
                cst2 = 0;
            }
            if (cbCST2Low.isSelected()) {
                cst2 = 1;
            }
            if (cbCST2Empty.isSelected()) {
                cst2 = 2;
            }
            if (cbCST2Fault.isSelected()) {
                cst2 = 9;
            }
            if (cbCST2NP.isSelected()) {
                cst2 = 5;
            }
            //CST3
            if (cbCST3OK.isSelected()) {
                cst3 = 0;
            }
            if (cbCST3Low.isSelected()) {
                cst3 = 1;
            }
            if (cbCST3Empty.isSelected()) {
                cst3 = 2;
            }
            if (cbCST3Fault.isSelected()) {
                cst3 = 9;
            }
            if (cbCST3NP.isSelected()) {
                cst3 = 5;
            }
            //CST4
            if (cbCST4OK.isSelected()) {
                cst4 = 0;
            }
            if (cbCST4Low.isSelected()) {
                cst4 = 1;
            }
            if (cbCST4Empty.isSelected()) {
                cst4 = 2;
            }
            if (cbCST4Fault.isSelected()) {
                cst4 = 9;
            }
            if (cbCST4NP.isSelected()) {
                cst4 = 5;
            }
            //Receipt Printer
            if (cbRPOK.isSelected()) {
                rcpt = 0;
            }
            if (cbRPLow.isSelected()) {
                rcpt = 1;
            }
            if (cbRPEmpty.isSelected()) {
                rcpt = 2;
            }
            if (cbRPFault.isSelected()) {
                rcpt = 3;
            }
            //Pin Pad
            if (cbPinPadOk.isSelected()) {
                pinPad = 0;
            }
            if (cbPinPadFault.isSelected()) {
                pinPad = 1;
            }
            //Drawer
            if (cbDispenserOk.isSelected()) {
                drawer = 0;
            }
            if (cbDispenserFault.isSelected()) {
                drawer = 1;
            }
            if (cbDispenserMoneyLeft.isSelected()) {
                drawer = 2;
            }
            //Card Reader
            if (cbCROK.isSelected()) {
                card = 0;
            }
            if (cbCRFault.isSelected()) {
                card = 1;
            }
            //Stat
            if (cbStatUp.isSelected()) {
                stat = 1;
            }
            if (cbStatDown.isSelected()) {
                stat = 0;
            }
            //State
            if (cbStateOpen.isSelected()) {
                state = 1;
            }
            if (cbStateClose.isSelected()) {
                state = 0;
            }

            //calculate flt lvl and priority
            if (cst1 == 2 || cst1 == 9) {
                cat_flt_lvl++;
            }
            if (cst2 == 2 || cst2 == 9) {
                cat_flt_lvl++;
            }
            if (cst3 == 2 || cst3 == 9) {
                cat_flt_lvl++;
            }
            if (cst4 == 2 || cst4 == 9) {
                cat_flt_lvl++;
            }
            //If three or more cassetets empty
            if (cat_flt_lvl >= 3) {
                priority++;
            }
            if (cst1 != 0 && cst1 != 5) {
                fltlvl++;
            }
            if (cst2 != 0 && cst2 != 5) {
                fltlvl++;
            }
            if (cst3 != 0 && cst3 != 5) {
                fltlvl++;
            }
            if (cst4 != 0 && cst4 != 5) {
                fltlvl++;
            }
            if (rcpt == 1 || rcpt == 2 || rcpt == 3) {
                fltlvl++;
            }
            if (pinPad == 1) {
                priority++;
                fltlvl++;
            }
            if (drawer == 1 || drawer == 2) {
                priority++;
                fltlvl++;
            }
            if (card != 0) {
                fltlvl++;
            }
            if (stat == 0) {
                priority++;
                fltlvl++;
            }
            if (state == 0) {
                fltlvl++;
            }

            err = cst1 + sep + cst2 + sep + cst3 + sep + cst4 + sep + rcpt + sep + pinPad
                    + sep + drawer + sep + card + sep + stat + sep + state;

            errSum = cst1 + sep + cst2 + sep + cst3 + sep + cst4 + sep + stat + sep + state
                    + sep + rcpt + sep + supervisor + sep + pinPad + sep + drawer + sep + card;

            String txt = atm + "#" + err + "#" + fltlvl + "#" + priority;

            int up1 = sql.updateATMStatus(txt);
            int up2 = sql.updateATMStatusSummary(atm, errSum);
            if (up1 > 0 || up2 > 0) {
                dg.dialogBox("Details Updated...", this);
                if (fltlvl > 0) {
                    int up = sql.insertSMS(atm, err, protocol, location, div);
                    if (up > 0) {
                        dg.dialogBox("SMS Sent...", this);
                    } else {
                        dg.dialogBox("SMS Not Sent...", this);
                    }
                } else if (fltlvl == 0) { //all errors clear
                    int up = sql.insertSMSFltLvl0(atm, err, location);
                    if (up > 0) {
                        dg.dialogBox("SMS Sent...", this);
                    } else {
                        dg.dialogBox("SMS Not Sent...", this);
                    }
                }
//                JOptionPane.showMessageDialog(null, "Updated", "Error", JOptionPane.DEFAULT_OPTION);
            } else {
                JOptionPane.showMessageDialog(null, "Error", "Error", JOptionPane.ERROR_MESSAGE);
            }
//            
//            if(up>0){
//                JOptionPane.showMessageDialog(null, "Updated", "Error", JOptionPane.DEFAULT_OPTION);
//            }else{
//                JOptionPane.showMessageDialog(null, "Error", "Error", JOptionPane.ERROR_MESSAGE);
//            }

//            System.out.println(atm + "#" + err + "#" + fltlvl + "#" + priority);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnATMStatusActionPerformed

    private void txtDenom2ValActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDenom2ValActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDenom2ValActionPerformed

    private void btnWithdrawalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWithdrawalActionPerformed
        // TODO add your handling code here:
        try {
            Other other = new Other();
            DialogBox dg = new DialogBox();
            sqlop sql = new sqlop();

            String atm = txtATM.getText();
            String txtVal = txtWdAmount.getText().toString();

            if (txtVal.length() > 0 && other.isInteger(txtVal)) {
                int wdAmount = Integer.parseInt(txtVal);

                if (wdAmount % 100 == 0) {
                    int denom1 = Integer.parseInt(lblDenom1Val.getText().trim());
                    int denom2 = Integer.parseInt(lblDenom2Val.getText().trim());
                    int denom3 = Integer.parseInt(lblDenom3Val.getText().trim());
                    int denom4 = Integer.parseInt(lblDenom4Val.getText().trim());

                    int denom1Val = Integer.parseInt(txtDenomVal1.getText().trim());
                    int denom2Val = Integer.parseInt(txtDenomVal2.getText().trim());
                    int denom3Val = Integer.parseInt(txtDenomVal3.getText().trim());
                    int denom4Val = Integer.parseInt(txtDenomVal4.getText().trim());

                    int denom1Dispense = 0;
                    int denom2Dispense = 0;
                    int denom3Dispense = 0;
                    int denom4Dispense = 0;

                    if (denom4Val > 0) {
                        if ((wdAmount / denom4) <= denom4Val) {
                            denom4Dispense = (wdAmount / denom4);
                            denom4Val -= (wdAmount / denom4);
                            wdAmount -= (denom4 * (wdAmount / denom4));
                        }
                    }
                    if (denom3Val > 0) {
                        if ((wdAmount / denom3) <= denom3Val) {
                            denom3Dispense = (wdAmount / denom3);
                            denom3Val -= (wdAmount / denom3);
                            wdAmount -= (denom3 * (wdAmount / denom3));
                        }
                    }
                    if (denom2Val > 0) {
                        if ((wdAmount / denom2) <= denom2Val) {
                            denom2Dispense = (wdAmount / denom2);
                            denom2Val -= (wdAmount / denom2);
                            wdAmount -= (denom2 * (wdAmount / denom2));
                        }
                    }
                    if (denom1Val > 0) {
                        if ((wdAmount / denom1) <= denom1Val) {
                            denom1Dispense = (wdAmount / denom1);
                            denom1Val -= (wdAmount / denom1);
                            wdAmount -= (denom1 * (wdAmount / denom1));
                        }
                    }

//                    System.out.println(wdAmount);
//                    System.out.println(denom4Val + "~" + denom4Dispense);
//                    System.out.println(denom3Val + "~" + denom3Dispense);
//                    System.out.println(denom2Val + "~" + denom2Dispense);
//                    System.out.println(denom1Val + "~" + denom1Dispense);
                    if (wdAmount > 0) {
                        dg.dialogBox("Try another amount...", this);
                    } else {
                        txtDenomVal1.setText(Integer.toString(denom1Val));
                        txtDenomVal2.setText(Integer.toString(denom2Val));
                        txtDenomVal3.setText(Integer.toString(denom3Val));
                        txtDenomVal4.setText(Integer.toString(denom4Val));

                        String outCash = other.getOutCash();
                        int newOutCash1 = Integer.parseInt(outCash.split("~")[0]) + denom1Dispense;
                        int newOutCash2 = Integer.parseInt(outCash.split("~")[1]) + denom2Dispense;
                        int newOutCash3 = Integer.parseInt(outCash.split("~")[2]) + denom3Dispense;
                        int newOutCash4 = Integer.parseInt(outCash.split("~")[3]) + denom4Dispense;

                        String txtEndCash = denom1Val + "~" + denom2Val + "~" + denom3Val + "~" + denom4Val;
                        String txtCashOut = newOutCash1 + "~" + newOutCash2 + "~" + newOutCash3 + "~" + newOutCash4;
                        other.setOutCash(txtCashOut);

//                        System.out.println(txtCashOut);
//                        System.out.println(txtEndCash);
                        if (sql.updateATMCashOut(atm, txtCashOut, txtEndCash) > 0) {
                            dg.dialogBox("Amount Updated...", this);
                        } else {
                            JOptionPane.showMessageDialog(null, "Error in updating", "Error", JOptionPane.ERROR_MESSAGE);
                        }

                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Enter amount in multiple of 100", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Amount", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnWithdrawalActionPerformed

    private void txtWdAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtWdAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtWdAmountActionPerformed

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed
        // TODO add your handling code here:
        try {
            Other other = new Other();
            DialogBox dg = new DialogBox();
            sqlop sql = new sqlop();
            String atm = txtATM.getText();

            int denom1Val = 0;
            int denom2Val = 0;
            int denom3Val = 0;
            int denom4Val = 0;

            if (other.isInteger(txtDenom1Val.getText().trim())) {
                denom1Val = Integer.parseInt(txtDenom1Val.getText().trim());
            }
            if (other.isInteger(txtDenom2Val.getText().trim())) {
                denom2Val = Integer.parseInt(txtDenom2Val.getText().trim());
            }
            if (other.isInteger(txtDenom3Val.getText().trim())) {
                denom3Val = Integer.parseInt(txtDenom3Val.getText().trim());
            }
            if (other.isInteger(txtDenom4Val.getText().trim())) {
                denom4Val = Integer.parseInt(txtDenom4Val.getText().trim());
            }

            String loadDenom = atm + "#" + denom1Val + "~" + denom2Val + "~" + denom3Val + "~" + denom4Val;

            if (sql.cashLoading(loadDenom) > 0) {
                dg.dialogBox("Cash Loading Completed...", this);
                txtDenomVal1.setText(Integer.toString(denom1Val));
                txtDenomVal2.setText(Integer.toString(denom2Val));
                txtDenomVal3.setText(Integer.toString(denom3Val));
                txtDenomVal4.setText(Integer.toString(denom4Val));
                other.setOutCash("0~0~0~0");
                other.setIncCash("0~0~0~0");
            } else {
                JOptionPane.showMessageDialog(null, "Error in updating", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnLoadActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ATM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ATM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ATM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ATM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ATM().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup BGCR;
    private javax.swing.ButtonGroup BGCST1;
    private javax.swing.ButtonGroup BGCST2;
    private javax.swing.ButtonGroup BGCST3;
    private javax.swing.ButtonGroup BGCST4;
    private javax.swing.ButtonGroup BGDISPENSER;
    private javax.swing.ButtonGroup BGPINPAD;
    private javax.swing.ButtonGroup BGRP;
    private javax.swing.ButtonGroup BGSTAT;
    private javax.swing.ButtonGroup BGSTATE;
    private javax.swing.JPanel CRPanel;
    private javax.swing.JPanel CSTDenomPanel;
    private javax.swing.JPanel CSTOtherPanel;
    private javax.swing.JPanel CSTPanel;
    private javax.swing.JPanel DispenserPanel;
    private javax.swing.JPanel PanelATMInfo;
    private javax.swing.JPanel PinPadDenomPanel;
    private javax.swing.JPanel RPPanel;
    private javax.swing.JPanel StatPanel;
    private javax.swing.JPanel StatePanel;
    private javax.swing.JButton btnATMStatus;
    private javax.swing.JButton btnLoad;
    private javax.swing.JButton btnWithdrawal;
    private javax.swing.JCheckBox cbCRFault;
    private javax.swing.JCheckBox cbCROK;
    private javax.swing.JCheckBox cbCST1Empty;
    private javax.swing.JCheckBox cbCST1Fault;
    private javax.swing.JCheckBox cbCST1Low;
    private javax.swing.JCheckBox cbCST1NP;
    private javax.swing.JCheckBox cbCST1OK;
    private javax.swing.JCheckBox cbCST2Empty;
    private javax.swing.JCheckBox cbCST2Fault;
    private javax.swing.JCheckBox cbCST2Low;
    private javax.swing.JCheckBox cbCST2NP;
    private javax.swing.JCheckBox cbCST2OK;
    private javax.swing.JCheckBox cbCST3Empty;
    private javax.swing.JCheckBox cbCST3Fault;
    private javax.swing.JCheckBox cbCST3Low;
    private javax.swing.JCheckBox cbCST3NP;
    private javax.swing.JCheckBox cbCST3OK;
    private javax.swing.JCheckBox cbCST4Empty;
    private javax.swing.JCheckBox cbCST4Fault;
    private javax.swing.JCheckBox cbCST4Low;
    private javax.swing.JCheckBox cbCST4NP;
    private javax.swing.JCheckBox cbCST4OK;
    private javax.swing.JCheckBox cbDispenserFault;
    private javax.swing.JCheckBox cbDispenserMoneyLeft;
    private javax.swing.JCheckBox cbDispenserOk;
    private javax.swing.JCheckBox cbPinPadFault;
    private javax.swing.JCheckBox cbPinPadOk;
    private javax.swing.JCheckBox cbRPEmpty;
    private javax.swing.JCheckBox cbRPFault;
    private javax.swing.JCheckBox cbRPLow;
    private javax.swing.JCheckBox cbRPOK;
    private javax.swing.JCheckBox cbStatDown;
    private javax.swing.JCheckBox cbStatUp;
    private javax.swing.JCheckBox cbStateClose;
    private javax.swing.JCheckBox cbStateOpen;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblATM;
    private javax.swing.JLabel lblDenom1;
    private javax.swing.JLabel lblDenom1Val;
    private javax.swing.JLabel lblDenom1Val1;
    private javax.swing.JLabel lblDenom2;
    private javax.swing.JLabel lblDenom2Val;
    private javax.swing.JLabel lblDenom2Val1;
    private javax.swing.JLabel lblDenom3;
    private javax.swing.JLabel lblDenom3Val;
    private javax.swing.JLabel lblDenom3Val1;
    private javax.swing.JLabel lblDenom4;
    private javax.swing.JLabel lblDenom4Val;
    private javax.swing.JLabel lblDenom4Val1;
    private javax.swing.JLabel lblDenom5;
    private javax.swing.JLabel lblDenom6;
    private javax.swing.JLabel lblDenom7;
    private javax.swing.JLabel lblDenom8;
    private javax.swing.JLabel lblDenom9;
    private javax.swing.JLabel lblLocation;
    private javax.swing.JTextField txtATM;
    private javax.swing.JTextField txtDenom1Val;
    private javax.swing.JTextField txtDenom2Val;
    private javax.swing.JTextField txtDenom3Val;
    private javax.swing.JTextField txtDenom4Val;
    private javax.swing.JTextField txtDenomVal1;
    private javax.swing.JTextField txtDenomVal2;
    private javax.swing.JTextField txtDenomVal3;
    private javax.swing.JTextField txtDenomVal4;
    private javax.swing.JTextField txtLocation;
    private javax.swing.JTextField txtWdAmount;
    // End of variables declaration//GEN-END:variables
}
