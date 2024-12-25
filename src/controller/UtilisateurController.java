package controller;

import model.Utilisateur;
import model.UtilisateurModel;

import java.util.List;

public class UtilisateurController {
    private UtilisateurModel utilisateurModel;

    public UtilisateurController() {
        this.utilisateurModel = new UtilisateurModel();
    }

    public void ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateurModel.ajouterUtilisateur(utilisateur);
    }

    public void modifierUtilisateur(Utilisateur utilisateur) {
        utilisateurModel.modifierUtilisateur(utilisateur);
    }

    public void supprimerUtilisateur(int id) {
        utilisateurModel.supprimerUtilisateur(id);
    }

    public List<Utilisateur> listerUtilisateurs() {
        return utilisateurModel.listerUtilisateurs();
    }

    public List<Utilisateur> rechercherUtilisateur(String query) {
        return utilisateurModel.rechercherUtilisateur(query);
    }
}
