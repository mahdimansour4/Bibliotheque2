package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurModel implements UtilisateurModelInterface{
    private List<Utilisateur> utilisateurs;
    private static final String CSV_FILE_PATH = "src/data/utilisateurs.csv";

    public UtilisateurModel() {
        this.utilisateurs = new ArrayList<>();
        this.utilisateurs = loadUtilisateursFromCSV();
    }

    public void ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateurs.add(utilisateur);
        saveUtilisateursToCSV();
    }

    public void modifierUtilisateur(Utilisateur utilisateur) {
        for (int i = 0; i < utilisateurs.size(); i++) {
            if (utilisateurs.get(i).getId() == utilisateur.getId()) {
                utilisateurs.set(i, utilisateur);
                saveUtilisateursToCSV();
                break;
            }
        }
    }

    public void supprimerUtilisateur(int id) {
        utilisateurs.removeIf(utilisateur -> utilisateur.getId() == id);
        saveUtilisateursToCSV();
    }

    public List<Utilisateur> listerUtilisateurs() {
        return new ArrayList<>(utilisateurs);
    }

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
