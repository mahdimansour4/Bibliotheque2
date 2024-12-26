package view;

import controller.EmpruntController;
import controller.LivreController;
import controller.UtilisateurController;
import model.Emprunt;
import model.Livre;
import model.Utilisateur;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panneau de rapport pour afficher les statistiques générales de la bibliothèque.
 * Comprend le total des livres, utilisateurs, emprunts et des graphiques.
 */
public class RapportPanel extends JPanel {
    private LivreController livreController;
    private EmpruntController empruntController;
    private UtilisateurController utilisateurController;

    private JLabel lblTotalLivres, lblTotalUtilisateurs, lblTotalEmprunts;
    private JPanel chartPanel; // Panneau contenant les graphiques pour les mises à jour dynamiques

    /**
     * Constructeur de la classe RapportPanel.
     * Initialise les contrôleurs et configure l'interface utilisateur pour afficher les statistiques.
     */
    public RapportPanel() {
        this.livreController = new LivreController();
        this.empruntController = new EmpruntController();
        this.utilisateurController = new UtilisateurController();

        setLayout(new BorderLayout());

        // Panneau supérieur pour les statistiques générales
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblTotalLivres = new JLabel("Total Livres: 0");
        lblTotalUtilisateurs = new JLabel("Total Utilisateurs: 0");
        lblTotalEmprunts = new JLabel("Total Emprunts: 0");
        statsPanel.add(lblTotalLivres);
        statsPanel.add(lblTotalUtilisateurs);
        statsPanel.add(lblTotalEmprunts);
        add(statsPanel, BorderLayout.NORTH);

        // Panneau central pour les graphiques
        chartPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(chartPanel, BorderLayout.CENTER);

        // Charger les données initiales
        loadStatistics();
    }

    /**
     * Charge les statistiques générales et met à jour les graphiques.
     */
    public void loadStatistics() {
        // Met à jour les statistiques générales
        lblTotalLivres.setText("Total Livres: " + livreController.listerLivres().size());
        lblTotalUtilisateurs.setText("Total Utilisateurs: " + utilisateurController.listerUtilisateurs().size());
        lblTotalEmprunts.setText("Total Emprunts: " + empruntController.listerEmprunts().size());

        // Rafraîchit les graphiques
        chartPanel.removeAll(); // Efface les graphiques existants
        JFreeChart mostBorrowedBooksChart = createMostBorrowedBooksChart();
        JFreeChart mostActiveUsersChart = createMostActiveUsersChart();
        chartPanel.add(new ChartPanel(mostBorrowedBooksChart));
        chartPanel.add(new ChartPanel(mostActiveUsersChart));
        chartPanel.revalidate(); // Met à jour le panneau pour afficher les nouveaux graphiques
        chartPanel.repaint();
    }

    /**
     * Crée un graphique en barres pour les livres les plus empruntés.
     *
     * @return Un graphique en barres montrant les 5 livres les plus empruntés.
     */
    private JFreeChart createMostBorrowedBooksChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Calcul des livres les plus empruntés
        Map<String, Integer> bookBorrowCounts = new HashMap<>();
        List<Emprunt> emprunts = empruntController.listerEmprunts();
        for (Emprunt emprunt : emprunts) {
            Livre livre = livreController.listerLivres().stream()
                    .filter(l -> l.getId() == emprunt.getLivreId())
                    .findFirst()
                    .orElse(null);
            if (livre != null) {
                bookBorrowCounts.put(livre.getTitre(), bookBorrowCounts.getOrDefault(livre.getTitre(), 0) + 1);
            }
        }

        // Remplir le dataset
        bookBorrowCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5) // Limite aux 5 premiers livres
                .forEach(entry -> dataset.addValue(entry.getValue(), "Emprunts", entry.getKey()));

        // Créer un graphique en barres
        return ChartFactory.createBarChart(
                "Livres les Plus Empruntés", // Titre du graphique
                "Livre", // Étiquette de l'axe des catégories
                "Nombre d'Emprunts", // Étiquette de l'axe des valeurs
                dataset
        );
    }

    /**
     * Crée un graphique circulaire pour les utilisateurs les plus actifs.
     *
     * @return Un graphique circulaire montrant les 10 utilisateurs les plus actifs.
     */
    private JFreeChart createMostActiveUsersChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Calcul des utilisateurs les plus actifs
        Map<String, Integer> userBorrowCounts = new HashMap<>();
        List<Emprunt> emprunts = empruntController.listerEmprunts();
        for (Emprunt emprunt : emprunts) {
            Utilisateur utilisateur = utilisateurController.listerUtilisateurs().stream()
                    .filter(u -> u.getId() == emprunt.getUtilisateurId())
                    .findFirst()
                    .orElse(null);
            if (utilisateur != null) {
                userBorrowCounts.put(utilisateur.getNom(), userBorrowCounts.getOrDefault(utilisateur.getNom(), 0) + 1);
            }
        }

        // Remplir le dataset
        userBorrowCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(10) // Limite aux 10 premiers utilisateurs
                .forEach(entry -> dataset.setValue(entry.getKey(), entry.getValue()));

        // Créer un graphique circulaire
        return ChartFactory.createPieChart(
                "Utilisateurs les Plus Actifs", // Titre du graphique
                dataset, // Dataset
                true, // Inclure une légende
                true, // Utiliser des infobulles
                false // Ne pas générer d'URLs
        );
    }
}
