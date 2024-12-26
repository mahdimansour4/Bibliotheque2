package controller;

import model.Utilisateur;
import model.UtilisateurModel;

import java.util.List;

/**
 * Contrôleur pour gérer les utilisateurs dans le système de bibliothèque.
 * Fournit une interface entre la vue et le modèle des utilisateurs.
 */
public class UtilisateurController {
    private UtilisateurModel utilisateurModel; // Modèle des utilisateurs utilisé pour les opérations

    /**
     * Constructeur de la classe UtilisateurController.
     * Initialise le modèle des utilisateurs.
     */
    public UtilisateurController() {
        this.utilisateurModel = new UtilisateurModel();
    }

    /**
     * Ajoute un nouvel utilisateur au système.
     *
     * @param utilisateur L'utilisateur à ajouter.
     */
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateurModel.ajouterUtilisateur(utilisateur);
    }

    /**
     * Modifie les informations d'un utilisateur existant.
     *
     * @param utilisateur L'utilisateur avec les nouvelles informations.
     */
    public void modifierUtilisateur(Utilisateur utilisateur) {
        utilisateurModel.modifierUtilisateur(utilisateur);
    }

    /**
     * Supprime un utilisateur du système en fonction de son identifiant.
     *
     * @param id L'identifiant de l'utilisateur à supprimer.
     */
    public void supprimerUtilisateur(int id) {
        utilisateurModel.supprimerUtilisateur(id);
    }

    /**
     * Récupère une liste contenant tous les utilisateurs dans le système.
     *
     * @return Une liste des utilisateurs.
     */
    public List<Utilisateur> listerUtilisateurs() {
        return utilisateurModel.listerUtilisateurs();
    }

    /**
     * Recherche des utilisateurs en fonction d'une requête.
     * La recherche peut être effectuée par nom ou par email.
     *
     * @param query Le texte de la requête de recherche.
     * @return Une liste des utilisateurs correspondant à la requête.
     */
    public List<Utilisateur> rechercherUtilisateur(String query) {
        return utilisateurModel.rechercherUtilisateur(query);
    }
}
