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

/**
 * Panneau pour gérer les retours d'emprunts non encore retournés.
 * Permet de visualiser les emprunts en attente de retour et d'enregistrer
 * les retours avec mise à jour des données associées.
 */
public class RetourPanel extends JPanel {
    private final EmpruntController empruntController; // Contrôleur pour gérer les emprunts
    private final LivrePanel livrePanel; // Référence au panneau des livres pour actualisation
    private JTable tableRetours; // Tableau affichant les emprunts non retournés
    private DefaultTableModel tableModel; // Modèle des données pour le tableau

    /**
     * Constructeur du panneau RetourPanel.
     * Initialise les composants de l'interface utilisateur et charge les emprunts non retournés.
     *
     * @param empruntController Contrôleur pour gérer les emprunts.
     * @param livrePanel        Panneau des livres pour actualisation après enregistrement des retours.
     */
    public RetourPanel(EmpruntController empruntController, LivrePanel livrePanel) {
        this.empruntController = empruntController; // Initialiser le contrôleur des emprunts
        this.livrePanel = livrePanel; // Référence au panneau des livres
        setLayout(new BorderLayout());

        // Initialisation du tableau des emprunts non retournés
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Livre ID", "Utilisateur ID", "Date Emprunt", "Date Retour Prévue"}, 0);
        tableRetours = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                // Mise en surbrillance des emprunts en retard
                if (column == 4) {
                    Object value = getValueAt(row, column);
                    if (value instanceof String) {
                        LocalDate returnDate = LocalDate.parse((String) value);
                        if (returnDate.isBefore(LocalDate.now())) { // En retard
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

        // Initialisation des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnReturn = new JButton("Enregistrer Retour");
        btnReturn.addActionListener(e -> enregistrerRetour());
        buttonPanel.add(btnReturn);
        add(buttonPanel, BorderLayout.SOUTH);

        loadNonReturnedEmprunts(); // Charger les emprunts non retournés au démarrage
    }

    /**
     * Charge les emprunts non retournés dans le tableau.
     * Ne prend en compte que les emprunts dont la date de retour effective est nulle.
     */
    public void loadNonReturnedEmprunts() {
        tableModel.setRowCount(0); // Effacer les données existantes dans le tableau
        List<Emprunt> emprunts = empruntController.listerEmprunts();
        for (Emprunt emprunt : emprunts) {
            if (emprunt.getDateRetourEffective() == null) { // Inclure uniquement les emprunts non retournés
                tableModel.addRow(new Object[]{
                        emprunt.getId(),
                        emprunt.getLivreId(),
                        emprunt.getUtilisateurId(),
                        emprunt.getDateEmprunt().toString(), // Convertir en String
                        emprunt.getDateRetourPrevue().toString() // Convertir en String
                });
            }
        }
    }

    /**
     * Enregistre le retour d'un emprunt sélectionné et met à jour les données associées.
     * Actualise également le panneau des livres après l'enregistrement.
     */
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

            // Actualiser les panneaux après l'enregistrement
            loadNonReturnedEmprunts(); // Recharger les emprunts non retournés
            livrePanel.loadLivres(); // Actualiser le panneau des livres
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement du retour: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
