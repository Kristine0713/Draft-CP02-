package com.mycompany.cp2ms1grp10;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GUIEnterEmployeeNumber extends JPanel {

    private final GUIMainFrame frame;
    private final JTextField empNumberField = new JTextField(20);
    private final JLabel errorLabel = new JLabel(" ");

    private String mode = "employee";

    public GUIEnterEmployeeNumber(GUIMainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(GUIMainFrame.COLOR_BG);
        build();
    }

    private void build() {
        add(GUIMainFrame.makeHeaderPanel("MOTORPH STAFF"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(GUIMainFrame.COLOR_BG);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 228, 236), 1),
                new EmptyBorder(50, 60, 50, 60)));

        JLabel title = new JLabel("Enter Employee Number");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(GUIMainFrame.COLOR_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        empNumberField.setMaximumSize(new Dimension(300, 40));
        empNumberField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        empNumberField.setHorizontalAlignment(JTextField.CENTER);
        empNumberField.setAlignmentX(Component.CENTER_ALIGNMENT);

        errorLabel.setForeground(new Color(180, 35, 24));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton submitBtn = GUIMainFrame.makeButton("Submit", GUIMainFrame.COLOR_PRIMARY);
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.setMaximumSize(new Dimension(300, 45));
        submitBtn.addActionListener(e -> submit());

        JButton backBtn = GUIMainFrame.makeButton("Back", new Color(71, 85, 105));
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(300, 40));
        backBtn.addActionListener(e -> frame.goBackFromEnterEmployee());

        card.add(title);
        card.add(Box.createVerticalStrut(20));
        card.add(empNumberField);
        card.add(Box.createVerticalStrut(10));
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(submitBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(backBtn);

        center.add(card);
        add(center, BorderLayout.CENTER);
    }

    private void submit() {
        String empNumber = empNumberField.getText().trim();

        Employee emp = EmployeeFinder.find(empNumber);

        if (emp != null) {
            errorLabel.setText(" ");
            frame.setCurrentEmployee(emp);

            if (mode.equals("employee")) {
                frame.showEmployeeMenu();
            } else if (mode.equals("payroll")) {
                frame.showViewOnePayroll();
            }

        } else {
            errorLabel.setText("Employee not found.");
        }
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void reset() {
        empNumberField.setText("");
        errorLabel.setText(" ");
    }
}