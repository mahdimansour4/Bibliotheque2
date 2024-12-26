package view;

import controller.EmpruntController;
import model.Emprunt;
import util.DocumentListenerAdapter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Panneau pour gérer les emprunts dans le système de bibliothèque.
 * Permet d'ajouter, rechercher et afficher les emprunts existants.
 */
public class EmpruntPanel extends JPanel {
    private final EmpruntController empruntController; // Contrôleur pour gérer les emprunts
    private final RetourPanel retourPanel; // Référence au panneau des retours pour les actualisations
    private final LivrePanel livrePanel; // Référence au panneau des livres pour les actualisations
    private JTable tableEmprunts; // Tableau pour afficher les emprunts
    private DefaultTableModel tableModel; // Modèle des données pour le tableau
    private TableRowSorter<DefaultTableModel> rowSorter; // Permet le tri des colonnes dans le tableau
    private JTextField txtSearch; // Champ de recherche pour les emprunts

    /**
     * Constructeur de la classe EmpruntPanel.
     * Configure l'interface utilisateur et charge les emprunts initiaux.
     *
     * @param empruntController Contrôleur pour gérer les emprunts.
     * @param retourPanel       Panneau des retours pour actualiser les emprunts non retournés.
     * @param livrePanel        Panneau des livres pour actualiser les données des livres.
     */
    public EmpruntPanel(EmpruntController empruntController, RetourPanel retourPanel, LivrePanel livrePanel) {
        this.empruntController = empruntController;
        this.retourPanel = retourPanel;
        this.livrePanel = livrePanel;
        setLayout(new BorderLayout());

        // Initialisation de l'interface utilisateur
        initUI();
        loadFilteredEmprunts(""); // Charger tous les emprunts initialement
    }

    /**
     * Initialise les composants de l'interface utilisateur, y compris le tableau et la barre de recherche.
     */
    private void initUI() {
        // Panneau de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(20);
        txtSearch.getDocument().addDocumentListener(new DocumentListenerAdapter(() -> loadFilteredEmprunts(txtSearch.getText())));
        searchPanel.add(new JLabel("Rechercher (Livre ID, Utilisateur ID):"));
        searchPanel.add(txtSearch);
        add(searchPanel, BorderLayout.NORTH);

        // Tableau pour afficher les emprunts
        tableModel = new DefaultTableModel(new String[]{"ID", "Livre ID", "Utilisateur ID", "Date Emprunt", "Date Retour Prévue", "Date Retour Effective"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: // ID column
                    case 1: // Livre ID column
                    case 2: // Utilisateur ID column
                        return Integer.class; // Colonnes numériques
                    case 3: // Date Emprunt column
                    case 4: // Date Retour Prévue column
                    case 5: // Date Retour Effective column
                        return LocalDate.class; // Colonnes de type date
                    default:
                        return String.class; // Valeur par défaut
                }
            }
        };

        tableEmprunts = new JTable(tableModel);

        // Permettre le tri des colonnes
        rowSorter = new TableRowSorter<>(tableModel);
        tableEmprunts.setRowSorter(rowSorter);

        add(new JScrollPane(tableEmprunts), BorderLayout.CENTER);

        // Bouton pour ajouter un emprunt
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAdd = new JButton("Ajouter Emprunt");
        btnAdd.addActionListener(e -> ajouterEmprunt());
        buttonPanel.add(btnAdd);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Ajoute un nouvel emprunt au système.
     * Demande à l'utilisateur de fournir les IDs du livre et de l'utilisateur.
     * Actualise les panneaux associés après l'ajout.
     */
    private void ajouterEmprunt() {
        try {
            int livreId = Integer.parseInt(JOptionPane.showInputDialog("Entrer l'ID du Livre:"));
            int utilisateurId = Integer.parseInt(JOptionPane.showInputDialog("Entrer l'ID de l'Utilisateur:"));

            Emprunt emprunt = new Emprunt(
                    empruntController.listerEmprunts().size() + 1, // Génération de l'ID
                    livreId,
                    utilisateurId,
                    LocalDate.now(), // Date de l'emprunt
                    LocalDate.now().plusDays(14), // Date de retour prévue
                    null // Pas encore retourné
            );

            empruntController.ajouterEmprunt(emprunt); // Ajouter l'emprunt via le contrôleur
            JOptionPane.showMessageDialog(this, "Emprunt ajouté avec succès.");

            // Actualiser les panneaux
            loadFilteredEmprunts(""); // Actualiser ce panneau
            retourPanel.loadNonReturnedEmprunts(); // Actualiser le panneau des retours
            livrePanel.loadLivres(); // Actualiser le panneau des livres
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Entrée invalide. Veuillez entrer des nombres pour les IDs.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Charge et filtre les emprunts en fonction de la requête de recherche.
     * Si la requête est vide, tous les emprunts sont affichés.
     *
     * @param query Texte de recherche pour filtrer les emprunts.
     */
    public void loadFilteredEmprunts(String query) {
        tableModel.setRowCount(0); // Effacer les données existantes dans le tableau

        List<Emprunt> emprunts;
        if (query.isEmpty()) {
            // Charger tous les emprunts si la recherche est vide
            emprunts = empruntController.listerEmprunts();
        } else {
            // Filtrer les emprunts en fonction de la recherche
            emprunts = empruntController.listerEmprunts().stream()
                    .filter(emprunt -> String.valueOf(emprunt.getLivreId()).contains(query) ||
                            String.valueOf(emprunt.getUtilisateurId()).contains(query))
                    .toList();
        }

        // Ajouter les emprunts filtrés au tableau
        for (Emprunt emprunt : emprunts) {
            tableModel.addRow(new Object[]{
                    emprunt.getId(),
                    emprunt.getLivreId(),
                    emprunt.getUtilisateurId(),
                    emprunt.getDateEmprunt(),
                    emprunt.getDateRetourPrevue(),
                    emprunt.getDateRetourEffective() == null ? "Non retourné" : emprunt.getDateRetourEffective()
            });
        }
    }
}
