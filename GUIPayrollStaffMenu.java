package com.mycompany.cp2ms1grp10;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GUIPayrollStaffMenu extends JPanel {

    private final GUIMainFrame frame;

    public GUIPayrollStaffMenu(GUIMainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(GUIMainFrame.COLOR_BG);
        build();
    }

    private void build() {
        add(GUIMainFrame.makeHeaderPanel("Payroll Staff Portal"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(GUIMainFrame.COLOR_BG);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 228, 236)),
                new EmptyBorder(40, 60, 40, 60)));

        JLabel titleLbl = new JLabel("Payroll Staff Menu");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLbl.setForeground(GUIMainFrame.COLOR_PRIMARY);
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLbl = new JLabel("What would you like to do?");
        subLbl.setFont(GUIMainFrame.FONT_LABEL);
        subLbl.setForeground(new Color(100, 116, 139));
        subLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(new Color(222, 228, 236));

        JButton viewAllBtn    = GUIMainFrame.makeButton("View All Employee Payroll",   GUIMainFrame.COLOR_PRIMARY);
        JButton viewOneBtn    = GUIMainFrame.makeButton("View One Employee Payroll",   GUIMainFrame.COLOR_ACCENT);
        JButton manageBtn     = GUIMainFrame.makeButton("Manage Employee Records",     GUIMainFrame.COLOR_PRIMARY);
        JButton attendanceBtn = GUIMainFrame.makeButton("Manage Attendance Records",   GUIMainFrame.COLOR_ACCENT);
        JButton logoutBtn     = GUIMainFrame.makeButton("Logout",                      new Color(71, 85, 105));

        styleMenuButton(viewAllBtn);
        styleMenuButton(viewOneBtn);
        styleMenuButton(manageBtn);
        styleMenuButton(attendanceBtn);
        styleMenuButton(logoutBtn);

        viewAllBtn.addActionListener(e    -> frame.showViewAllPayroll());
        viewOneBtn.addActionListener(e    -> frame.showStaffSearchEmployee());
        manageBtn.addActionListener(e     -> frame.showEmployeeManagement());
        attendanceBtn.addActionListener(e -> frame.showAttendanceManagement());
        logoutBtn.addActionListener(e     -> frame.logout());

        card.add(titleLbl);
        card.add(Box.createVerticalStrut(4));
        card.add(subLbl);
        card.add(Box.createVerticalStrut(24));
        card.add(sep);
        card.add(Box.createVerticalStrut(24));
        card.add(viewAllBtn);
        card.add(Box.createVerticalStrut(12));
        card.add(viewOneBtn);
        card.add(Box.createVerticalStrut(12));
        card.add(manageBtn);
        card.add(Box.createVerticalStrut(12));
        card.add(attendanceBtn);
        card.add(Box.createVerticalStrut(12));
        card.add(logoutBtn);

        center.add(card);
        add(center, BorderLayout.CENTER);
    }

    private void styleMenuButton(JButton btn) {
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(320, 44));
        btn.setPreferredSize(new Dimension(300, 44));
    }
}
