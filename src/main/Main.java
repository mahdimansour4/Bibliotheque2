package main;

import view.LivrePanel;
import view.EmpruntPanel;
import view.UtilisateurPanel;
import view.RapportPanel;
import view.RetourPanel;
import controller.EmpruntController;
import view.LoginForm;

import javax.swing.*;

/**
 * Classe principale pour lancer l'application de gestion de bibliothèque.
 * Inclut un formulaire de connexion et l'interface principale avec des panneaux tabulés.
 */
public class Main {

    /**
     * Méthode principale pour lancer l'application.
     * Affiche d'abord le formulaire de connexion.
     *
     * @param args Arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Afficher le formulaire de connexion
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }

    /**
     * Lance l'application principale après une connexion réussie.
     * Configure et affiche les panneaux principaux de l'application.
     */
    public static void launchMainApp() {
        JFrame frame = new JFrame("Gestion de Bibliothèque");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);

        // Contrôleur partagé pour les emprunts
        EmpruntController empruntController = new EmpruntController();

        // Initialiser les panneaux
        LivrePanel livrePanel = new LivrePanel();
        RetourPanel retourPanel = new RetourPanel(empruntController, livrePanel); // Créer RetourPanel
        EmpruntPanel empruntPanel = new EmpruntPanel(empruntController, retourPanel, livrePanel); // Passer RetourPanel à EmpruntPanel
        UtilisateurPanel utilisateurPanel = new UtilisateurPanel();
        RapportPanel rapportPanel = new RapportPanel();

        // Créer un panneau à onglets
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Livres", livrePanel);
        tabbedPane.addTab("Utilisateurs", utilisateurPanel);
        tabbedPane.addTab("Emprunts", empruntPanel);
        tabbedPane.addTab("Retours", retourPanel);
        tabbedPane.addTab("Rapports", rapportPanel);

        // Ajouter un ChangeListener pour actualiser les panneaux à chaque changement d'onglet
        tabbedPane.addChangeListener(e -> {
            int selectedTab = tabbedPane.getSelectedIndex();
            switch (selectedTab) {
                case 0 -> {
                    System.out.println("Actualisation de LivrePanel");
                    livrePanel.loadLivres(); // Actualiser LivrePanel
                }
                case 1 -> utilisateurPanel.loadUtilisateurs(); // Actualiser UtilisateurPanel
                case 2 -> empruntPanel.loadFilteredEmprunts(""); // Actualiser EmpruntPanel
                case 3 -> rapportPanel.loadStatistics(); // Actualiser RapportPanel
                case 4 -> retourPanel.loadNonReturnedEmprunts(); // Actualiser RetourPanel
            }
        });

        frame.add(tabbedPane);
        frame.setVisible(true);
    }
}
