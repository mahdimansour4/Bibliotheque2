package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LivreModel implements LivreModelInterface {
    private List<Livre> livres;
    private static final String CSV_FILE_PATH = "src/data/livres.csv";

    public LivreModel() {
        this.livres = new ArrayList<>();
        this.livres = loadBooksFromCSV();
    }

    @Override
    public void ajouterLivre(Livre livre) {
        livres.add(livre);
        saveBooksToCSV();
    }

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

    @Override
    public void supprimerLivre(int id) {
        livres.removeIf(livre -> livre.getId() == id);
        saveBooksToCSV();
    }

    @Override
    public List<Livre> listerLivres() {
        return new ArrayList<>(livres);
    }

    @Override
    public Livre chercherLivreParId(int id) {
        for (Livre livre : livres) {
            if (livre.getId() == id) {
                return livre;
            }
        }
        return null;
    }

    public Livre chercherLivreParIsbn(String isbn) {
        for (Livre livre : livres) {
            if (livre.getIsbn().equalsIgnoreCase(isbn)) {
                return livre;
            }
        }
        return null;
    }

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

    private List<Livre> loadBooksFromCSV() {
        List<Livre> loadedLivres = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    // Skip header
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length == 7) { // Ensure correct number of columns
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
