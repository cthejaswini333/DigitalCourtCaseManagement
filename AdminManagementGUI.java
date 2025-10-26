package com.digitalcourt.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminManagementGUI extends JFrame {

    public AdminManagementGUI() {
        setTitle("Admin Dashboard - Digital Court Management");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, Admin!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(welcomeLabel);

        JButton manageAdminsBtn = new JButton("Manage Admins");
        JButton manageUsersBtn = new JButton("Manage Users");
        JButton manageCasesBtn = new JButton("Manage Cases");

        add(manageAdminsBtn);
        add(manageUsersBtn);
        add(manageCasesBtn);

        // Button actions
        manageAdminsBtn.addActionListener(e -> {
            new AdminGUI(); // Opens Admin management GUI
        });

        manageUsersBtn.addActionListener(e -> {
            new AddUserGUI(); // Opens User management GUI
        });

        manageCasesBtn.addActionListener(e -> {
            new AddCaseGUI(); // Opens Case management GUI
        });

        setVisible(true);
    }
    public static void main(String[] args) {
        new AdminManagementGUI();
    }
}