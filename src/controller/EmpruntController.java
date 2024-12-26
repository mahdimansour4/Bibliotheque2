package controller;

import model.Emprunt;
import model.EmpruntModel;

import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur pour gérer les emprunts dans le système de bibliothèque.
 * Fournit une interface entre la vue et le modèle des emprunts.
 */
public class EmpruntController {
    private EmpruntModel empruntModel; // Modèle des emprunts utilisé pour les opérations

    /**
     * Constructeur de la classe EmpruntController.
     * Initialise le modèle des emprunts.
     */
    public EmpruntController() {
        this.empruntModel = new EmpruntModel();
    }

    /**
     * Ajoute un nouvel emprunt au système.
     *
     * @param emprunt L'emprunt à ajouter.
     */
    public void ajouterEmprunt(Emprunt emprunt) {
        empruntModel.ajouterEmprunt(emprunt);
    }

    /**
     * Enregistre le retour d'un emprunt en mettant à jour la date de retour effective.
     *
     * @param empruntId           L'identifiant de l'emprunt.
     * @param dateRetourEffective La date effective du retour.
     */
    public void enregistrerRetour(int empruntId, LocalDate dateRetourEffective) {
        empruntModel.enregistrerRetour(empruntId, dateRetourEffective);
    }

    /**
     * Récupère une liste contenant tous les emprunts dans le système.
     *
     * @return Une liste des emprunts.
     */
    public List<Emprunt> listerEmprunts() {
        return empruntModel.listerEmprunts();
    }

    /**
     * Recherche des emprunts en fonction d'une requête.
     * Si la requête est numérique, elle cherche les emprunts par utilisateur ou par livre.
     * Si la requête n'est pas numérique, elle retourne tous les emprunts.
     *
     * @param query Le texte de la requête de recherche.
     * @return Une liste des emprunts correspondant à la requête.
     */
    public List<Emprunt> rechercherEmprunts(String query) {
        try {
            int idQuery = Integer.parseInt(query);
            List<Emprunt> resultats = empruntModel.chercherEmpruntsParUtilisateur(idQuery);
            resultats.addAll(empruntModel.chercherEmpruntsParLivre(idQuery));
            return resultats;
        } catch (NumberFormatException e) {
            return empruntModel.listerEmprunts(); // Retourne tous les emprunts si la requête n'est pas numérique
        }
    }
}
