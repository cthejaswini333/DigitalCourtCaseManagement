package com.digitalcourt.management;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddCaseGUI extends JFrame {
    private DatabaseHandler db;
    private JTable table;
    private DefaultTableModel model;

    public AddCaseGUI() {
        db = new DatabaseHandler();
        setTitle("Case Management");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table setup
        model = new DefaultTableModel();
        table = new JTable(model);
        model.setColumnIdentifiers(new String[]{"ID", "Title", "Description", "Filed By (User ID)", "Assigned To (Admin ID)", "Status", "Date Filed"});
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
        addBtn.addActionListener(e -> addCase());
        updateBtn.addActionListener(e -> updateCase());
        deleteBtn.addActionListener(e -> deleteCase());
        refreshBtn.addActionListener(e -> loadCases());

        loadCases();
        setVisible(true);
    }

    private void loadCases() {
        model.setRowCount(0);
        try {
            Statement st = db.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM cases");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("case_id"),
                        rs.getString("case_title"),
                        rs.getString("case_description"),
                        rs.getInt("filed_by"),
                        rs.getInt("assigned_to"),
                        rs.getString("status"),
                        rs.getDate("date_filed")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addCase() {
        JTextField title = new JTextField();
        JTextField description = new JTextField();
        JTextField filedBy = new JTextField();
        JTextField assignedTo = new JTextField();
        JTextField status = new JTextField();
        JTextField dateFiled = new JTextField();
        Object[] message = {
                "Case Title:", title,
                "Description:", description,
                "Filed By (User ID):", filedBy,
                "Assigned To (Admin ID):", assignedTo,
                "Status:", status,
                "Date Filed (YYYY-MM-DD):", dateFiled
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Case", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String sql = "INSERT INTO cases (case_title, case_description, filed_by, assigned_to, status, date_filed) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = db.getConnection().prepareStatement(sql);
                pst.setString(1, title.getText());
                pst.setString(2, description.getText());
                pst.setInt(3, Integer.parseInt(filedBy.getText()));
                pst.setInt(4, Integer.parseInt(assignedTo.getText()));
                pst.setString(5, status.getText());
                pst.setString(6, dateFiled.getText());
                pst.executeUpdate();
                loadCases();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding case. Check User ID / Admin ID exists.");
            } catch (NumberFormatException nf) {
                JOptionPane.showMessageDialog(this, "Filed By and Assigned To must be valid numbers (IDs).");
            }
        }
    }

    private void updateCase() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a case to update");
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        JTextField title = new JTextField((String) table.getValueAt(row, 1));
        JTextField description = new JTextField((String) table.getValueAt(row, 2));
        JTextField filedBy = new JTextField(table.getValueAt(row, 3).toString());
        JTextField assignedTo = new JTextField(table.getValueAt(row, 4).toString());
        JTextField status = new JTextField((String) table.getValueAt(row, 5));
        JTextField dateFiled = new JTextField(table.getValueAt(row, 6).toString());

        Object[] message = {
                "Case Title:", title,
                "Description:", description,
                "Filed By (User ID):", filedBy,
                "Assigned To (Admin ID):", assignedTo,
                "Status:", status,
                "Date Filed (YYYY-MM-DD):", dateFiled
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Update Case", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String sql = "UPDATE cases SET case_title=?, case_description=?, filed_by=?, assigned_to=?, status=?, date_filed=? WHERE case_id=?";
                PreparedStatement pst = db.getConnection().prepareStatement(sql);
                pst.setString(1, title.getText());
                pst.setString(2, description.getText());
                pst.setInt(3, Integer.parseInt(filedBy.getText()));
                pst.setInt(4, Integer.parseInt(assignedTo.getText()));
                pst.setString(5, status.getText());
                pst.setString(6, dateFiled.getText());
                pst.setInt(7, id);
                pst.executeUpdate();
                loadCases();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating case. Check User ID / Admin ID exists.");
            } catch (NumberFormatException nf) {
                JOptionPane.showMessageDialog(this, "Filed By and Assigned To must be valid numbers (IDs).");
            }
        }
    }

    private void deleteCase() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a case to delete");
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this case?", "Delete Case", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM cases WHERE case_id=?";
                PreparedStatement pst = db.getConnection().prepareStatement(sql);
                pst.setInt(1, id);
                pst.executeUpdate();
                loadCases();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting case.");
            }
        }
    }

    public static void main(String[] args) {
        new AddCaseGUI();
    }
}