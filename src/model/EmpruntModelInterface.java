package model;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface pour gérer les emprunts dans le système de bibliothèque.
 * Définit les méthodes nécessaires pour manipuler les emprunts.
 */
public interface EmpruntModelInterface {

    /**
     * Ajoute un nouvel emprunt au système.
     *
     * @param emprunt L'emprunt à ajouter.
     */
    void ajouterEmprunt(Emprunt emprunt);

    /**
     * Modifie les informations d'un emprunt existant.
     *
     * @param emprunt L'emprunt avec les nouvelles informations.
     */
    void modifierEmprunt(Emprunt emprunt);

    /**
     * Supprime un emprunt du système en fonction de son identifiant.
     *
     * @param id L'identifiant de l'emprunt à supprimer.
     */
    void supprimerEmprunt(int id);

    /**
     * Récupère la liste de tous les emprunts dans le système.
     *
     * @return Une liste contenant tous les emprunts.
     */
    List<Emprunt> listerEmprunts();

    /**
     * Recherche les emprunts associés à un utilisateur donné.
     *
     * @param utilisateurId L'identifiant de l'utilisateur.
     * @return Une liste des emprunts correspondant à l'utilisateur.
     */
    List<Emprunt> chercherEmpruntsParUtilisateur(int utilisateurId);

    /**
     * Recherche les emprunts associés à un livre donné.
     *
     * @param livreId L'identifiant du livre.
     * @return Une liste des emprunts correspondant au livre.
     */
    List<Emprunt> chercherEmpruntsParLivre(int livreId);

    /**
     * Enregistre le retour d'un emprunt en mettant à jour la date de retour effective.
     *
     * @param empruntId           L'identifiant de l'emprunt.
     * @param dateRetourEffective La date effective du retour.
     */
    void enregistrerRetour(int empruntId, LocalDate dateRetourEffective);
}
