/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cp2ms1grp10;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 *
 * @author Josh
 */
public class GUIEmployeeMenu extends JPanel {

    private final GUIMainFrame frame;
    private final JLabel       welcomeLabel = new JLabel("Welcome");
    private final JLabel       posLabel     = new JLabel();

    public GUIEmployeeMenu(GUIMainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(GUIMainFrame.COLOR_BG);
        build();
    }

    private void build() {
        add(GUIMainFrame.makeHeaderPanel("Employee Portal"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(GUIMainFrame.COLOR_BG);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 228, 236), 1),
                new EmptyBorder(40, 60, 40, 60)));

        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        welcomeLabel.setForeground(GUIMainFrame.COLOR_PRIMARY);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        posLabel.setFont(GUIMainFrame.FONT_LABEL);
        posLabel.setForeground(new Color(100, 116, 139));
        posLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(new Color(222, 228, 236));

        JButton infoBtn    = GUIMainFrame.makeButton("View My Information", GUIMainFrame.COLOR_PRIMARY);
        JButton payrollBtn = GUIMainFrame.makeButton("View My Payroll",     GUIMainFrame.COLOR_ACCENT);
        JButton logoutBtn  = GUIMainFrame.makeButton("Logout",              new Color(71, 85, 105));

        styleMenuButton(infoBtn);
        styleMenuButton(payrollBtn);
        styleMenuButton(logoutBtn);

        infoBtn.addActionListener(e    -> frame.showEmployeeInfo());
        payrollBtn.addActionListener(e -> frame.showViewOnePayroll());
        logoutBtn.addActionListener(e  -> frame.logout());

        card.add(welcomeLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(posLabel);
        card.add(Box.createVerticalStrut(24));
        card.add(sep);
        card.add(Box.createVerticalStrut(24));
        card.add(infoBtn);
        card.add(Box.createVerticalStrut(12));
        card.add(payrollBtn);
        card.add(Box.createVerticalStrut(12));
        card.add(logoutBtn);

        center.add(card);
        add(center, BorderLayout.CENTER);
    }

    private void styleMenuButton(JButton btn) {
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(300, 44));
        btn.setPreferredSize(new Dimension(280, 44));
    }

    public void setEmployee(Employee emp) {
        welcomeLabel.setText("Welcome, " + emp.getFirstName().trim() + " " + emp.getLastName().trim());
        posLabel.setText(emp.getPosition());
    }
}