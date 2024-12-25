package main;

import view.LivrePanel;
import view.EmpruntPanel;
import view.UtilisateurPanel;
import view.RapportPanel;
import view.RetourPanel;
import controller.EmpruntController;
import view.LoginForm;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Show login form
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }

    // Launch the main application after successful login
    public static void launchMainApp() {
        JFrame frame = new JFrame("Gestion de Bibliothèque");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);

        // Shared EmpruntController
        EmpruntController empruntController = new EmpruntController();

        // Initialize Panels
        LivrePanel livrePanel = new LivrePanel();
        RetourPanel retourPanel = new RetourPanel(empruntController,livrePanel); // Create RetourPanel
        EmpruntPanel empruntPanel = new EmpruntPanel(empruntController, retourPanel,livrePanel); // Pass RetourPanel to EmpruntPanel
        UtilisateurPanel utilisateurPanel = new UtilisateurPanel();
        RapportPanel rapportPanel = new RapportPanel();

        // Create TabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Livres", livrePanel);
        tabbedPane.addTab("Utilisateurs", utilisateurPanel);
        tabbedPane.addTab("Emprunts", empruntPanel);
        tabbedPane.addTab("Retours", retourPanel);
        tabbedPane.addTab("Rapports", rapportPanel);

        // Add ChangeListener to Refresh Tabs
        tabbedPane.addChangeListener(e -> {
            int selectedTab = tabbedPane.getSelectedIndex();
            switch (selectedTab) {
                case 0 -> {
                    System.out.println("Refreshing LivrePanel");
                    livrePanel.loadLivres(); // Refresh LivrePanel
                }
                case 1 -> utilisateurPanel.loadUtilisateurs(); // Refresh UtilisateurPanel
                case 2 -> empruntPanel.loadFilteredEmprunts(""); // Refresh EmpruntPanel
                case 3 -> rapportPanel.loadStatistics(); // Refresh RapportPanel
                case 4 -> retourPanel.loadNonReturnedEmprunts(); // Refresh RetourPanel
            }
        });

        frame.add(tabbedPane);
        frame.setVisible(true);
    }
}
