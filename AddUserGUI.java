package com.digitalcourt.management;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddUserGUI extends JFrame {
    private DatabaseHandler db;
    private JTable table;
    private DefaultTableModel model;

    public AddUserGUI() {
        db = new DatabaseHandler();
        setTitle("User Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table setup
        model = new DefaultTableModel();
        table = new JTable(model);
        model.setColumnIdentifiers(new String[]{"ID", "Username", "Password", "Role"});
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel panel = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(refreshBtn);
        add(panel, BorderLayout.SOUTH);

        // Button actions
        addBtn.addActionListener(e -> addUser());
        updateBtn.addActionListener(e -> updateUser());
        deleteBtn.addActionListener(e -> deleteUser());
        refreshBtn.addActionListener(e -> loadUsers());

        loadUsers();
        setVisible(true);
    }

    private void loadUsers() {
        model.setRowCount(0);
        try {
            Statement st = db.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addUser() {
        JTextField username = new JTextField();
        JTextField password = new JTextField();
        JTextField role = new JTextField();
        Object[] message = {
                "Username:", username,
                "Password:", password,
                "Role:", role
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
                PreparedStatement pst = db.getConnection().prepareStatement(sql);
                pst.setString(1, username.getText());
                pst.setString(2, password.getText());
                pst.setString(3, role.getText());
                pst.executeUpdate();
                loadUsers();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding user.");
            }
        }
    }

    private void updateUser() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a user to update");
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        JTextField username = new JTextField((String) table.getValueAt(row, 1));
        JTextField password = new JTextField((String) table.getValueAt(row, 2));
        JTextField role = new JTextField((String) table.getValueAt(row, 3));
        Object[] message = {
                "Username:", username,
                "Password:", password,
                "Role:", role
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Update User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String sql = "UPDATE users SET username=?, password=?, role=? WHERE user_id=?";
                PreparedStatement pst = db.getConnection().prepareStatement(sql);
                pst.setString(1, username.getText());
                pst.setString(2, password.getText());
                pst.setString(3, role.getText());
                pst.setInt(4, id);
                pst.executeUpdate();
                loadUsers();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating user.");
            }
        }
    }

    private void deleteUser() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a user to delete");
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Delete User", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM users WHERE user_id=?";
                PreparedStatement pst = db.getConnection().prepareStatement(sql);
                pst.setInt(1, id);
                pst.executeUpdate();
                loadUsers();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting user. Check if user is linked to a case.");
            }
        }
    }

    public static void main(String[] args) {
        new AddUserGUI();
    }
}