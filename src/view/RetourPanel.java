package view;

import controller.EmpruntController;
import controller.LivreController;
import model.Emprunt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class RetourPanel extends JPanel {
    private final EmpruntController empruntController; // Use the provided controller
    private final LivrePanel livrePanel;
    private JTable tableRetours;
    private DefaultTableModel tableModel;

    public RetourPanel(EmpruntController empruntController, LivrePanel livrePanel) {
        this.empruntController = empruntController; // Assign the passed controller
        this.livrePanel = livrePanel;
        setLayout(new BorderLayout());

        // Table for borrowings that haven't been returned
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Livre ID", "Utilisateur ID", "Date Emprunt", "Date Retour Prévue"}, 0);
        tableRetours = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                // Safely convert to LocalDate for overdue highlighting
                if (column == 4) {
                    Object value = getValueAt(row, column);
                    if (value instanceof String) {
                        LocalDate returnDate = LocalDate.parse((String) value);
                        if (returnDate.isBefore(LocalDate.now())) { // Overdue
                            c.setBackground(Color.RED);
                            c.setForeground(Color.WHITE);
                        } else {
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                        }
                    }
                }
                return c;
            }
        };
        add(new JScrollPane(tableRetours), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnReturn = new JButton("Enregistrer Retour");
        btnReturn.addActionListener(e -> enregistrerRetour());
        buttonPanel.add(btnReturn);
        add(buttonPanel, BorderLayout.SOUTH);

        loadNonReturnedEmprunts();
    }

    public void loadNonReturnedEmprunts() {
        tableModel.setRowCount(0); // Clear table
        List<Emprunt> emprunts = empruntController.listerEmprunts();
        for (Emprunt emprunt : emprunts) {
            if (emprunt.getDateRetourEffective() == null) { // Only include non-returned borrowings
                tableModel.addRow(new Object[]{
                        (Object) emprunt.getId(),
                        (Object) emprunt.getLivreId(),
                        (Object) emprunt.getUtilisateurId(),
                        emprunt.getDateEmprunt().toString(), // Convert to String
                        emprunt.getDateRetourPrevue().toString() // Convert to String
                });
            }
        }
    }


    private void enregistrerRetour() {
        int selectedRow = tableRetours.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un emprunt pour enregistrer le retour.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int empruntId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            empruntController.enregistrerRetour(empruntId, LocalDate.now());
            JOptionPane.showMessageDialog(this, "Retour enregistré avec succès.");

            // Refresh panels
            loadNonReturnedEmprunts();
            livrePanel.loadLivres(); // Explicitly refresh LivrePanel
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement du retour: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


}
