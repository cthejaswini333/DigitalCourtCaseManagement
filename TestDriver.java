package com.digitalcourt.management;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class TestDriver extends JFrame {
    private DatabaseHandler dbHandler;

    public TestDriver() {
        dbHandler = new DatabaseHandler();
        initGUI();
    }

    private void initGUI() {
        setTitle("Digital Court Case Management");
        setSize(650, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(13, 1));

        JButton[] buttons = new JButton[]{
            new JButton("1. Add Admin"),
            new JButton("2. View Admins"),
            new JButton("3. Update Admin"),
            new JButton("4. Delete Admin"),
            new JButton("5. Add User"),
            new JButton("6. View Users"),
            new JButton("7. Update User"),
            new JButton("8. Delete User"),
            new JButton("9. Add Case"),
            new JButton("10. View Cases"),
            new JButton("11. Update Case"),
            new JButton("12. Delete Case"),
            new JButton("13. Exit")
        };

        for (JButton btn : buttons) add(btn);

        // Admin actions
        buttons[0].addActionListener(e -> addAdmin());
        buttons[1].addActionListener(e -> viewAdmins());
        buttons[2].addActionListener(e -> updateAdmin());
        buttons[3].addActionListener(e -> deleteAdmin());

        // User actions
        buttons[4].addActionListener(e -> addUser());
        buttons[5].addActionListener(e -> viewUsers());
        buttons[6].addActionListener(e -> updateUser());
        buttons[7].addActionListener(e -> deleteUser());

        // Case actions
        buttons[8].addActionListener(e -> addCase());
        buttons[9].addActionListener(e -> viewCases());
        buttons[10].addActionListener(e -> updateCase());
        buttons[11].addActionListener(e -> deleteCase());

        buttons[12].addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    // ------------------ Admin Methods ------------------
    private void addAdmin() {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        Object[] fields = {"Name:", nameField, "Email:", emailField};
        int option = JOptionPane.showConfirmDialog(this, fields, "Add Admin", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION) {
            try {
                PreparedStatement ps = dbHandler.getConnection()
                        .prepareStatement("INSERT INTO admin(name,email) VALUES(?,?)");
                ps.setString(1, nameField.getText());
                ps.setString(2, emailField.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Admin Added!");
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    private void viewAdmins() {
        String search = JOptionPane.showInputDialog("Search Admin by Name (leave blank for all):");
        try {
            PreparedStatement ps;
            if(search == null || search.trim().isEmpty())
                ps = dbHandler.getConnection().prepareStatement("SELECT * FROM admin");
            else {
                ps = dbHandler.getConnection().prepareStatement("SELECT * FROM admin WHERE name LIKE ?");
                ps.setString(1, "%" + search + "%");
            }
            ResultSet rs = ps.executeQuery();
            showTable(rs, "Admins");
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void updateAdmin() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        Object[] fields = {"Admin ID:", idField, "New Name:", nameField, "New Email:", emailField};
        int option = JOptionPane.showConfirmDialog(this, fields, "Update Admin", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION) {
            try {
                PreparedStatement ps = dbHandler.getConnection()
                        .prepareStatement("UPDATE admin SET name=?, email=? WHERE id=?");
                ps.setString(1, nameField.getText());
                ps.setString(2, emailField.getText());
                ps.setInt(3, Integer.parseInt(idField.getText()));
                int rows = ps.executeUpdate();
                JOptionPane.showMessageDialog(this, rows > 0 ? "Admin Updated!" : "Admin Not Found!");
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    private void deleteAdmin() {
        String id = JOptionPane.showInputDialog("Enter Admin ID to Delete:");
        if(id != null) {
            try {
                PreparedStatement ps = dbHandler.getConnection().prepareStatement("DELETE FROM admin WHERE id=?");
                ps.setInt(1, Integer.parseInt(id));
                int rows = ps.executeUpdate();
                JOptionPane.showMessageDialog(this, rows > 0 ? "Admin Deleted!" : "Admin Not Found!");
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    // ------------------ User Methods ------------------
    private void addUser() {
        JTextField nameField = new JTextField();
        JTextField roleField = new JTextField();
        Object[] fields = {"Name:", nameField, "Role:", roleField};
        int option = JOptionPane.showConfirmDialog(this, fields, "Add User", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION) {
            try {
                PreparedStatement ps = dbHandler.getConnection().prepareStatement("INSERT INTO users(name,role) VALUES(?,?)");
                ps.setString(1, nameField.getText());
                ps.setString(2, roleField.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "User Added!");
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    private void viewUsers() {
        String search = JOptionPane.showInputDialog("Search User by Name or Role (leave blank for all):");
        try {
            PreparedStatement ps;
            if(search == null || search.trim().isEmpty())
                ps = dbHandler.getConnection().prepareStatement("SELECT * FROM users");
            else {
                ps = dbHandler.getConnection().prepareStatement("SELECT * FROM users WHERE name LIKE ? OR role LIKE ?");
                ps.setString(1, "%" + search + "%");
                ps.setString(2, "%" + search + "%");
            }
            ResultSet rs = ps.executeQuery();
            showTable(rs, "Users");
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void updateUser() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField roleField = new JTextField();
        Object[] fields = {"User ID:", idField, "New Name:", nameField, "New Role:", roleField};
        int option = JOptionPane.showConfirmDialog(this, fields, "Update User", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION) {
            try {
                PreparedStatement ps = dbHandler.getConnection().prepareStatement("UPDATE users SET name=?, role=? WHERE id=?");
                ps.setString(1, nameField.getText());
                ps.setString(2, roleField.getText());
                ps.setInt(3, Integer.parseInt(idField.getText()));
                int rows = ps.executeUpdate();
                JOptionPane.showMessageDialog(this, rows > 0 ? "User Updated!" : "User Not Found!");
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    private void deleteUser() {
        String id = JOptionPane.showInputDialog("Enter User ID to Delete:");
        if(id != null) {
            try {
                PreparedStatement ps = dbHandler.getConnection().prepareStatement("DELETE FROM users WHERE id=?");
                ps.setInt(1, Integer.parseInt(id));
                int rows = ps.executeUpdate();
                JOptionPane.showMessageDialog(this, rows > 0 ? "User Deleted!" : "User Not Found!");
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    // ------------------ Case Methods ------------------
    private void addCase() {
        JTextField titleField = new JTextField();
        JTextField statusField = new JTextField();
        Object[] fields = {"Title:", titleField, "Status:", statusField};
        int option = JOptionPane.showConfirmDialog(this, fields, "Add Case", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION) {
            try {
                PreparedStatement ps = dbHandler.getConnection().prepareStatement("INSERT INTO cases(title,status) VALUES(?,?)");
                ps.setString(1, titleField.getText());
                ps.setString(2, statusField.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Case Added!");
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    private void viewCases() {
        String search = JOptionPane.showInputDialog("Search Case by Title or Status (leave blank for all):");
        try {
            PreparedStatement ps;
            if(search == null || search.trim().isEmpty())
                ps = dbHandler.getConnection().prepareStatement("SELECT * FROM cases");
            else {
                ps = dbHandler.getConnection().prepareStatement("SELECT * FROM cases WHERE title LIKE ? OR status LIKE ?");
                ps.setString(1, "%" + search + "%");
                ps.setString(2, "%" + search + "%");
            }
            ResultSet rs = ps.executeQuery();
            showTable(rs, "Cases");
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void updateCase() {
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField statusField = new JTextField();
        Object[] fields = {"Case ID:", idField, "New Title:", titleField, "New Status:", statusField};
        int option = JOptionPane.showConfirmDialog(this, fields, "Update Case", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION) {
            try {
                PreparedStatement ps = dbHandler.getConnection().prepareStatement("UPDATE cases SET title=?, status=? WHERE id=?");
                ps.setString(1, titleField.getText());
                ps.setString(2, statusField.getText());
                ps.setInt(3, Integer.parseInt(idField.getText()));
                int rows = ps.executeUpdate();
                JOptionPane.showMessageDialog(this, rows > 0 ? "Case Updated!" : "Case Not Found!");
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    private void deleteCase() {
        String id = JOptionPane.showInputDialog("Enter Case ID to Delete:");
        if(id != null) {
            try {
                PreparedStatement ps = dbHandler.getConnection().prepareStatement("DELETE FROM cases WHERE id=?");
                ps.setInt(1, Integer.parseInt(id));
                int rows = ps.executeUpdate();
                JOptionPane.showMessageDialog(this, rows > 0 ? "Case Deleted!" : "Case Not Found!");
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    // ------------------ Utility ------------------
    private void showTable(ResultSet rs, String title) throws SQLException {
        JTable table = new JTable(buildTableModel(rs));
        JScrollPane scrollPane = new JScrollPane(table);
        JFrame frame = new JFrame("View " + title);
        frame.setSize(600, 300);
        frame.add(scrollPane);
        frame.setVisible(true);
    }

    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        String[] columnNames = new String[columnCount];
        for(int i = 0; i < columnCount; i++) columnNames[i] = metaData.getColumnName(i + 1);

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        while(rs.next()) {
            Object[] row = new Object[columnCount];
            for(int i = 0; i < columnCount; i++) row[i] = rs.getObject(i + 1);
            model.addRow(row);
        }
        return model;
    }

    public static void main(String[] args) {
    	SwingUtilities.invokeLater(() -> new TestDriver());
    }
    }