package view;

import main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Formulaire de connexion pour le système de gestion de bibliothèque.
 * Permet à un utilisateur de s'authentifier avant d'accéder à l'application principale.
 */
public class LoginForm extends JFrame {
    private JTextField txtUsername; // Champ de saisie pour le nom d'utilisateur
    private JPasswordField txtPassword; // Champ de saisie pour le mot de passe

    /**
     * Constructeur de la classe LoginForm.
     * Configure l'interface utilisateur et initialise les composants nécessaires.
     */
    public LoginForm() {
        setTitle("Système de Gestion de Bibliothèque - Connexion");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panneau d'en-tête
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180)); // Couleur bleu acier
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblHeader = new JLabel("Système de Gestion de Bibliothèque", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Panneau central pour les champs de saisie
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espacement entre les composants
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
        txtPassword.addActionListener(this::handleLogin); // Connexion via la touche Entrée
        centerPanel.add(txtPassword, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Panneau inférieur pour les boutons
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton btnLogin = new JButton("Connexion");
        btnLogin.setBackground(new Color(34, 139, 34)); // Couleur verte
        btnLogin.setForeground(Color.WHITE); // Texte blanc
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(this::handleLogin);
        southPanel.add(btnLogin);
        add(southPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Centre la fenêtre sur l'écran
    }

    /**
     * Gère l'action de connexion.
     * Vérifie les identifiants saisis et ouvre l'application principale si la connexion est réussie.
     *
     * @param e L'événement déclenché par le bouton ou la touche Entrée.
     */
    private void handleLogin(ActionEvent e) {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        // Vérification des identifiants
        if ("admin".equals(username) && "password".equals(password)) {
            JOptionPane.showMessageDialog(this, "Connexion réussie ! Bienvenue, " + username + ".");
            dispose(); // Ferme le formulaire de connexion
            Main.launchMainApp(); // Lance l'application principale
        } else {
            JOptionPane.showMessageDialog(this, "Identifiants invalides. Veuillez réessayer.", "Connexion échouée", JOptionPane.ERROR_MESSAGE);
        }
    }
}
