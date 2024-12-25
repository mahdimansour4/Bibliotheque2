package view;

import controller.UtilisateurController;
import model.Utilisateur;
import util.DocumentListenerAdapter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class UtilisateurPanel extends JPanel {
    private UtilisateurController utilisateurController;
    private JTable tableUtilisateurs;
    private DefaultTableModel tableModel;
    private JTextField txtNom, txtEmail, txtSearch;

    public UtilisateurPanel() {
        utilisateurController = new UtilisateurController();
        setLayout(new BorderLayout());

        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(20);
        txtSearch.getDocument().addDocumentListener(new DocumentListenerAdapter(this::rechercherUtilisateur));
        searchPanel.add(new JLabel("Rechercher (Nom ou Email):"));
        searchPanel.add(txtSearch);
        add(searchPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setPreferredSize(new Dimension(600, 300));
        tableModel = new DefaultTableModel(new String[]{"ID", "Nom", "Email"}, 0);
        tableUtilisateurs = new JTable(tableModel);

        // Enable sorting by setting a RowSorter
        TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(tableModel);
        tableUtilisateurs.setRowSorter(rowSorter);

        tableUtilisateurs.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableUtilisateurs.getSelectedRow() != -1) {
                int selectedRow = tableUtilisateurs.getSelectedRow();
                txtNom.setText(tableModel.getValueAt(selectedRow, 1).toString());
                txtEmail.setText(tableModel.getValueAt(selectedRow, 2).toString());
            }
        });

        tablePanel.add(new JScrollPane(tableUtilisateurs), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // Input fields at the top of the panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Détails de l'utilisateur"));
        inputPanel.setPreferredSize(new Dimension(300, 150)); // Adjust size

        JPanel fieldsPanel = new JPanel(new GridBagLayout()); // Inner panel for alignment
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH; // Align at the top

        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldsPanel.add(new JLabel("Nom:"), gbc);

        gbc.gridx = 1;
        txtNom = new JTextField(15); // Adjust field size
        fieldsPanel.add(txtNom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        fieldsPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        txtEmail = new JTextField(15); // Adjust field size
        fieldsPanel.add(txtEmail, gbc);

        inputPanel.add(fieldsPanel, BorderLayout.NORTH); // Align the fields to the top
        add(inputPanel, BorderLayout.EAST);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAdd = new JButton("Ajouter Utilisateur");
        btnAdd.addActionListener(e -> ajouterUtilisateur());
        JButton btnDelete = new JButton("Supprimer Utilisateur");
        btnDelete.addActionListener(e -> supprimerUtilisateur());
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.SOUTH);

        loadUtilisateurs();
    }

    private void ajouterUtilisateur() {
        String nom = txtNom.getText().trim();
        String email = txtEmail.getText().trim();

        // Validation conditions
        if (nom.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Adresse e-mail invalide. Veuillez entrer une adresse e-mail valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ensure the email is unique
        List<Utilisateur> utilisateurs = utilisateurController.listerUtilisateurs();
        for (Utilisateur u : utilisateurs) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                JOptionPane.showMessageDialog(this, "Cet e-mail est déjà utilisé. Veuillez en utiliser un autre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Create and add the user if all conditions are met
        Utilisateur utilisateur = new Utilisateur(
                utilisateurs.size() + 1, // Generate ID
                nom,
                email
        );

        utilisateurController.ajouterUtilisateur(utilisateur);
        JOptionPane.showMessageDialog(this, "Utilisateur ajouté avec succès.");
        loadUtilisateurs();
        clearInputFields();
    }

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

    private void rechercherUtilisateur() {
        String query = txtSearch.getText();
        List<Utilisateur> utilisateurs = utilisateurController.rechercherUtilisateur(query);
        tableModel.setRowCount(0);
        for (Utilisateur utilisateur : utilisateurs) {
            tableModel.addRow(new Object[]{
                    (Object) utilisateur.getId(),
                    utilisateur.getNom(),
                    utilisateur.getEmail()
            });
        }
    }

    public void loadUtilisateurs() {
        tableModel.setRowCount(0);
        List<Utilisateur> utilisateurs = utilisateurController.listerUtilisateurs();
        for (Utilisateur utilisateur : utilisateurs) {
            tableModel.addRow(new Object[]{
                    (Object) utilisateur.getId(),
                    utilisateur.getNom(),
                    utilisateur.getEmail()
            });
        }
    }

    private void clearInputFields() {
        txtNom.setText("");
        txtEmail.setText("");
        txtSearch.setText("");
    }
}
