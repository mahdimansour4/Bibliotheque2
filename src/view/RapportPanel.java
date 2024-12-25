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

public class RapportPanel extends JPanel {
    private LivreController livreController;
    private EmpruntController empruntController;
    private UtilisateurController utilisateurController;

    private JLabel lblTotalLivres, lblTotalUtilisateurs, lblTotalEmprunts;

    public RapportPanel() {
        this.livreController = new LivreController();
        this.empruntController = new EmpruntController();
        this.utilisateurController = new UtilisateurController();

        setLayout(new BorderLayout());

        // Top Panel for General Statistics
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblTotalLivres = new JLabel("Total Livres: 0");
        lblTotalUtilisateurs = new JLabel("Total Utilisateurs: 0");
        lblTotalEmprunts = new JLabel("Total Emprunts: 0");
        statsPanel.add(lblTotalLivres);
        statsPanel.add(lblTotalUtilisateurs);
        statsPanel.add(lblTotalEmprunts);
        add(statsPanel, BorderLayout.NORTH);

        // Center Panel for Charts
        JPanel chartPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create Charts
        JFreeChart mostBorrowedBooksChart = createMostBorrowedBooksChart();
        JFreeChart mostActiveUsersChart = createMostActiveUsersChart();

        // Add Charts to the Panel
        chartPanel.add(new ChartPanel(mostBorrowedBooksChart));
        chartPanel.add(new ChartPanel(mostActiveUsersChart));

        add(chartPanel, BorderLayout.CENTER);

        // Load Data
        loadStatistics();
    }

    public void loadStatistics() {
        // General Statistics
        lblTotalLivres.setText("Total Livres: " + livreController.listerLivres().size());
        lblTotalUtilisateurs.setText("Total Utilisateurs: " + utilisateurController.listerUtilisateurs().size());
        lblTotalEmprunts.setText("Total Emprunts: " + empruntController.listerEmprunts().size());

        // Refresh Charts
        JFreeChart mostBorrowedBooksChart = createMostBorrowedBooksChart();
        JFreeChart mostActiveUsersChart = createMostActiveUsersChart();

        ((ChartPanel) ((JPanel) getComponent(1)).getComponent(0)).setChart(mostBorrowedBooksChart);
        ((ChartPanel) ((JPanel) getComponent(1)).getComponent(1)).setChart(mostActiveUsersChart);
    }


    private JFreeChart createMostBorrowedBooksChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Calculate Most Borrowed Books
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

        // Populate Dataset
        bookBorrowCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5) // Limit to Top 5
                .forEach(entry -> dataset.addValue(entry.getValue(), "Emprunts", entry.getKey()));

        // Create Bar Chart
        return ChartFactory.createBarChart(
                "Livres les Plus Empruntés", // Chart Title
                "Livre", // Category Axis Label
                "Nombre d'Emprunts", // Value Axis Label
                dataset
        );
    }

    private JFreeChart createMostActiveUsersChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Calculate Most Active Users
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

        // Populate Dataset
        userBorrowCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5) // Limit to Top 5
                .forEach(entry -> dataset.setValue(entry.getKey(), entry.getValue()));

        // Create Pie Chart
        return ChartFactory.createPieChart(
                "Utilisateurs les Plus Actifs", // Chart Title
                dataset, // Dataset
                true, // Include Legend
                true, // Use tooltips
                false // Do not generate URLs
        );
    }
}
