package model;

import java.util.List;

/**
 * Interface pour gérer les utilisateurs dans le système de bibliothèque.
 * Définit les méthodes nécessaires pour manipuler les utilisateurs.
 */
public interface UtilisateurModelInterface {

    /**
     * Ajoute un nouvel utilisateur au système.
     *
     * @param utilisateur L'utilisateur à ajouter.
     */
    void ajouterUtilisateur(Utilisateur utilisateur);

    /**
     * Modifie les informations d'un utilisateur existant.
     *
     * @param utilisateur L'utilisateur avec les nouvelles informations.
     */
    void modifierUtilisateur(Utilisateur utilisateur);

    /**
     * Supprime un utilisateur du système en fonction de son identifiant.
     *
     * @param id L'identifiant de l'utilisateur à supprimer.
     */
    void supprimerUtilisateur(int id);

    /**
     * Récupère une liste contenant tous les utilisateurs dans le système.
     *
     * @return Une liste des utilisateurs.
     */
    List<Utilisateur> listerUtilisateurs();

    /**
     * Recherche des utilisateurs en fonction d'une requête (nom ou email).
     *
     * @param query Le texte à rechercher dans les noms ou emails.
     * @return Une liste des utilisateurs correspondant à la requête.
     */
    List<Utilisateur> rechercherUtilisateur(String query);
}
