package view;

import controller.LivreController;
import model.Livre;
import util.DocumentListenerAdapter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

/**
 * Panneau pour gérer les livres dans la bibliothèque.
 * Permet l'ajout, la modification, la suppression et la recherche de livres.
 */
public class LivrePanel extends JPanel {
    private LivreController livreController; // Contrôleur pour gérer les livres
    private JTable tableLivres; // Tableau pour afficher les livres
    private DefaultTableModel tableModel; // Modèle des données pour le tableau
    private TableRowSorter<DefaultTableModel> rowSorter; // Permet de trier les colonnes du tableau
    private JTextField txtISBN, txtTitre, txtAuteur, txtGenre, txtQuantite, txtSearch; // Champs pour l'entrée utilisateur

    /**
     * Constructeur de LivrePanel.
     * Configure l'interface utilisateur pour gérer les livres.
     */
    public LivrePanel() {
        this.livreController = new LivreController();
        setLayout(new BorderLayout());

        // Barre de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(20);
        txtSearch.getDocument().addDocumentListener(new DocumentListenerAdapter(this::rechercherLivre));
        searchPanel.add(new JLabel("Rechercher (Titre, Auteur ou ISBN):"));
        searchPanel.add(txtSearch);
        add(searchPanel, BorderLayout.NORTH);

        // Tableau pour afficher les livres
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setPreferredSize(new Dimension(700, 300)); // Taille ajustée pour le tableau
        tableModel = new DefaultTableModel(new String[]{"ID", "ISBN", "Titre", "Auteur", "Genre", "Quantité"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 5) return Integer.class; // Colonnes ID et Quantité en entier
                return String.class; // Autres colonnes en chaîne de caractères
            }
        };
        tableLivres = new JTable(tableModel);

        // Permettre le tri des colonnes
        rowSorter = new TableRowSorter<>(tableModel);
        tableLivres.setRowSorter(rowSorter);

        // Pré-remplir les champs lors de la sélection d'une ligne
        tableLivres.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableLivres.getSelectedRow() != -1) {
                int selectedRow = tableLivres.getSelectedRow();
                txtISBN.setText(tableModel.getValueAt(selectedRow, 1).toString());
                txtTitre.setText(tableModel.getValueAt(selectedRow, 2).toString());
                txtAuteur.setText(tableModel.getValueAt(selectedRow, 3).toString());
                txtGenre.setText(tableModel.getValueAt(selectedRow, 4).toString());
                txtQuantite.setText(tableModel.getValueAt(selectedRow, 5).toString());
            }
        });

        tablePanel.add(new JScrollPane(tableLivres), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // Champs de saisie pour les livres
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setPreferredSize(new Dimension(250, 100)); // Ajuster la taille des champs
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(new JLabel("ISBN:"));
        txtISBN = new JTextField(25);
        inputPanel.add(txtISBN);
        inputPanel.add(new JLabel("Titre:"));
        txtTitre = new JTextField(25);
        inputPanel.add(txtTitre);
        inputPanel.add(new JLabel("Auteur:"));
        txtAuteur = new JTextField(25);
        inputPanel.add(txtAuteur);
        inputPanel.add(new JLabel("Genre:"));
        txtGenre = new JTextField(25);
        inputPanel.add(txtGenre);
        inputPanel.add(new JLabel("Quantité:"));
        txtQuantite = new JTextField(25);
        inputPanel.add(txtQuantite);
        add(inputPanel, BorderLayout.EAST);

        // Boutons pour les actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAdd = new JButton("Ajouter Livre");
        btnAdd.addActionListener(e -> ajouterLivre());
        JButton btnModify = new JButton("Modifier Livre");
        btnModify.addActionListener(e -> modifierLivre());
        JButton btnDelete = new JButton("Supprimer Livre");
        btnDelete.addActionListener(e -> supprimerLivre());
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnModify);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.SOUTH);

        loadLivres(); // Charger la liste des livres au démarrage
    }

    /**
     * Ajoute un nouveau livre à la bibliothèque.
     * Vérifie les champs et actualise le tableau après l'ajout.
     */
    private void ajouterLivre() {
        try {
            Livre livre = new Livre(
                    livreController.listerLivres().size() + 1, // Génère un ID
                    txtISBN.getText(),
                    txtTitre.getText(),
                    txtAuteur.getText(),
                    0, // Valeur par défaut pour une colonne non utilisée
                    txtGenre.getText(),
                    Integer.parseInt(txtQuantite.getText())
            );
            livreController.ajouterLivre(livre);
            loadLivres(); // Actualiser le tableau
            clearInputFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une quantité valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Modifie les informations d'un livre sélectionné.
     * Vérifie les champs et actualise le tableau après modification.
     */
    private void modifierLivre() {
        int selectedRow = tableLivres.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un livre à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Livre livre = new Livre(
                    Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString()), // ID
                    txtISBN.getText(),
                    txtTitre.getText(),
                    txtAuteur.getText(),
                    0,
                    txtGenre.getText(),
                    Integer.parseInt(txtQuantite.getText())
            );

            livreController.modifierLivre(livre);
            JOptionPane.showMessageDialog(this, "Livre modifié avec succès.");
            loadLivres(); // Actualiser le tableau
            clearInputFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une quantité valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Supprime un livre sélectionné dans le tableau.
     */
    private void supprimerLivre() {
        int selectedRow = tableLivres.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un livre à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        livreController.supprimerLivre(id);
        loadLivres(); // Actualiser le tableau
    }

    /**
     * Recherche des livres en fonction du texte saisi dans le champ de recherche.
     * Si le champ est vide, tous les livres sont affichés.
     */
    private void rechercherLivre() {
        String query = txtSearch.getText().trim();
        tableModel.setRowCount(0); // Effacer le tableau

        List<Livre> livres;
        if (query.isEmpty()) {
            livres = livreController.listerLivres();
        } else {
            livres = livreController.rechercherLivres(query);
        }

        for (Livre livre : livres) {
            tableModel.addRow(new Object[]{
                    livre.getId(),
                    livre.getIsbn(),
                    livre.getTitre(),
                    livre.getAuteur(),
                    livre.getGenre(),
                    livre.getQuantite()
            });
        }
    }

    /**
     * Charge et affiche tous les livres dans le tableau.
     */
    public void loadLivres() {
        tableModel.setRowCount(0); // Effacer le tableau
        List<Livre> livres = livreController.listerLivres();
        for (Livre livre : livres) {
            tableModel.addRow(new Object[]{
                    livre.getId(),
                    livre.getIsbn(),
                    livre.getTitre(),
                    livre.getAuteur(),
                    livre.getGenre(),
                    livre.getQuantite()
            });
        }
    }

    /**
     * Réinitialise tous les champs de saisie à des valeurs vides.
     */
    private void clearInputFields() {
        txtISBN.setText("");
        txtTitre.setText("");
        txtAuteur.setText("");
        txtGenre.setText("");
        txtQuantite.setText("");
    }
}
