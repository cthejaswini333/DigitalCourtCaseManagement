package com.digitalcourt.management;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1, 10, 10));
        setLocationRelativeTo(null);

        JButton manageAdmins = new JButton("Manage Admins");
        JButton manageUsers = new JButton("Manage Users");
        JButton manageCases = new JButton("Manage Cases");
        JButton addCaseBtn = new JButton("Add New Case");
        JButton logoutBtn = new JButton("Logout");

        add(manageAdmins);
        add(manageUsers);
        add(manageCases);
        add(addCaseBtn);
        add(logoutBtn);

        manageAdmins.addActionListener(e -> new TestDriver().viewAdmins());
        manageUsers.addActionListener(e -> new TestDriver().viewUsers());
        manageCases.addActionListener(e -> new TestDriver().viewCases());
        addCaseBtn.addActionListener(e -> new AddCase());

        logoutBtn.addActionListener(e -> {
            new LoginGUI();
            dispose();
        });

        setVisible(true);
    }
}