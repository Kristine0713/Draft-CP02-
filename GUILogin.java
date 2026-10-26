package com.mycompany.cp2ms1grp10;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GUILogin extends JPanel {

    private final GUIMainFrame frame;
    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JLabel errorLabel = new JLabel(" ");

    public GUILogin(GUIMainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(GUIMainFrame.COLOR_BG);
        build();
    }

    private void build() {
        add(GUIMainFrame.makeHeaderPanel("Payroll System Login"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(GUIMainFrame.COLOR_BG);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 228, 236), 1),
                new EmptyBorder(50, 60, 50, 60)));

        JLabel titleLbl = new JLabel("Sign In");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLbl.setForeground(GUIMainFrame.COLOR_PRIMARY);
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLbl = new JLabel("MotorPH Employee Payroll System");
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subLbl.setForeground(new Color(100, 116, 139));
        subLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(subLbl);
        card.add(Box.createVerticalStrut(30));
        card.add(makeFieldPanel("Username", usernameField));
        card.add(Box.createVerticalStrut(18));
        card.add(makeFieldPanel("Password", passwordField));
        card.add(Box.createVerticalStrut(10));

        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        errorLabel.setForeground(new Color(180, 35, 24));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(20));

        JButton loginBtn = GUIMainFrame.makeButton("Login", GUIMainFrame.COLOR_PRIMARY);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(300, 50));
        loginBtn.setPreferredSize(new Dimension(300, 50));
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginBtn.addActionListener(e -> doLogin());
        card.add(loginBtn);

        JLabel hint = new JLabel("Hint: employee | payroll_staff  /  password: 12345");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        hint.setForeground(new Color(148, 163, 184));
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(16));
        card.add(hint);

        SwingUtilities.invokeLater(() -> {
            JRootPane root = getRootPane();
            if (root != null) {
                root.setDefaultButton(loginBtn);
            }
        });

        passwordField.addActionListener(e -> doLogin());
        usernameField.addActionListener(e -> doLogin());

        center.add(card);
        add(center, BorderLayout.CENTER);
    }

    private JPanel makeFieldPanel(String label, JComponent field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);

        JLabel lbl = new JLabel(label);
        lbl.setFont(GUIMainFrame.FONT_BOLD);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (field instanceof JTextField) {
            ((JTextField) field).setFont(new Font("SansSerif", Font.PLAIN, 15));
            ((JTextField) field).setHorizontalAlignment(JTextField.CENTER);
        }

        if (field instanceof JPasswordField) {
            ((JPasswordField) field).setFont(new Font("SansSerif", Font.PLAIN, 15));
            ((JPasswordField) field).setHorizontalAlignment(JTextField.CENTER);
        }

        field.setMaximumSize(new Dimension(300, 40));
        field.setPreferredSize(new Dimension(300, 40));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(lbl);
        p.add(Box.createVerticalStrut(6));
        p.add(field);

        return p;
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        Verification v = new Verification();
        String role = v.verify(username, password);

        if (role != null) {
            errorLabel.setText(" ");
            frame.onLoginSuccess(role);
        } else {
            errorLabel.setText("Incorrect username or password. Please try again.");
            passwordField.setText("");
        }
    }

    public void reset() {
        usernameField.setText("");
        passwordField.setText("");
        errorLabel.setText(" ");
    }
}