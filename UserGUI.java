package com.digitalcourt.management;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UserGUI extends JFrame {
    private DatabaseHandler db;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtId, txtUsername, txtPassword, txtRole;

    public UserGUI() {
        db = new DatabaseHandler();
        setTitle("User Management");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Input Panel ---
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        txtId = new JTextField();
        txtUsername = new JTextField();
        txtPassword = new JTextField();
        txtRole = new JTextField();

        inputPanel.add(new JLabel("User ID (for update/delete):"));
        inputPanel.add(txtId);
        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(txtUsername);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(txtPassword);
        inputPanel.add(new JLabel("Role:"));
        inputPanel.add(txtRole);

        // --- Buttons ---
        JPanel btnPanel = new JPanel();
        JButton btnView = new JButton("View All");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        btnPanel.add(btnView);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);

        // --- Table ---
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"User ID", "Username", "Password", "Role"});
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // --- Add to Frame ---
        add(inputPanel, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // --- Button Actions ---
        btnView.addActionListener(e -> loadUsers());
        btnUpdate.addActionListener(e -> updateUser());
        btnDelete.addActionListener(e -> deleteUser());

        setVisible(true);
    }

    private void loadUsers() {
        try {
            ResultSet rs = db.getAllUsers();
            model.setRowCount(0); // clear table
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading users!");
        }
    }

    private void updateUser() {
        int id = Integer.parseInt(txtId.getText());
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String role = txtRole.getText();

        if (db.updateUser(id, username, password, role)) {
            JOptionPane.showMessageDialog(this, "User updated successfully!");
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "Update failed!");
        }
    }

    private void deleteUser() {
        int id = Integer.parseInt(txtId.getText());
        if (db.deleteUser(id)) {
            JOptionPane.showMessageDialog(this, "User deleted successfully!");
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "Delete failed!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserGUI());
    }
}