package com.mycompany.cp2ms1grp10;

import javax.swing.*;
import java.awt.*;

public class GUIMainFrame extends JFrame {

    public static final Color COLOR_BG      = new Color(244, 246, 250);
    public static final Color COLOR_PRIMARY = new Color(31, 41, 55);
    public static final Color COLOR_ACCENT  = new Color(15, 118, 110);

    public static final Font FONT_BOLD  = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_LABEL = new Font("SansSerif", Font.PLAIN, 13);

    private JPanel     mainPanel;
    private CardLayout cardLayout;

    private GUILogin                  loginPanel;
    private GUIEnterEmployeeNumber    enterEmployeePanel;
    private GUIEmployeeMenu           employeeMenu;
    private GUIPayrollStaffMenu       payrollMenu;
    private GUIEmployeeInformation    employeeInfoPanel;
    private GUIViewOneEmployeePayroll viewPayrollPanel;
    private GUIViewAllEmployeePayroll viewAllPayrollPanel;
    private GUIEmployeeManagement     employeeManagementPanel;
    private GUIAttendanceManagement   attendanceManagementPanel;

    private Employee currentEmployee;
    private String   currentRole;

    public GUIMainFrame() {
        setTitle("MotorPH Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);

        loginPanel                = new GUILogin(this);
        enterEmployeePanel        = new GUIEnterEmployeeNumber(this);
        employeeMenu              = new GUIEmployeeMenu(this);
        payrollMenu               = new GUIPayrollStaffMenu(this);
        employeeInfoPanel         = new GUIEmployeeInformation(this);
        viewPayrollPanel          = new GUIViewOneEmployeePayroll(this);
        viewAllPayrollPanel       = new GUIViewAllEmployeePayroll(this);
        employeeManagementPanel   = new GUIEmployeeManagement(this);
        attendanceManagementPanel = new GUIAttendanceManagement(this);

        mainPanel.add(loginPanel,                "Login");
        mainPanel.add(enterEmployeePanel,        "EnterEmployee");
        mainPanel.add(employeeMenu,              "EmployeeMenu");
        mainPanel.add(payrollMenu,               "PayrollMenu");
        mainPanel.add(employeeInfoPanel,         "EmployeeInfo");
        mainPanel.add(viewPayrollPanel,          "ViewPayroll");
        mainPanel.add(viewAllPayrollPanel,       "ViewAllPayroll");
        mainPanel.add(employeeManagementPanel,   "EmployeeManagement");
        mainPanel.add(attendanceManagementPanel, "AttendanceManagement");

        add(mainPanel);
        displayPanel("Login");
    }

    public void displayPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public void onLoginSuccess(String role) {
        currentRole = role;
        if (role.equals("employee")) {
            enterEmployeePanel.setMode("employee");
            enterEmployeePanel.reset();
            displayPanel("EnterEmployee");
        } else if (role.equals("payroll_staff")) {
            displayPanel("PayrollMenu");
        }
    }

    public void setCurrentEmployee(Employee emp) {
        this.currentEmployee = emp;
    }

    public void showEmployeeMenu() {
        employeeMenu.setEmployee(currentEmployee);
        displayPanel("EmployeeMenu");
    }

    public void showEmployeeInfo() {
        employeeInfoPanel.setEmployee(currentEmployee);
        displayPanel("EmployeeInfo");
    }

    public void showViewOnePayroll() {
        viewPayrollPanel.displayPayroll(currentEmployee);
        displayPanel("ViewPayroll");
    }

    public void showViewAllPayroll() {
        viewAllPayrollPanel.displayAllPayroll();
        displayPanel("ViewAllPayroll");
    }

    public void showStaffSearchEmployee() {
        enterEmployeePanel.setMode("payroll");
        enterEmployeePanel.reset();
        displayPanel("EnterEmployee");
    }

    public void showEmployeeManagement() {
        employeeManagementPanel.refreshTable();
        displayPanel("EmployeeManagement");
    }

    public void showAttendanceManagement() {
        attendanceManagementPanel.refreshTable();
        displayPanel("AttendanceManagement");
    }

    public void goBackFromPayroll() {
        if (currentRole.equals("employee")) displayPanel("EmployeeMenu");
        else displayPanel("PayrollMenu");
    }

    public void goBackFromEnterEmployee() {
        if (currentRole.equals("employee")) displayPanel("Login");
        else displayPanel("PayrollMenu");
    }

    public void logout() {
        loginPanel.reset();
        displayPanel("Login");
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public static JButton makeButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        return btn;
    }

    public static JPanel makeHeaderPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_PRIMARY);
        panel.setPreferredSize(new Dimension(800, 60));
        JLabel label = new JLabel(title);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(label);
        return panel;
    }
}
