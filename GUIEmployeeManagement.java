package com.mycompany.cp2ms1grp10;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

public class GUIEmployeeManagement extends JPanel {

    private final GUIMainFrame frame;

    private DefaultTableModel tableModel;
    private JTable employeeTable;

    // Text fields
    private final JTextField txtEmpNum     = new JTextField();
    private final JTextField txtLastName   = new JTextField();
    private final JTextField txtFirstName  = new JTextField();
    private final JTextField txtBirthday   = new JTextField("MM/DD/YYYY");
    private final JTextField txtPhone      = new JTextField("XXX-XXX-XXX");
    private final JTextField txtPosition   = new JTextField();
    private final JTextField txtSSS        = new JTextField("XX-XXXXXXX-X");
    private final JTextField txtTIN        = new JTextField("XXX-XXX-XXX-XXX");
    private final JTextField txtPhilHealth = new JTextField("XXXXXXXXXXXX");
    private final JTextField txtSalary     = new JTextField();
   
    private final JLabel statusLabel = new JLabel(" ");
    private final JLabel lblFormMode = new JLabel("New Employee");

    // Row-action buttons (need refs to disable during edit)
    private JButton btnEdit;
    private JButton btnDelete;

    // Swappable button panels
    private final CardLayout actionLayout = new CardLayout();
    private final JPanel     actionCards  = new JPanel(actionLayout);

    // State
    private boolean editMode    = false;
    private int     selectedRow = -1;

    // Originals stored when edit starts — used for change detection
    private String origLastName, origFirstName, origBirthday, origPhone,
                   origPosition, origSSS, origTIN, origPhilHealth, origSalary;

    // Colours
    private static final Color COLOR_DELETE = new Color(180, 35, 24);
    private static final Color COLOR_GRAY   = new Color(71, 85, 105);
    private static final Color COLOR_GREEN  = new Color(22, 128, 93);

