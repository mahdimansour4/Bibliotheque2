package model;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Modèle pour gérer les emprunts dans le système de bibliothèque.
 * Fournit des méthodes pour ajouter, modifier, supprimer et rechercher des emprunts,
 * ainsi que pour enregistrer les retours et gérer les fichiers CSV.
 */
public class EmpruntModel implements EmpruntModelInterface {
    private List<Emprunt> emprunts;
    private static final String CSV_FILE_PATH = "src/data/emprunts.csv";

    /**
     * Constructeur de la classe EmpruntModel.
     * Charge les emprunts à partir du fichier CSV.
     */
    public EmpruntModel() {
        this.emprunts = loadEmpruntsFromCSV();
    }

    /**
     * Ajoute un nouvel emprunt au système et met à jour la quantité du livre associé.
     *
     * @param emprunt L'emprunt à ajouter.
     */
    @Override
    public void ajouterEmprunt(Emprunt emprunt) {
        LivreModel livreModel = new LivreModel();
        Livre livre = livreModel.chercherLivreParId(emprunt.getLivreId());

        if (livre != null) {
            if (livre.getQuantite() > 0) {
                livre.setQuantite(livre.getQuantite() - 1);
                livreModel.modifierLivre(livre);
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

    /**
     * Modifie un emprunt existant dans le système.
     *
     * @param emprunt L'emprunt avec les nouvelles informations.
     */
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

    /**
     * Supprime un emprunt du système en fonction de son identifiant.
     *
     * @param id L'identifiant de l'emprunt à supprimer.
     */
    @Override
    public void supprimerEmprunt(int id) {
        emprunts.removeIf(emprunt -> emprunt.getId() == id);
        saveEmpruntsToCSV();
    }

    /**
     * Liste tous les emprunts du système.
     *
     * @return Une liste contenant tous les emprunts.
     */
    @Override
    public List<Emprunt> listerEmprunts() {
        return new ArrayList<>(emprunts);
    }

    /**
     * Recherche les emprunts associés à un utilisateur donné.
     *
     * @param utilisateurId L'identifiant de l'utilisateur.
     * @return Une liste des emprunts correspondant à l'utilisateur.
     */
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

    /**
     * Recherche les emprunts associés à un livre donné.
     *
     * @param livreId L'identifiant du livre.
     * @return Une liste des emprunts correspondant au livre.
     */
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

    /**
     * Enregistre un retour d'emprunt et met à jour la quantité du livre associé.
     *
     * @param empruntId           L'identifiant de l'emprunt.
     * @param dateRetourEffective La date effective du retour.
     */
    @Override
    public void enregistrerRetour(int empruntId, LocalDate dateRetourEffective) {
        for (Emprunt emprunt : emprunts) {
            if (emprunt.getId() == empruntId) {
                if (emprunt.getDateRetourEffective() != null) {
                    System.out.println("Retour déjà enregistré pour cet emprunt.");
                    return;
                }

                emprunt.setDateRetourEffective(dateRetourEffective);

                LivreModel livreModel = new LivreModel();
                Livre livre = livreModel.chercherLivreParId(emprunt.getLivreId());
                if (livre != null) {
                    livre.setQuantite(livre.getQuantite() + 1);
                    livreModel.modifierLivre(livre);
                }

                saveEmpruntsToCSV();
                return;
            }
        }
        System.out.println("Emprunt non trouvé.");
    }

    /**
     * Sauvegarde les emprunts actuels dans le fichier CSV.
     */
    private void saveEmpruntsToCSV() {
        File file = new File(CSV_FILE_PATH);
        File parentDir = file.getParentFile();

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

    /**
     * Charge les emprunts à partir du fichier CSV.
     *
     * @return Une liste des emprunts chargés.
     */
    private List<Emprunt> loadEmpruntsFromCSV() {
        List<Emprunt> emprunts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length == 6) {
                    Emprunt emprunt = new Emprunt(
                            Integer.parseInt(values[0]),
                            Integer.parseInt(values[1]),
                            Integer.parseInt(values[2]),
                            LocalDate.parse(values[3]),
                            LocalDate.parse(values[4]),
                            "null".equals(values[5]) ? null : LocalDate.parse(values[5])
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
