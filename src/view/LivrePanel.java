package view;

import controller.LivreController;
import model.Livre;
import util.DocumentListenerAdapter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class LivrePanel extends JPanel {
    private LivreController livreController;
    private JTable tableLivres;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter; // For column sorting
    private JTextField txtISBN, txtTitre, txtAuteur, txtGenre, txtQuantite, txtSearch;

    public LivrePanel() {
        this.livreController = new LivreController();
        setLayout(new BorderLayout());

        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(20);
        txtSearch.getDocument().addDocumentListener(new DocumentListenerAdapter(this::rechercherLivre));
        searchPanel.add(new JLabel("Rechercher (Titre, Auteur ou ISBN):"));
        searchPanel.add(txtSearch);
        add(searchPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setPreferredSize(new Dimension(700, 300)); // Set smaller size for the table panel
        tableModel = new DefaultTableModel(new String[]{"ID", "ISBN", "Titre", "Auteur", "Genre", "Quantité"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 5) return Integer.class; // ID and Quantité are integers
                return String.class; // All other columns are strings
            }
        };
        tableLivres = new JTable(tableModel);

        // Enable sorting by setting a RowSorter
        rowSorter = new TableRowSorter<>(tableModel);
        tableLivres.setRowSorter(rowSorter);

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

        // Input fields
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setPreferredSize(new Dimension(400, 300)); // Increase size of the input fields
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(new JLabel("ISBN:"));
        txtISBN = new JTextField(25); // Set larger text fields
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

        // Buttons
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

        loadLivres();
    }

    private void ajouterLivre() {
        try {
            Livre livre = new Livre(
                    livreController.listerLivres().size() + 1, // Generate ID
                    txtISBN.getText(),
                    txtTitre.getText(),
                    txtAuteur.getText(),
                    0, // Default value for column not being used
                    txtGenre.getText(),
                    Integer.parseInt(txtQuantite.getText())
            );
            livreController.ajouterLivre(livre);
            loadLivres(); // Refresh table
            clearInputFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une quantité valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


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

            livreController.modifierLivre(livre); // Update the book
            JOptionPane.showMessageDialog(this, "Livre modifié avec succès.");
            loadLivres(); // Refresh the table
            clearInputFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une quantité valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerLivre() {
        int selectedRow = tableLivres.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un livre à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        livreController.supprimerLivre(id);
        loadLivres(); // Refresh table
    }

    private void rechercherLivre() {
        String query = txtSearch.getText();
        List<Livre> livres = livreController.rechercherLivres(query);
        tableModel.setRowCount(0);
        for (Livre livre : livres) {
            tableModel.addRow(new Object[]{
                    livre.getId(), livre.getIsbn(), livre.getTitre(), livre.getAuteur(), livre.getGenre(), livre.getQuantite()
            });
        }
    }

    public void loadLivres() {
        tableModel.setRowCount(0); // Clear the table
        List<Livre> livres = livreController.listerLivres(); // Fetch updated book list
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

    private void clearInputFields() {
        txtISBN.setText("");
        txtTitre.setText("");
        txtAuteur.setText("");
        txtGenre.setText("");
        txtQuantite.setText("");
    }
}
