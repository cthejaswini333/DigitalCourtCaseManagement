package com.digitalcourt.management;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminGUI extends JFrame {
    private DatabaseHandler db;
    private JTable table;
    private DefaultTableModel model;

    public AdminGUI() {
        db = new DatabaseHandler();
        setTitle("Admin Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table setup
        model = new DefaultTableModel();
        table = new JTable(model);
        model.setColumnIdentifiers(new String[]{"ID", "Username", "Password", "Email"});
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
        addBtn.addActionListener(e -> addAdmin());
        updateBtn.addActionListener(e -> updateAdmin());
        deleteBtn.addActionListener(e -> deleteAdmin());
        refreshBtn.addActionListener(e -> loadAdmins());

        loadAdmins();
        setVisible(true);
    }

    private void loadAdmins() {
        model.setRowCount(0);
        try {
            Statement st = db.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM admins");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("admin_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addAdmin() {
        JTextField username = new JTextField();
        JTextField password = new JTextField();
        JTextField email = new JTextField();
        Object[] message = {
                "Username:", username,
                "Password:", password,
                "Email:", email
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Admin", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String sql = "INSERT INTO admins (username, password, email) VALUES (?, ?, ?)";
                PreparedStatement pst = db.getConnection().prepareStatement(sql);
                pst.setString(1, username.getText());
                pst.setString(2, password.getText());
                pst.setString(3, email.getText());
                pst.executeUpdate();
                loadAdmins();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding admin.");
            }
        }
    }

    private void updateAdmin() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an admin to update");
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        JTextField username = new JTextField((String) table.getValueAt(row, 1));
        JTextField password = new JTextField((String) table.getValueAt(row, 2));
        JTextField email = new JTextField((String) table.getValueAt(row, 3));
        Object[] message = {
                "Username:", username,
                "Password:", password,
                "Email:", email
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Update Admin", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String sql = "UPDATE admins SET username=?, password=?, email=? WHERE admin_id=?";
                PreparedStatement pst = db.getConnection().prepareStatement(sql);
                pst.setString(1, username.getText());
                pst.setString(2, password.getText());
                pst.setString(3, email.getText());
                pst.setInt(4, id);
                pst.executeUpdate();
                loadAdmins();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating admin.");
            }
        }
    }

    private void deleteAdmin() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an admin to delete");
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this admin?", "Delete Admin", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM admins WHERE admin_id=?";
                PreparedStatement pst = db.getConnection().prepareStatement(sql);
                pst.setInt(1, id);
                pst.executeUpdate();
                loadAdmins();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting admin. Check if admin is linked to a case.");
            }
        }
    }

    public static void main(String[] args) {
        new AdminGUI();
    }
}