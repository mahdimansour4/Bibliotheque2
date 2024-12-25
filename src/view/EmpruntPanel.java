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

public class EmpruntPanel extends JPanel {
    private final EmpruntController empruntController;
    private final RetourPanel retourPanel; // Reference to RetourPanel
    private final LivrePanel livrePanel;
    private JTable tableEmprunts;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter; // For column sorting
    private JTextField txtSearch; // Unified search field


    public EmpruntPanel(EmpruntController empruntController, RetourPanel retourPanel, LivrePanel livrePanel) {
        this.empruntController = empruntController;
        this.retourPanel = retourPanel; // Assign RetourPanel reference
        this.livrePanel = livrePanel;
        setLayout(new BorderLayout());

        // Initialize UI
        initUI();
        loadFilteredEmprunts(""); // Load all data initially
    }

    private void initUI() {
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(20);
        txtSearch.getDocument().addDocumentListener(new DocumentListenerAdapter(() -> loadFilteredEmprunts(txtSearch.getText())));
        searchPanel.add(new JLabel("Rechercher (Livre ID, Utilisateur ID):"));
        searchPanel.add(txtSearch);
        add(searchPanel, BorderLayout.NORTH);

        // Table for borrowings
        tableModel = new DefaultTableModel(new String[]{"ID", "Livre ID", "Utilisateur ID", "Date Emprunt", "Date Retour Prévue", "Date Retour Effective"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: // ID column
                    case 1: // Livre ID column
                    case 2: // Utilisateur ID column
                        return Integer.class; // Treat as integers
                    case 3: // Date Emprunt column
                    case 4: // Date Retour Prévue column
                    case 5: // Date Retour Effective column
                        return LocalDate.class; // Treat as dates
                    default:
                        return String.class; // Default to string
                }
            }
        };

        tableEmprunts = new JTable(tableModel);

        // Enable sorting by setting a RowSorter
        rowSorter = new TableRowSorter<>(tableModel);
        tableEmprunts.setRowSorter(rowSorter);

        add(new JScrollPane(tableEmprunts), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAdd = new JButton("Ajouter Emprunt");
        btnAdd.addActionListener(e -> ajouterEmprunt());
        buttonPanel.add(btnAdd);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void ajouterEmprunt() {
        try {
            int livreId = Integer.parseInt(JOptionPane.showInputDialog("Entrer l'ID du Livre:"));
            int utilisateurId = Integer.parseInt(JOptionPane.showInputDialog("Entrer l'ID de l'Utilisateur:"));

            Emprunt emprunt = new Emprunt(
                    empruntController.listerEmprunts().size() + 1, // Generate ID
                    livreId,
                    utilisateurId,
                    LocalDate.now(), // Date Emprunt
                    LocalDate.now().plusDays(14), // Date Retour Prévue
                    null // Date Retour Effective
            );

            empruntController.ajouterEmprunt(emprunt); // Add borrowing to controller
            JOptionPane.showMessageDialog(this, "Emprunt ajouté avec succès.");

            // Refresh panels
            loadFilteredEmprunts("");
            retourPanel.loadNonReturnedEmprunts(); // Refresh RetourPanel
            livrePanel.loadLivres(); // Explicitly refresh LivrePanel
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Entrée invalide. Veuillez entrer des nombres pour les IDs.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadFilteredEmprunts(String query) {
        tableModel.setRowCount(0); // Clear the table to avoid duplication

        List<Emprunt> emprunts;
        if (query.isEmpty()) {
            // If search query is empty, load all emprunts
            emprunts = empruntController.listerEmprunts();
        } else {
            // Filter emprunts based on the search query
            emprunts = empruntController.listerEmprunts().stream()
                    .filter(emprunt -> String.valueOf(emprunt.getLivreId()).contains(query) ||
                            String.valueOf(emprunt.getUtilisateurId()).contains(query))
                    .toList();
        }

        // Populate the table with the filtered or complete list of emprunts
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
