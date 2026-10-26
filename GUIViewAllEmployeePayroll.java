package com.mycompany.cp2ms1grp10;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;

public class GUIViewAllEmployeePayroll extends JPanel {

    private final GUIMainFrame frame;

    private final JPanel payrollArea = new JPanel();

    public GUIViewAllEmployeePayroll(GUIMainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(GUIMainFrame.COLOR_BG);
        build();
    }

    private void build() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(GUIMainFrame.COLOR_PRIMARY);
        header.setBorder(new EmptyBorder(15,25,15,25));

        JLabel title = new JLabel("All Employee Payroll");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));

        header.add(title, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        payrollArea.setLayout(new BoxLayout(payrollArea, BoxLayout.Y_AXIS));
        payrollArea.setBackground(GUIMainFrame.COLOR_BG);
        payrollArea.setBorder(new EmptyBorder(20,20,20,20));

        JScrollPane scrollPane = new JScrollPane(payrollArea);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        JButton backBtn = GUIMainFrame.makeButton("Back", GUIMainFrame.COLOR_PRIMARY);
        backBtn.addActionListener(e -> frame.displayPanel("PayrollMenu"));

        JPanel bottom = new JPanel();
        bottom.setBackground(GUIMainFrame.COLOR_BG);
        bottom.setBorder(new EmptyBorder(10,0,15,0));
        bottom.add(backBtn);

        add(bottom, BorderLayout.SOUTH);
    }

    public void displayAllPayroll() {

        payrollArea.removeAll();

        PayrollCalculation pc = new PayrollCalculation();

        DecimalFormat money = new DecimalFormat("₱#,##0.00");

        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        for(Employee emp : DataManagement.employees){

            JPanel employeeCard = new JPanel();
            employeeCard.setLayout(new BoxLayout(employeeCard, BoxLayout.Y_AXIS));
            employeeCard.setBackground(GUIMainFrame.COLOR_PRIMARY);
            employeeCard.setBorder(new EmptyBorder(20,20,20,20));
            employeeCard.setMaximumSize(new Dimension(Integer.MAX_VALUE,130));

            JLabel name = new JLabel(emp.getFirstName() + " " + emp.getLastName());
            name.setAlignmentX(Component.CENTER_ALIGNMENT);
            name.setForeground(Color.WHITE);
            name.setFont(new Font("SansSerif",Font.BOLD,24));

            JLabel position = new JLabel(emp.getPosition());
            position.setAlignmentX(Component.CENTER_ALIGNMENT);
            position.setForeground(Color.WHITE);
            position.setFont(new Font("SansSerif",Font.PLAIN,16));

            JLabel employeeNumber = new JLabel("Employee # " + emp.getEmployeeNumber());
            employeeNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
            employeeNumber.setForeground(Color.WHITE);
            employeeNumber.setFont(new Font("SansSerif",Font.PLAIN,14));

            employeeCard.add(name);
            employeeCard.add(Box.createVerticalStrut(8));
            employeeCard.add(position);
            employeeCard.add(Box.createVerticalStrut(5));
            employeeCard.add(employeeNumber);

            payrollArea.add(employeeCard);
            payrollArea.add(Box.createVerticalStrut(20));

            for(int month = 1; month <= 12; month++){

                Payroll p = pc.computePayrollByMonth(
                        emp,
                        DataManagement.attendance,
                        month
                );

                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(222, 228, 236)),
                        new EmptyBorder(15,20,15,20)
                ));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE,280));

                JLabel monthLabel = new JLabel(months[month - 1]);
                monthLabel.setFont(new Font("SansSerif",Font.BOLD,20));

                JPanel top = new JPanel(new BorderLayout());
                top.setOpaque(false);
                top.add(monthLabel,BorderLayout.WEST);

                card.add(top,BorderLayout.NORTH);

                if(p.getHoursWorked() > 0){

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
                    netTitle.setFont(new Font("SansSerif",Font.BOLD,16));

                    JLabel net = new JLabel(money.format(p.getNet()));
                    net.setForeground(new Color(22, 128, 93));
                    net.setFont(new Font("SansSerif",Font.BOLD,18));

                    info.add(netTitle);
                    info.add(net);

                    card.add(info,BorderLayout.CENTER);

                }else{

                    JPanel unavailable = new JPanel(new BorderLayout());
                    unavailable.setBackground(new Color(255, 247, 237));
                    unavailable.setBorder(new EmptyBorder(25,20,25,20));

                    JLabel warning = new JLabel("Payroll Unavailable");
                    warning.setForeground(new Color(180, 83, 9));
                    warning.setFont(new Font("SansSerif",Font.BOLD,18));

                    JLabel details = new JLabel(
                            "<html>Missing attendance data.<br>Please check the CSV.</html>"
                    );

                    details.setFont(new Font("SansSerif",Font.PLAIN,14));

                    unavailable.add(warning,BorderLayout.NORTH);
                    unavailable.add(details,BorderLayout.CENTER);

                    card.add(unavailable,BorderLayout.CENTER);

                }

                payrollArea.add(card);
                payrollArea.add(Box.createVerticalStrut(18));

            }
            
            payrollArea.add(Box.createVerticalStrut(35));

            JSeparator separator = new JSeparator();
            separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
            separator.setForeground(new Color(203, 213, 225));
            payrollArea.add(separator);

            payrollArea.add(Box.createVerticalStrut(35));

        }

        payrollArea.revalidate();
        payrollArea.repaint();

        JLabel message = new JLabel(
                "<html><center>"
                + "<h2 style='color:#16805D;'>Payroll Generated Successfully</h2>"
                + "<p>Payroll Reports for all Employees have been Generated.</p>"
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