    public GUIEmployeeManagement(GUIMainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(GUIMainFrame.COLOR_BG);
        build();
        applyNameFilter(txtLastName);
        applyNameFilter(txtFirstName);
        applyNameFilter(txtPosition);
        
        // Numeric input filters
        applyAutoFormat(txtBirthday, new int[]{1,3}, '/', 8);   // MM/DD/YYYY
        applyAutoFormat(txtPhone, new int[]{2,5}, '-', 9);      // XXX-XXX-XXX
        applyAutoFormat(txtSSS, new int[]{1,8}, '-', 10);       // XX-XXXXXXX-X
        applyAutoFormat(txtTIN, new int[]{2,5,8}, '-', 12);     // XXX-XXX-XXX-XXX
        applyAutoFormat(txtPhilHealth, new int[]{}, ' ', 12);   // 12 digits only
        applyDecimalFilter(txtSalary);
        
        // Placeholder
        addPlaceholderBehavior(txtBirthday, "MM/DD/YYYY");
        addPlaceholderBehavior(txtPhone, "XXX-XXX-XXX");
        addPlaceholderBehavior(txtSSS, "XX-XXXXXXX-X");
        addPlaceholderBehavior(txtTIN, "XXX-XXX-XXX-XXX");
        addPlaceholderBehavior(txtPhilHealth, "XXXXXXXXXXXX");
        
        Font italic = txtBirthday.getFont().deriveFont(Font.ITALIC);

        txtBirthday.setFont(italic);
        txtPhone.setFont(italic);
        txtSSS.setFont(italic);
        txtTIN.setFont(italic);
        txtPhilHealth.setFont(italic);
        txtEmpNum.setText(generateEmployeeNumber());
    }

private void build() {
    add(GUIMainFrame.makeHeaderPanel("Employee Record Management"), BorderLayout.NORTH);

    JPanel content = new JPanel(new BorderLayout(0, 12));
    content.setBackground(GUIMainFrame.COLOR_BG);
    content.setBorder(new EmptyBorder(15, 20, 15, 20));

    // ── Table ───────────────────────────────────────────────────────────
    String[] columns = {
        "Emp #", "Last Name", "First Name", "Birthday", "Phone",
        "Position", "Hourly Rate", "SSS Number", "TIN Number", "PhilHealth Number"
    };
    tableModel = new DefaultTableModel(columns, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };

    employeeTable = new JTable(tableModel);
    employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    employeeTable.setRowHeight(22);

    employeeTable.getSelectionModel().addListSelectionListener(e -> {
        if (e.getValueIsAdjusting()) return;
        selectedRow = employeeTable.getSelectedRow();
        boolean has = selectedRow != -1 && !editMode;
        btnEdit.setEnabled(has);
        btnDelete.setEnabled(has);
    });

    content.add(new JScrollPane(employeeTable), BorderLayout.CENTER);

    // ── Form Card ───────────────────────────────────────────────────────
    JPanel formCard = new JPanel(new BorderLayout(0, 8));
    formCard.setBackground(Color.WHITE);
    formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 228, 236)),
            new EmptyBorder(14, 18, 14, 18)));

    // Mode label
    lblFormMode.setFont(new Font("SansSerif", Font.BOLD, 13));
    lblFormMode.setForeground(GUIMainFrame.COLOR_PRIMARY);
    lblFormMode.setBorder(new EmptyBorder(0, 0, 4, 0));
    formCard.add(lblFormMode, BorderLayout.NORTH);

    // Fields
    JPanel fields = new JPanel(new GridLayout(4, 4, 10, 8));
    fields.setBackground(Color.WHITE);

    fields.add(makeField("Employee Number:", txtEmpNum));
    fields.add(makeField("Last Name:", txtLastName));
    fields.add(makeField("First Name:", txtFirstName));
    fields.add(makeField("Birthday:", txtBirthday));
    fields.add(makeField("Phone Number:", txtPhone));
    fields.add(makeField("Position:", txtPosition));
    fields.add(makeField("SSS Number:", txtSSS));
    fields.add(makeField("TIN Number:", txtTIN));
    fields.add(makeField("PhilHealth Number:", txtPhilHealth));
    fields.add(makeField("Hourly Rate:", txtSalary));
    fields.add(new JLabel(""));

    txtEmpNum.setEditable(false);
    formCard.add(fields, BorderLayout.CENTER);

    // ── Action panels (swapped via CardLayout) ───────────────────────────
    actionCards.setBackground(Color.WHITE);

    // — Normal mode —
    JPanel normalActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
    normalActions.setBackground(Color.WHITE);

    JButton btnAdd   = GUIMainFrame.makeButton("Add Employee", GUIMainFrame.COLOR_ACCENT);
    btnEdit          = GUIMainFrame.makeButton("Edit",         GUIMainFrame.COLOR_PRIMARY);
    btnDelete        = GUIMainFrame.makeButton("Delete",       COLOR_DELETE);
    JButton btnClear = GUIMainFrame.makeButton("Clear",        COLOR_GRAY);
    JButton btnBack  = GUIMainFrame.makeButton("Back",         GUIMainFrame.COLOR_PRIMARY);

    btnEdit.setEnabled(false);
    btnDelete.setEnabled(false);

    btnAdd.addActionListener(e    -> addEmployee());
    btnEdit.addActionListener(e   -> loadSelectedEmployee());
    btnDelete.addActionListener(e -> deleteEmployee());
    btnClear.addActionListener(e  -> clearForm());
    btnBack.addActionListener(e   -> { clearForm(); frame.displayPanel("PayrollMenu"); });

    normalActions.add(btnAdd);
    normalActions.add(btnEdit);
    normalActions.add(btnDelete);
    normalActions.add(btnClear);
    normalActions.add(btnBack);

    // — Edit mode —
    JPanel editActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
    editActions.setBackground(Color.WHITE);

    JButton btnSave   = GUIMainFrame.makeButton("Save Changes", GUIMainFrame.COLOR_PRIMARY);
    JButton btnCancel = GUIMainFrame.makeButton("Cancel",       COLOR_GRAY);

    btnSave.addActionListener(e   -> saveEdit());
    btnCancel.addActionListener(e -> handleCancelEdit());

    editActions.add(btnSave);
    editActions.add(btnCancel);

    actionCards.add(normalActions, "normal");
    actionCards.add(editActions,   "edit");
    actionLayout.show(actionCards, "normal");

    // ── South panel holds both statusLabel and action buttons ─────────────
    JPanel southPanel = new JPanel(new BorderLayout());
    southPanel.setBackground(Color.WHITE);

    statusLabel.setText(" ");
    statusLabel.setForeground(new Color(180, 35, 24));
    statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
    southPanel.add(statusLabel, BorderLayout.NORTH);

    southPanel.add(actionCards, BorderLayout.SOUTH);

    formCard.add(southPanel, BorderLayout.SOUTH);

    content.add(formCard, BorderLayout.SOUTH);
    add(content, BorderLayout.CENTER);
    }

    private JPanel makeField(String label, JTextField field) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(Color.WHITE);

    JLabel lbl = new JLabel(label);
    lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
    lbl.setForeground(new Color(31, 41, 55));

    panel.add(lbl, BorderLayout.NORTH);
    panel.add(field, BorderLayout.CENTER);

    return panel;
    }
    
    private void applyDecimalFilter(JTextField field) {
    ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null) {
                String newText = fb.getDocument().getText(0, fb.getDocument().getLength());
                newText = newText.substring(0, offset) + string + newText.substring(offset);
                if (newText.matches("\\d*(\\.\\d{0,2})?")) {
                    super.insertString(fb, offset, string, attr);
                }
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text != null) {
                String newText = fb.getDocument().getText(0, fb.getDocument().getLength());
                newText = newText.substring(0, offset) + text + newText.substring(offset + length);
                if (newText.matches("\\d*(\\.\\d{0,2})?")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        }
    });
}
    
    public final void refreshTable() {
        tableModel.setRowCount(0);
        for (Employee emp : DataManagement.employees) {
            tableModel.addRow(new Object[]{
                emp.getEmployeeNumber(), emp.getLastName(), emp.getFirstName(),
                emp.getBirthday(), emp.getPhoneNumber(), emp.getPosition(),
                emp.getHourlySalary(), emp.getSssNumber(),
                emp.getTinNumber(), emp.getPhilHealthNumber()
            });
        }
    }

    // ── Add (normal mode) ───────────────────────────────────────────────────

    private void addEmployee() {
        String[] vals = collectAndValidate();
        if (vals == null) return;

        String empNum    = vals[0], lastName  = vals[1], firstName = vals[2],
               birthday  = vals[3], phone     = vals[4], position  = vals[5],
               sss       = vals[6], tin       = vals[7], philHealth= vals[8];
        double salary    = Double.parseDouble(vals[9]);

        if (isDuplicate(empNum)) {
            setStatus("Employee number already exists.", true);
            return;
        }

        DataManagement.employees.add(new Employee(
                empNum, lastName, firstName, birthday, phone,
                position, salary, sss, tin, philHealth));

        refreshTable();
        clearForm();
        showSuccessPopup("Employee Added",
                "<b>" + firstName + " " + lastName + "</b> has been added successfully.");
        setStatus("Employee " + firstName + " " + lastName + " added.", false);
    }

    // ── Edit (edit mode) ────────────────────────────────────────────────────

    private void loadSelectedEmployee() {
        if (selectedRow == -1) return;

        txtEmpNum.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtLastName.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtFirstName.setText(tableModel.getValueAt(selectedRow, 2).toString());
        setTextBypassingFilter(txtBirthday,   tableModel.getValueAt(selectedRow, 3).toString());
        setTextBypassingFilter(txtPhone,      tableModel.getValueAt(selectedRow, 4).toString());
        txtPosition.setText(tableModel.getValueAt(selectedRow, 5).toString());
        txtSalary.setText(tableModel.getValueAt(selectedRow, 6).toString());
        setTextBypassingFilter(txtSSS,        tableModel.getValueAt(selectedRow, 7).toString());
        setTextBypassingFilter(txtTIN,        tableModel.getValueAt(selectedRow, 8).toString());
        setTextBypassingFilter(txtPhilHealth, tableModel.getValueAt(selectedRow, 9).toString());
        
        Font normal = txtBirthday.getFont().deriveFont(Font.PLAIN);
        
        txtBirthday.setForeground(new Color(31, 41, 55));
        txtPhone.setForeground(new Color(31, 41, 55));
        txtSSS.setForeground(new Color(31, 41, 55));
        txtTIN.setForeground(new Color(31, 41, 55));
        txtPhilHealth.setForeground(new Color(31, 41, 55));
        
        txtBirthday.setFont(normal);
        txtPhone.setFont(normal);
        txtSSS.setFont(normal);
        txtTIN.setFont(normal);
        txtPhilHealth.setFont(normal);
        

        // Store originals for change-detection
        origLastName   = txtLastName.getText().trim();
        origFirstName  = txtFirstName.getText().trim();
        origBirthday   = txtBirthday.getText().trim();
        origPhone      = txtPhone.getText().trim();
        origPosition   = txtPosition.getText().trim();
        origSSS        = txtSSS.getText().trim();
        origTIN        = txtTIN.getText().trim();
        origPhilHealth = txtPhilHealth.getText().trim();
        origSalary     = txtSalary.getText().trim();

        editMode = true;
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);

        lblFormMode.setText("Editing Employee — #" + txtEmpNum.getText().trim()
                + "  (" + origFirstName + " " + origLastName + ")");
        lblFormMode.setForeground(GUIMainFrame.COLOR_PRIMARY);

        actionLayout.show(actionCards, "edit");
        setStatus("Edit the fields below, then click Save Changes.", false);
        txtLastName.requestFocus();
    }

    private void saveEdit() {
        String[] vals = collectAndValidate();
        if (vals == null) return;

        String empNum    = vals[0], lastName  = vals[1], firstName = vals[2],
               birthday  = vals[3], phone     = vals[4], position  = vals[5],
               sss       = vals[6], tin       = vals[7], philHealth= vals[8];
        double salary    = Double.parseDouble(vals[9]);

        DataManagement.employees.set(selectedRow, new Employee(
                empNum, lastName, firstName, birthday, phone,
                position, salary, sss, tin, philHealth));

        refreshTable();
        showSuccessPopup("Employee Updated",
                "<b>" + firstName + " " + lastName + "</b> has been updated successfully.");
        setStatus("Employee " + firstName + " " + lastName + " updated.", false);
        exitEditMode();
    }

    /**
     * Cancel handler — shows confirmation popup only if something was changed;
     * exits edit mode immediately if nothing was modified.
     */
    private void handleCancelEdit() {
        if (hasUnsavedChanges()) {
            boolean confirmed = showCancelConfirmPopup();
            if (!confirmed) return;
        }
        exitEditMode();
    }

    private void exitEditMode() {
        editMode = false;
        origLastName = origFirstName = origBirthday = origPhone =
        origPosition = origSSS = origTIN = origPhilHealth = origSalary = null;

        lblFormMode.setText("New Employee");
        lblFormMode.setForeground(GUIMainFrame.COLOR_PRIMARY);

        actionLayout.show(actionCards, "normal");
        clearForm();
    }

    /** Returns true if any editable field differs from when Edit was clicked. */
    private boolean hasUnsavedChanges() {
        if (origLastName == null) return false;
        return !txtLastName.getText().trim().equals(origLastName)
            || !txtFirstName.getText().trim().equals(origFirstName)
            || !txtBirthday.getText().trim().equals(origBirthday)
            || !txtPhone.getText().trim().equals(origPhone)
            || !txtPosition.getText().trim().equals(origPosition)
            || !txtSSS.getText().trim().equals(origSSS)
            || !txtTIN.getText().trim().equals(origTIN)
            || !txtPhilHealth.getText().trim().equals(origPhilHealth)
            || !txtSalary.getText().trim().equals(origSalary);
    }

    // ── Delete ──────────────────────────────────────────────────────────────

    private void deleteEmployee() {
        if (selectedRow == -1) return;

        String name = tableModel.getValueAt(selectedRow, 2) + " "
                    + tableModel.getValueAt(selectedRow, 1);
        String empNum = tableModel.getValueAt(selectedRow, 0).toString();

        if (!showDeleteConfirmPopup(name, empNum)) return;

        DataManagement.employees.remove(selectedRow);
        refreshTable();
        clearForm();
        setStatus("Employee " + name + " deleted.", false);
    }

    // ── Shared validation ───────────────────────────────────────────────────

    /**
     * Reads and validates all form fields.
     * @return String[]{empNum, lastName, firstName, birthday, phone, position,
     *                   sss, tin, philHealth, salary} or null on failure.
     */
        private String[] collectAndValidate() {
            String empNum     = txtEmpNum.getText().trim();
            String lastName   = txtLastName.getText().trim();
            String firstName  = txtFirstName.getText().trim();
            String birthday   = txtBirthday.getText().trim();
            String phone      = txtPhone.getText().trim();
            String position   = txtPosition.getText().trim();
            String sss        = txtSSS.getText().trim();
            String tin        = txtTIN.getText().trim();
            String philHealth = txtPhilHealth.getText().trim();
            String salaryRaw  = txtSalary.getText().trim();
        
            // Required fields check
            if (lastName.isEmpty() || firstName.isEmpty() || birthday.isEmpty()
                    || phone.isEmpty() || position.isEmpty() || sss.isEmpty()
                    || tin.isEmpty() || philHealth.isEmpty() || salaryRaw.isEmpty()) {
                setStatus("All fields are required.", true);
                return null;
            }
        
            // Validators with explicit error messages
            if (!isValidName(lastName)) {
                setStatus("Last Name should contain letters only.", true);
                return null;
            }
        
            if (!isValidName(firstName)) {
                setStatus("First Name should contain letters only.", true);
                return null;
            }
        
            if (!isValidName(position)) {
                setStatus("Position should contain letters only.", true);
                return null;
            }
        
            if (!isValidBirthday(birthday)) {
                setStatus("Enter a valid Birthday (MM/DD/YYYY)", true);
                return null;
            }
        
            if (!isValidPhone(phone)) {
                setStatus("Enter a valid Phone (XXX-XXX-XXX)", true);
                return null;
            }
        
            if (!isValidSSS(sss)) {
                setStatus("Enter a valid SSS (XX-XXXXXXX-X)", true);
                return null;
            }
        
            if (!isValidTIN(tin)) {
                setStatus("Enter a valid TIN (XXX-XXX-XXX-XXX)", true);
                return null;
            }
        
            if (!isValidPhilHealth(philHealth)) {
                setStatus("Enter a valid PhilHealth (12 digits)", true);
                return null;
            }
        
            double salary;
            try {
                salaryRaw = salaryRaw.replaceAll(",", ""); // allow commas
                salary = Double.parseDouble(salaryRaw);
                if (salary <= 0) {
                    setStatus("Hourly rate must be greater than zero.", true);
                    return null;
                }
                salary = Math.round(salary * 100.0) / 100.0;
            } catch (NumberFormatException e) {
                setStatus("Hourly rate must be a number.", true);
                return null;
            }
        
            return new String[]{empNum, lastName, firstName, birthday, phone,
                                position, sss, tin, philHealth, String.valueOf(salary)};
        }

    // ── Auto-format helper ────────────────────────────────────────────────
    private void applyAutoFormat(JTextField field, int[] separatorPositions, char separator, int maxDigits) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string != null && string.matches("\\d+")) {
                    String digits = (field.getText() + string).replaceAll("[^\\d]", "");
                    String formatted = formatWithSeparators(digits, separatorPositions, separator, maxDigits);
                    super.replace(fb, 0, field.getText().length(), formatted, attr);
                }
            }
    
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text != null && text.matches("\\d*")) {
                    String digits = (field.getText().substring(0, offset) + text +
                            field.getText().substring(offset + length)).replaceAll("[^\\d]", "");
                    String formatted = formatWithSeparators(digits, separatorPositions, separator, maxDigits);
                    super.replace(fb, 0, field.getText().length(), formatted, attrs);
                }
            }
        });
    }
    
    private String formatWithSeparators(String digits, int[] separatorPositions, char separator, int maxDigits) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digits.length() && i < maxDigits; i++) {
            sb.append(digits.charAt(i));
            for (int pos : separatorPositions) {
                if (i == pos) sb.append(separator);
            }
        }
        return sb.toString();
    }


    // ── Validators ──────────────────────────────────────────────────────────

    private boolean isValidName(String name) {
    return name.matches("^[A-Za-z '-]+$");
    }
    private boolean isValidBirthday(String d) {
        if (!d.matches("^(0[1-9]|1[0-2])/([0-2][0-9]|3[01])/\\d{4}$")) {
            return false;
        }
        String[] parts = d.split("/");
        int month = Integer.parseInt(parts[0]);
        int day   = Integer.parseInt(parts[1]);
    
        // Reject 00 month or day
        if (month == 0 || day == 0) {
            return false;
        }
        return true;
    }
    private boolean isValidPhone(String p) {
        return p.matches("^\\d{3}-\\d{3}-\\d{3}$");
    }
    private boolean isValidSSS(String s) {
        return s.matches("^\\d{2}-\\d{7}-\\d{1}$");
    }
    private boolean isValidTIN(String t) {
        return t.matches("^\\d{3}-\\d{3}-\\d{3}-\\d{3}$");
    }
    private boolean isValidPhilHealth(String ph) {
        return ph.matches("^\\d{12}$");
    }
    private boolean isDuplicate(String empNum) {
        for (Employee e : DataManagement.employees)
            if (e.getEmployeeNumber().equals(empNum)) return true;
        return false;
    }

    private String generateEmployeeNumber() {
        int highest = 10000;
        for (Employee emp : DataManagement.employees) {
            try {
                int n = Integer.parseInt(emp.getEmployeeNumber());
                if (n > highest) highest = n;
            } catch (NumberFormatException ignored) {}
        }
        return String.valueOf(highest + 1);
    }

    // ── Popup Cards ─────────────────────────────────────────────────────────

    private void showSuccessPopup(String title, String message) {
        JOptionPane.showMessageDialog(this,
                buildPopupCard("✔", COLOR_GREEN, message), title, JOptionPane.PLAIN_MESSAGE);
    }

    private boolean showCancelConfirmPopup() {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(380, 148));

        JPanel top = new JPanel();
        top.setBackground(GUIMainFrame.COLOR_PRIMARY);
        top.setBorder(new EmptyBorder(10, 0, 10, 0));
        JLabel icon = new JLabel("⚠  Cancel Edit?");
        icon.setFont(new Font("SansSerif", Font.BOLD, 15));
        icon.setForeground(Color.WHITE);
        top.add(icon);

        JLabel msg = new JLabel(
                "<html><center>Your changes have not been saved.<br>"
                + "Are you sure you want to cancel editing?</center></html>");
        msg.setFont(new Font("SansSerif", Font.PLAIN, 13));
        msg.setHorizontalAlignment(SwingConstants.CENTER);
        msg.setBorder(new EmptyBorder(8, 16, 8, 16));

        card.add(top, BorderLayout.NORTH);
        card.add(msg, BorderLayout.CENTER);

        return JOptionPane.showConfirmDialog(
                this, card, "Discard Changes?",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.YES_OPTION;
    }

    private boolean showDeleteConfirmPopup(String name, String empNum) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(360, 140));

        JPanel top = new JPanel();
        top.setBackground(COLOR_DELETE);
        top.setBorder(new EmptyBorder(10, 0, 10, 0));
        JLabel icon = new JLabel("⚠  Confirm Delete");
        icon.setFont(new Font("SansSerif", Font.BOLD, 15));
        icon.setForeground(Color.WHITE);
        top.add(icon);

        JLabel msg = new JLabel(
                "<html><center>Delete employee record for<br>"
                + "<b>" + name + "</b>  (#" + empNum + ")?<br>"
                + "<span style='color:gray;font-size:11px'>This action cannot be undone.</span>"
                + "</center></html>");
        msg.setFont(new Font("SansSerif", Font.PLAIN, 13));
        msg.setHorizontalAlignment(SwingConstants.CENTER);
        msg.setBorder(new EmptyBorder(6, 16, 6, 16));

        card.add(top, BorderLayout.NORTH);
        card.add(msg, BorderLayout.CENTER);

        return JOptionPane.showConfirmDialog(
                this, card, "Delete Employee",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.YES_OPTION;
    }

    private JPanel buildPopupCard(String iconText, Color accentColor, String message) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(360, 120));

        JPanel top = new JPanel();
        top.setBackground(accentColor);
        top.setBorder(new EmptyBorder(8, 0, 8, 0));
        JLabel icon = new JLabel(iconText);
        icon.setFont(new Font("SansSerif", Font.BOLD, 20));
        icon.setForeground(Color.WHITE);
        top.add(icon);

        JLabel msg = new JLabel("<html><center>" + message + "</center></html>");
        msg.setFont(new Font("SansSerif", Font.PLAIN, 13));
        msg.setHorizontalAlignment(SwingConstants.CENTER);
        msg.setBorder(new EmptyBorder(4, 16, 8, 16));

        card.add(top,  BorderLayout.NORTH);
        card.add(msg,  BorderLayout.CENTER);
        return card;
    }

    // ── Utilities ────────────────────────────────────────────────────────────

    private void clearForm() {
        txtEmpNum.setText(generateEmployeeNumber());
        txtLastName.setText("");
        txtFirstName.setText("");
        txtPosition.setText("");
    
        setTextBypassingFilter(txtBirthday, "MM/DD/YYYY");
        txtBirthday.setForeground(new Color(148, 163, 184));
        txtBirthday.setFont(txtBirthday.getFont().deriveFont(Font.ITALIC));
    
        setTextBypassingFilter(txtPhone, "XXX-XXX-XXX");
        txtPhone.setForeground(new Color(148, 163, 184));
        txtPhone.setFont(txtPhone.getFont().deriveFont(Font.ITALIC));
    
        setTextBypassingFilter(txtSSS, "XX-XXXXXXX-X");
        txtSSS.setForeground(new Color(148, 163, 184));
        txtSSS.setFont(txtSSS.getFont().deriveFont(Font.ITALIC));
    
        setTextBypassingFilter(txtTIN, "XXX-XXX-XXX-XXX");
        txtTIN.setForeground(new Color(148, 163, 184));
        txtTIN.setFont(txtTIN.getFont().deriveFont(Font.ITALIC));
    
        setTextBypassingFilter(txtPhilHealth, "XXXXXXXXXXXX");
        txtPhilHealth.setForeground(new Color(148, 163, 184));
        txtPhilHealth.setFont(txtPhilHealth.getFont().deriveFont(Font.ITALIC));
    
        txtSalary.setText("");
    
        statusLabel.setText(" ");
        employeeTable.clearSelection();
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        selectedRow = -1;
    }

    private void setStatus(String msg, boolean error) {
        statusLabel.setText(msg);
        statusLabel.setForeground(error ? COLOR_DELETE : COLOR_GREEN);
    }
    
    private void applyNameFilter(JTextField field) {

    ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void insertString(FilterBypass fb, int offset,
                                 String string, AttributeSet attr)
                throws BadLocationException {

            if (string != null && string.matches("[A-Za-z '-]+")) {
                super.insertString(fb, offset, string, attr);
            }
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length,
                            String text, AttributeSet attrs)
                throws BadLocationException {

            if (text == null || text.matches("[A-Za-z '-]*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
     });
  }
    
    private void addPlaceholderBehavior(JTextField field, String placeholder) {

    field.setForeground(new Color(148, 163, 184));

    field.addFocusListener(new java.awt.event.FocusAdapter() {

        @Override
        public void focusGained(java.awt.event.FocusEvent e) {

            if (field.getText().equals(placeholder)) {
                field.setText("");
                field.setForeground(new Color(31, 41, 55));
                field.setFont(field.getFont().deriveFont(Font.PLAIN));
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent e) {

            if (field.getText().trim().isEmpty()) {
                field.setText(placeholder);
                field.setForeground(new Color(148, 163, 184));
                field.setFont(field.getFont().deriveFont(Font.ITALIC));
            }
        }
    });
}
        private void applyInputFilter(JTextField field, String allowedCharacters) {

    ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {

        @Override
        public void insertString(FilterBypass fb, int offset,
                                 String string, AttributeSet attr)
                throws BadLocationException {

            if (string != null && string.matches("[" + allowedCharacters + "]+")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length,
                            String text, AttributeSet attrs)
                throws BadLocationException {

            if (text == null || text.matches("[" + allowedCharacters + "]*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    });
}

    private void setTextBypassingFilter(JTextField field, String text) {
        javax.swing.text.AbstractDocument doc = (javax.swing.text.AbstractDocument) field.getDocument();
        javax.swing.text.DocumentFilter filter = doc.getDocumentFilter();
        doc.setDocumentFilter(null);
        field.setText(text);
        doc.setDocumentFilter(filter);
    }
}
     
     
