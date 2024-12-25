package view;

import main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginForm() {
        setTitle("Système de Gestion de Bibliothèque - Connexion");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180)); // Steel blue
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblHeader = new JLabel("Bienvenue au Système de Gestion de Bibliothèque", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel for Inputs
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(new JLabel("Nom d'utilisateur :"), gbc);

        gbc.gridx = 1;
        txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(200, 30));
        centerPanel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(new JLabel("Mot de passe :"), gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(200, 30));
        txtPassword.addActionListener(this::handleLogin); // Trigger login on Enter
        centerPanel.add(txtPassword, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // South Panel for Buttons
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton btnLogin = new JButton("Connexion");
        btnLogin.setBackground(new Color(34, 139, 34)); // Green background
        btnLogin.setForeground(Color.WHITE); // White text
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(this::handleLogin);
        southPanel.add(btnLogin);
        add(southPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Center the window
    }

    private void handleLogin(ActionEvent e) {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if ("admin".equals(username) && "password".equals(password)) {
            JOptionPane.showMessageDialog(this, "Connexion réussie ! Bienvenue, " + username + ".");
            dispose();
            Main.launchMainApp();
        } else {
            JOptionPane.showMessageDialog(this, "Identifiants invalides. Veuillez réessayer.", "Connexion échouée", JOptionPane.ERROR_MESSAGE);
        }
    }
}
