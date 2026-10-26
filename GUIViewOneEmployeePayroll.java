package com.mycompany.cp2ms1grp10;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;

public class GUIViewOneEmployeePayroll extends JPanel {

    private final GUIMainFrame frame;

    private JPanel headerPanel;
    private JLabel headerLabel;

    private final JPanel payrollArea = new JPanel();

    public GUIViewOneEmployeePayroll(GUIMainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(GUIMainFrame.COLOR_BG);
        build();
    }

    private void build() {

        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(GUIMainFrame.COLOR_PRIMARY);
        headerPanel.setBorder(new EmptyBorder(15, 25, 15, 25));

        headerLabel = new JLabel("My Payroll");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 22));

        headerPanel.add(headerLabel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        payrollArea.setLayout(new BoxLayout(payrollArea, BoxLayout.Y_AXIS));
        payrollArea.setBackground(GUIMainFrame.COLOR_BG);
        payrollArea.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(payrollArea);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        JButton backBtn = GUIMainFrame.makeButton("Back", GUIMainFrame.COLOR_PRIMARY);
        backBtn.addActionListener(e -> frame.goBackFromPayroll());

        JPanel bottom = new JPanel();
        bottom.setBackground(GUIMainFrame.COLOR_BG);
        bottom.setBorder(new EmptyBorder(10, 0, 15, 0));
        bottom.add(backBtn);

        add(bottom, BorderLayout.SOUTH);
    }

    public void displayPayroll(Employee emp) {

        payrollArea.removeAll();

        headerLabel.setText(emp.getFirstName() + "'s Payroll");

        PayrollCalculation pc = new PayrollCalculation();

        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        DecimalFormat money = new DecimalFormat("₱#,##0.00");

        for (int month = 1; month <= 12; month++) {

            Payroll p = pc.computePayrollByMonth(emp, DataManagement.attendance, month);

            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(222, 228, 236)),
                    new EmptyBorder(15,20,15,20)
            ));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.setOpaque(false);

            JLabel monthLabel = new JLabel(months[month - 1]);
            monthLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

            titlePanel.add(monthLabel, BorderLayout.WEST);

            card.add(titlePanel, BorderLayout.NORTH);

            if (p.getHoursWorked() > 0) {

                JPanel info = new JPanel(new GridLayout(7,2,10,10));
                info.setOpaque(false);
                info.setBorder(new EmptyBorder(20,0,0,0));

                info.add(new JLabel("Hours Worked"));
                info.add(new JLabel(String.format("%.2f hrs", p.getHoursWorked())));

                info.add(new JLabel("Gross Pay"));
                info.add(new JLabel(money.format(p.getGross())));

                info.add(new JLabel("SSS"));
                info.add(new JLabel(money.format(p.getSSS())));

                info.add(new JLabel("PhilHealth"));
                info.add(new JLabel(money.format(p.getPhilhealth())));

                info.add(new JLabel("Pag-IBIG"));
                info.add(new JLabel(money.format(p.getPagibig())));

                info.add(new JLabel("Withholding Tax"));
                info.add(new JLabel(money.format(p.getWithholding())));

                JLabel netTitle = new JLabel("Net Pay");
                netTitle.setFont(new Font("SansSerif", Font.BOLD,16));

                JLabel netValue = new JLabel(money.format(p.getNet()));
                netValue.setFont(new Font("SansSerif", Font.BOLD,18));
                netValue.setForeground(new Color(22, 128, 93));

                info.add(netTitle);
                info.add(netValue);

                card.add(info, BorderLayout.CENTER);

            } else {

                JPanel unavailable = new JPanel(new BorderLayout());
                unavailable.setBackground(new Color(255, 247, 237));
                unavailable.setBorder(new EmptyBorder(25,20,25,20));

                JLabel warning = new JLabel("Payroll Unavailable");
                warning.setFont(new Font("SansSerif", Font.BOLD,18));
                warning.setForeground(new Color(180, 83, 9));

                JLabel details;

                if(frame.getCurrentRole().equals("employee")){

                    details = new JLabel(
                            "<html>This month is unavailable.<br>Contact the payroll department.</html>"
                    );

                }else{

                    details = new JLabel(
                            "<html>Missing attendance data.<br>Please check the CSV.</html>"
                    );

                }

                details.setFont(new Font("SansSerif",Font.PLAIN,14));

                unavailable.add(warning,BorderLayout.NORTH);
                unavailable.add(details,BorderLayout.CENTER);

                card.add(unavailable,BorderLayout.CENTER);

            }

            payrollArea.add(card);
            payrollArea.add(Box.createVerticalStrut(18));
        }
        

        payrollArea.revalidate();
        payrollArea.repaint();

        JLabel message = new JLabel(
                "<html><center>"
                + "<h2 style='color:#16805D;'>Payroll Generated Successfully</h2>"
                + "<p>Payroll Report for Employee #"
                + emp.getEmployeeNumber()
                + " has been Generated."
                + "</center></html>"
        );

        message.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JOptionPane.showMessageDialog(
                this,
                message,
                "MotorPH Payroll System",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}