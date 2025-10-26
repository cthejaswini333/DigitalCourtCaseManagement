package com.digitalcourt.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginGUI extends JFrame {
    private DatabaseHandler db;

    public LoginGUI() {
        db = new DatabaseHandler();

        setTitle("Digital Court Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 50, 100, 25);
        add(userLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(150, 50, 180, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 90, 100, 25);
        add(passLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 90, 180, 25);
        add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 140, 100, 30);
        add(loginBtn);

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter username and password");
                return;
            }

            try {
                // Check if user is an admin
                String sqlAdmin = "SELECT * FROM admins WHERE username=? AND password=?";
                PreparedStatement pstAdmin = db.getConnection().prepareStatement(sqlAdmin);
                pstAdmin.setString(1, username);
                pstAdmin.setString(2, password);
                ResultSet rsAdmin = pstAdmin.executeQuery();

                if (rsAdmin.next()) {
                    JOptionPane.showMessageDialog(this, "Admin login successful!");
                    new AdminGUI();
                    dispose();
                    return;
                }

                // Check if user is a normal user
                String sqlUser = "SELECT * FROM users WHERE username=? AND password=?";
                PreparedStatement pstUser = db.getConnection().prepareStatement(sqlUser);
                pstUser.setString(1, username);
                pstUser.setString(2, password);
                ResultSet rsUser = pstUser.executeQuery();

                if (rsUser.next()) {
                    JOptionPane.showMessageDialog(this, "User login successful!");
                    new AddCaseGUI(); // Users can access cases
                    dispose();
                    return;
                }

                // Invalid login
                JOptionPane.showMessageDialog(this, "Invalid username or password");

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error!");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}