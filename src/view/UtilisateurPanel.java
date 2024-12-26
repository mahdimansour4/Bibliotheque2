package view;

import controller.UtilisateurController;
import model.Utilisateur;
import util.DocumentListenerAdapter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

/**
 * Panneau pour gérer les utilisateurs dans la bibliothèque.
 * Permet l'ajout, la suppression et la recherche des utilisateurs.
 */
public class UtilisateurPanel extends JPanel {
    private UtilisateurController utilisateurController; // Contrôleur pour gérer les utilisateurs
    private JTable tableUtilisateurs; // Tableau pour afficher les utilisateurs
    private DefaultTableModel tableModel; // Modèle des données pour le tableau
    private JTextField txtNom, txtEmail, txtSearch; // Champs de saisie pour les utilisateurs

    /**
     * Constructeur de la classe UtilisateurPanel.
     * Configure l'interface utilisateur pour la gestion des utilisateurs.
     */
    public UtilisateurPanel() {
        utilisateurController = new UtilisateurController();
        setLayout(new BorderLayout());

        // Barre de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(20);
        txtSearch.getDocument().addDocumentListener(new DocumentListenerAdapter(this::rechercherUtilisateur));
        searchPanel.add(new JLabel("Rechercher (Nom ou Email):"));
        searchPanel.add(txtSearch);
        add(searchPanel, BorderLayout.NORTH);

        // Tableau pour afficher les utilisateurs
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setPreferredSize(new Dimension(600, 300));
        tableModel = new DefaultTableModel(new String[]{"ID", "Nom", "Email"}, 0);
        tableUtilisateurs = new JTable(tableModel);

        // Permettre le tri des colonnes
        TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(tableModel);
        tableUtilisateurs.setRowSorter(rowSorter);

        // Pré-remplir les champs lors de la sélection d'une ligne
        tableUtilisateurs.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableUtilisateurs.getSelectedRow() != -1) {
                int selectedRow = tableUtilisateurs.getSelectedRow();
                txtNom.setText(tableModel.getValueAt(selectedRow, 1).toString());
                txtEmail.setText(tableModel.getValueAt(selectedRow, 2).toString());
            }
        });

        tablePanel.add(new JScrollPane(tableUtilisateurs), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // Champs de saisie
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Détails de l'utilisateur"));
        inputPanel.setPreferredSize(new Dimension(300, 150));

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldsPanel.add(new JLabel("Nom:"), gbc);

        gbc.gridx = 1;
        txtNom = new JTextField(15);
        fieldsPanel.add(txtNom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        fieldsPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        txtEmail = new JTextField(15);
        fieldsPanel.add(txtEmail, gbc);

        inputPanel.add(fieldsPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.EAST);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAdd = new JButton("Ajouter Utilisateur");
        btnAdd.addActionListener(e -> ajouterUtilisateur());
        JButton btnDelete = new JButton("Supprimer Utilisateur");
        btnDelete.addActionListener(e -> supprimerUtilisateur());
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.SOUTH);

        loadUtilisateurs(); // Charger les utilisateurs au démarrage
    }

    /**
     * Ajoute un nouvel utilisateur à la bibliothèque.
     * Vérifie les champs et actualise le tableau après l'ajout.
     */
    private void ajouterUtilisateur() {
        String nom = txtNom.getText().trim();
        String email = txtEmail.getText().trim();

        // Conditions de validation
        if (nom.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Adresse e-mail invalide. Veuillez entrer une adresse e-mail valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Vérifie que l'email est unique
        List<Utilisateur> utilisateurs = utilisateurController.listerUtilisateurs();
        for (Utilisateur u : utilisateurs) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                JOptionPane.showMessageDialog(this, "Cet e-mail est déjà utilisé. Veuillez en utiliser un autre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Crée et ajoute l'utilisateur si toutes les conditions sont remplies
        Utilisateur utilisateur = new Utilisateur(
                utilisateurs.size() + 1, // Génère un ID
                nom,
                email
        );

        utilisateurController.ajouterUtilisateur(utilisateur);
        JOptionPane.showMessageDialog(this, "Utilisateur ajouté avec succès.");
        loadUtilisateurs(); // Actualiser le tableau
        clearInputFields();
    }

    /**
     * Supprime un utilisateur sélectionné dans le tableau.
     * Actualise le tableau après la suppression.
     */
    private void supprimerUtilisateur() {
        int selectedRow = tableUtilisateurs.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        utilisateurController.supprimerUtilisateur(id);
        loadUtilisateurs();
    }

    /**
     * Recherche des utilisateurs en fonction du texte saisi dans le champ de recherche.
     * Si le champ est vide, tous les utilisateurs sont affichés.
     */
    private void rechercherUtilisateur() {
        String query = txtSearch.getText();
        List<Utilisateur> utilisateurs = utilisateurController.rechercherUtilisateur(query);
        tableModel.setRowCount(0);
        for (Utilisateur utilisateur : utilisateurs) {
            tableModel.addRow(new Object[]{
                    utilisateur.getId(),
                    utilisateur.getNom(),
                    utilisateur.getEmail()
            });
        }
    }

    /**
     * Charge et affiche tous les utilisateurs dans le tableau.
     */
    public void loadUtilisateurs() {
        tableModel.setRowCount(0);
        List<Utilisateur> utilisateurs = utilisateurController.listerUtilisateurs();
        for (Utilisateur utilisateur : utilisateurs) {
            tableModel.addRow(new Object[]{
                    utilisateur.getId(),
                    utilisateur.getNom(),
                    utilisateur.getEmail()
            });
        }
    }

    /**
     * Réinitialise tous les champs de saisie et le champ de recherche.
     */
    private void clearInputFields() {
        txtNom.setText("");
        txtEmail.setText("");
        txtSearch.setText("");
    }
}
