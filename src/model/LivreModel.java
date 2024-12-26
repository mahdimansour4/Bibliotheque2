package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Modèle pour gérer les livres dans le système de bibliothèque.
 * Fournit des méthodes pour ajouter, modifier, supprimer et rechercher des livres,
 * ainsi que pour sauvegarder et charger les données depuis un fichier CSV.
 */
public class LivreModel implements LivreModelInterface {
    private List<Livre> livres; // Liste des livres chargés
    private static final String CSV_FILE_PATH = "src/data/livres.csv"; // Chemin du fichier CSV des livres

    /**
     * Constructeur de la classe LivreModel.
     * Initialise la liste des livres en la chargeant depuis le fichier CSV.
     */
    public LivreModel() {
        this.livres = new ArrayList<>();
        this.livres = loadBooksFromCSV();
    }

    /**
     * Ajoute un nouveau livre au système et le sauvegarde dans le fichier CSV.
     *
     * @param livre Le livre à ajouter.
     */
    @Override
    public void ajouterLivre(Livre livre) {
        livres.add(livre);
        saveBooksToCSV();
    }

    /**
     * Modifie les informations d'un livre existant.
     *
     * @param livre Le livre avec les nouvelles informations.
     */
    @Override
    public void modifierLivre(Livre livre) {
        for (int i = 0; i < livres.size(); i++) {
            if (livres.get(i).getId() == livre.getId()) {
                livres.set(i, livre);
                saveBooksToCSV();
                break;
            }
        }
    }

    /**
     * Supprime un livre du système en fonction de son identifiant.
     *
     * @param id L'identifiant du livre à supprimer.
     */
    @Override
    public void supprimerLivre(int id) {
        livres.removeIf(livre -> livre.getId() == id);
        saveBooksToCSV();
    }

    /**
     * Récupère une liste contenant tous les livres dans le système.
     *
     * @return Une liste des livres.
     */
    @Override
    public List<Livre> listerLivres() {
        return new ArrayList<>(livres);
    }

    /**
     * Recherche un livre par son identifiant.
     *
     * @param id L'identifiant du livre recherché.
     * @return Le livre correspondant ou null s'il n'existe pas.
     */
    @Override
    public Livre chercherLivreParId(int id) {
        for (Livre livre : livres) {
            if (livre.getId() == id) {
                return livre;
            }
        }
        return null;
    }

    /**
     * Recherche un livre par son numéro ISBN.
     *
     * @param isbn Le numéro ISBN du livre recherché.
     * @return Le livre correspondant ou null s'il n'existe pas.
     */
    public Livre chercherLivreParIsbn(String isbn) {
        for (Livre livre : livres) {
            if (livre.getIsbn().equalsIgnoreCase(isbn)) {
                return livre;
            }
        }
        return null;
    }

    /**
     * Recherche des livres par leur titre.
     *
     * @param titre Le titre ou une partie du titre à rechercher.
     * @return Une liste des livres correspondant au critère.
     */
    @Override
    public List<Livre> chercherLivresParTitre(String titre) {
        List<Livre> resultats = new ArrayList<>();
        for (Livre livre : livres) {
            if (livre.getTitre().toLowerCase().contains(titre.toLowerCase())) {
                resultats.add(livre);
            }
        }
        return resultats;
    }

    /**
     * Recherche des livres par leur auteur.
     *
     * @param auteur Le nom de l'auteur ou une partie de celui-ci à rechercher.
     * @return Une liste des livres correspondant au critère.
     */
    @Override
    public List<Livre> chercherLivresParAuteur(String auteur) {
        List<Livre> resultats = new ArrayList<>();
        for (Livre livre : livres) {
            if (livre.getAuteur().toLowerCase().contains(auteur.toLowerCase())) {
                resultats.add(livre);
            }
        }
        return resultats;
    }

    /**
     * Sauvegarde la liste actuelle des livres dans le fichier CSV.
     */
    private void saveBooksToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            writer.write("id,isbn,titre,auteur,anneePublication,genre,quantite");
            writer.newLine();
            for (Livre livre : livres) {
                writer.write(
                        livre.getId() + "," +
                                livre.getIsbn() + "," +
                                livre.getTitre() + "," +
                                livre.getAuteur() + "," +
                                livre.getAnneePublication() + "," +
                                livre.getGenre() + "," +
                                livre.getQuantite()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture dans le fichier CSV: " + e.getMessage());
        }
    }

    /**
     * Charge la liste des livres depuis le fichier CSV.
     *
     * @return Une liste des livres chargés depuis le fichier CSV.
     */
    private List<Livre> loadBooksFromCSV() {
        List<Livre> loadedLivres = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    // Ignorer l'en-tête
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length == 7) { // Vérifie que le nombre de colonnes est correct
                    Livre livre = new Livre(
                            Integer.parseInt(values[0]), // id
                            values[1],                  // isbn
                            values[2],                  // titre
                            values[3],                  // auteur
                            Integer.parseInt(values[4]), // anneePublication
                            values[5],                  // genre
                            Integer.parseInt(values[6])  // quantite
                    );
                    loadedLivres.add(livre);
                } else {
                    System.err.println("Ligne ignorée: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Fichier CSV non trouvé: " + CSV_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier CSV: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur inattendue: " + e.getMessage());
        }
        return loadedLivres;
    }
}
