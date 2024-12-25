package model;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpruntModel implements EmpruntModelInterface {
    private List<Emprunt> emprunts;
    private static final String CSV_FILE_PATH = "src/data/emprunts.csv";

    public EmpruntModel() {
        this.emprunts = loadEmpruntsFromCSV();
    }

    @Override
    public void ajouterEmprunt(Emprunt emprunt) {
        // Access LivreModel to handle book quantity
        LivreModel livreModel = new LivreModel();
        Livre livre = livreModel.chercherLivreParId(emprunt.getLivreId());

        if (livre != null) {
            if (livre.getQuantite() > 0) {
                // Decrease the book quantity
                livre.setQuantite(livre.getQuantite() - 1);
                livreModel.modifierLivre(livre);

                // Add the borrowing record
                emprunts.add(emprunt);
                saveEmpruntsToCSV();

                System.out.println("Emprunt ajouté avec succès et la quantité de livres a été mise à jour.");
            } else {
                System.out.println("Impossible d'ajouter l'emprunt. La quantité de ce livre est insuffisante.");
            }
        } else {
            System.out.println("Livre introuvable pour l'emprunt.");
        }
    }


    @Override
    public void modifierEmprunt(Emprunt emprunt) {
        for (int i = 0; i < emprunts.size(); i++) {
            if (emprunts.get(i).getId() == emprunt.getId()) {
                emprunts.set(i, emprunt);
                saveEmpruntsToCSV();
                break;
            }
        }
    }

    @Override
    public void supprimerEmprunt(int id) {
        emprunts.removeIf(emprunt -> emprunt.getId() == id);
        saveEmpruntsToCSV();
    }

    @Override
    public List<Emprunt> listerEmprunts() {
        return new ArrayList<>(emprunts);
    }

    @Override
    public List<Emprunt> chercherEmpruntsParUtilisateur(int utilisateurId) {
        List<Emprunt> resultats = new ArrayList<>();
        for (Emprunt emprunt : emprunts) {
            if (emprunt.getUtilisateurId() == utilisateurId) {
                resultats.add(emprunt);
            }
        }
        return resultats;
    }

    @Override
    public List<Emprunt> chercherEmpruntsParLivre(int livreId) {
        List<Emprunt> resultats = new ArrayList<>();
        for (Emprunt emprunt : emprunts) {
            if (emprunt.getLivreId() == livreId) {
                resultats.add(emprunt);
            }
        }
        return resultats;
    }



    private void saveEmpruntsToCSV() {
        File file = new File(CSV_FILE_PATH);
        File parentDir = file.getParentFile();

        // Create the directory if it doesn't exist
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,livreId,utilisateurId,dateEmprunt,dateRetourPrevue,dateRetourEffective");
            writer.newLine();

            for (Emprunt emprunt : emprunts) {
                writer.write(
                        emprunt.getId() + "," +
                                emprunt.getLivreId() + "," +
                                emprunt.getUtilisateurId() + "," +
                                emprunt.getDateEmprunt() + "," +
                                emprunt.getDateRetourPrevue() + "," +
                                (emprunt.getDateRetourEffective() != null ? emprunt.getDateRetourEffective() : "null")
                );
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture dans le fichier CSV: " + e.getMessage());
        }
    }

    @Override
    public void enregistrerRetour(int empruntId, LocalDate dateRetourEffective) {
        for (Emprunt emprunt : emprunts) {
            if (emprunt.getId() == empruntId) {
                if (emprunt.getDateRetourEffective() != null) {
                    System.out.println("Retour déjà enregistré pour cet emprunt.");
                    return;
                }

                // Set the return date
                emprunt.setDateRetourEffective(dateRetourEffective);

                // Update the book quantity in LivreModel
                LivreModel livreModel = new LivreModel(); // Assuming LivreModel is accessible
                Livre livre = livreModel.chercherLivreParId(emprunt.getLivreId());
                if (livre != null) {
                    livre.setQuantite(livre.getQuantite() + 1); // Increment the quantity
                    livreModel.modifierLivre(livre);
                }

                // Save changes to CSV or database
                saveEmpruntsToCSV();
                return;
            }
        }
        System.out.println("Emprunt non trouvé.");
    }



    private List<Emprunt> loadEmpruntsFromCSV() {
        List<Emprunt> emprunts = new ArrayList<>();
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
                if (values.length == 6) { // Ensure correct number of columns
                    Emprunt emprunt = new Emprunt(
                            Integer.parseInt(values[0]), // id
                            Integer.parseInt(values[1]), // livreId
                            Integer.parseInt(values[2]), // utilisateurId
                            LocalDate.parse(values[3]),  // dateEmprunt
                            LocalDate.parse(values[4]),  // dateRetourPrevue
                            "null".equals(values[5]) ? null : LocalDate.parse(values[5]) // dateRetourEffective
                    );
                    emprunts.add(emprunt);
                } else {
                    System.err.println("Ligne ignorée: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier CSV non trouvé, démarrage avec une liste vide.");
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier CSV: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur inattendue: " + e.getMessage());
        }
        return emprunts;
    }
}
