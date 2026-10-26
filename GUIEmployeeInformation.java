package com.mycompany.cp2ms1grp10;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GUIEmployeeInformation extends JPanel {

    private final GUIMainFrame frame;

    // Personal Information
    private final JLabel empNumber = new JLabel();
    private final JLabel name = new JLabel();
    private final JLabel birthday = new JLabel();
    private final JLabel phone = new JLabel();
    
    // Company Informaton
    private final JLabel position = new JLabel();
    private final JLabel sss = new JLabel();
    private final JLabel tin = new JLabel();
    private final JLabel philhealth = new JLabel();
    private final JLabel salary = new JLabel();

    public GUIEmployeeInformation(GUIMainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(GUIMainFrame.COLOR_BG);
        build();
    }

    private void build() {
        add(GUIMainFrame.makeHeaderPanel("Employee Information"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(GUIMainFrame.COLOR_BG);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 228, 236), 1),
                new EmptyBorder(40, 60, 40, 60)));
        
        // ----- Personal Information -----
        JLabel personalInfoLabel = new JLabel("Personal Information");
        personalInfoLabel.setFont(GUIMainFrame.FONT_BOLD);
        personalInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(personalInfoLabel);
        
        card.add(makeLabel("Employee Number: ", empNumber));
        card.add(makeLabel("Name: ", name));
        card.add(makeLabel("Birthday: ", birthday));
        card.add(makeLabel("Phone: ", phone));

        card.add(Box.createRigidArea(new Dimension(0, 25))); 
        
        // ----- Company Information -----
        JLabel companyInfoLabel = new JLabel("Company Information");
        companyInfoLabel.setFont(GUIMainFrame.FONT_BOLD);
        companyInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(companyInfoLabel);
        
        card.add(makeLabel("Position: ", position));
        card.add(makeLabel("SSS Number: ", sss));
        card.add(makeLabel("TIN Number: ", tin));
        card.add(makeLabel("PhilHealth Number: ", philhealth));
        card.add(makeLabel("Hourly Salary: ", salary));
        
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        
        JButton backBtn = GUIMainFrame.makeButton("Back", GUIMainFrame.COLOR_PRIMARY);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.addActionListener(e -> frame.displayPanel("EmployeeMenu"));

        card.add(backBtn);

        center.add(card);
        add(center, BorderLayout.CENTER);
    }

    private JPanel makeLabel(String title, JLabel value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);

        JLabel t = new JLabel(title);
        t.setFont(GUIMainFrame.FONT_BOLD);

        value.setFont(GUIMainFrame.FONT_LABEL);

        p.add(t, BorderLayout.WEST);
        p.add(value, BorderLayout.CENTER);

        return p;
    }

    public void setEmployee(Employee emp) {
        // Personal Information
        empNumber.setText(emp.getEmployeeNumber());
        name.setText(emp.getFirstName() + " " + emp.getLastName());
        birthday.setText(emp.getBirthday());
        phone.setText(emp.getPhoneNumber());
        
        // Company Information
        position.setText(emp.getPosition());
        sss.setText(emp.getSssNumber());
        tin.setText(emp.getTinNumber());
        philhealth.setText(emp.getPhilHealthNumber());
        salary.setText(String.valueOf(emp.getHourlySalary()));
    }  
}