package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Modèle pour gérer les utilisateurs dans le système de bibliothèque.
 * Fournit des méthodes pour ajouter, modifier, supprimer et rechercher des utilisateurs,
 * ainsi que pour sauvegarder et charger les données depuis un fichier CSV.
 */
public class UtilisateurModel implements UtilisateurModelInterface {
    private List<Utilisateur> utilisateurs; // Liste des utilisateurs chargés
    private static final String CSV_FILE_PATH = "src/data/utilisateurs.csv"; // Chemin du fichier CSV des utilisateurs

    /**
     * Constructeur de la classe UtilisateurModel.
     * Initialise la liste des utilisateurs en la chargeant depuis le fichier CSV.
     */
    public UtilisateurModel() {
        this.utilisateurs = new ArrayList<>();
        this.utilisateurs = loadUtilisateursFromCSV();
    }

    /**
     * Ajoute un nouvel utilisateur au système et le sauvegarde dans le fichier CSV.
     *
     * @param utilisateur L'utilisateur à ajouter.
     */
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateurs.add(utilisateur);
        saveUtilisateursToCSV();
    }

    /**
     * Modifie les informations d'un utilisateur existant.
     *
     * @param utilisateur L'utilisateur avec les nouvelles informations.
     */
    public void modifierUtilisateur(Utilisateur utilisateur) {
        for (int i = 0; i < utilisateurs.size(); i++) {
            if (utilisateurs.get(i).getId() == utilisateur.getId()) {
                utilisateurs.set(i, utilisateur);
                saveUtilisateursToCSV();
                break;
            }
        }
    }

    /**
     * Supprime un utilisateur du système en fonction de son identifiant.
     *
     * @param id L'identifiant de l'utilisateur à supprimer.
     */
    public void supprimerUtilisateur(int id) {
        utilisateurs.removeIf(utilisateur -> utilisateur.getId() == id);
        saveUtilisateursToCSV();
    }

    /**
     * Récupère une liste contenant tous les utilisateurs dans le système.
     *
     * @return Une liste des utilisateurs.
     */
    public List<Utilisateur> listerUtilisateurs() {
        return new ArrayList<>(utilisateurs);
    }

    /**
     * Recherche des utilisateurs en fonction d'une requête (nom ou email).
     *
     * @param query Le texte à rechercher dans les noms ou emails.
     * @return Une liste des utilisateurs correspondant à la requête.
     */
    public List<Utilisateur> rechercherUtilisateur(String query) {
        List<Utilisateur> resultats = new ArrayList<>();
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getNom().toLowerCase().contains(query.toLowerCase()) ||
                    utilisateur.getEmail().toLowerCase().contains(query.toLowerCase())) {
                resultats.add(utilisateur);
            }
        }
        return resultats;
    }

    /**
     * Sauvegarde la liste actuelle des utilisateurs dans le fichier CSV.
     */
    private void saveUtilisateursToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            writer.write("id,nom,email");
            writer.newLine();
            for (Utilisateur utilisateur : utilisateurs) {
                writer.write(
                        utilisateur.getId() + "," +
                                utilisateur.getNom() + "," +
                                utilisateur.getEmail()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture dans le fichier CSV: " + e.getMessage());
        }
    }

    /**
     * Charge la liste des utilisateurs depuis le fichier CSV.
     *
     * @return Une liste des utilisateurs chargés depuis le fichier CSV.
     */
    private List<Utilisateur> loadUtilisateursFromCSV() {
        List<Utilisateur> loadedUtilisateurs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length == 3) {
                    Utilisateur utilisateur = new Utilisateur(
                            Integer.parseInt(values[0]), // id
                            values[1],                  // nom
                            values[2]                   // email
                    );
                    loadedUtilisateurs.add(utilisateur);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier CSV non trouvé, démarrage avec une liste vide.");
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier CSV: " + e.getMessage());
        }
        return loadedUtilisateurs;
    }
}
