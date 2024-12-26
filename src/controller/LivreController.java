package controller;

import model.Livre;
import model.LivreModel;

import java.util.List;

/**
 * Contrôleur pour gérer les livres dans le système de bibliothèque.
 * Fournit une interface entre la vue et le modèle des livres.
 */
public class LivreController {
    private LivreModel livreModel; // Modèle des livres utilisé pour les opérations

    /**
     * Constructeur de la classe LivreController.
     * Initialise le modèle des livres.
     */
    public LivreController() {
        this.livreModel = new LivreModel();
    }

    /**
     * Ajoute un nouveau livre au système.
     *
     * @param livre Le livre à ajouter.
     */
    public void ajouterLivre(Livre livre) {
        livreModel.ajouterLivre(livre);
    }

    /**
     * Modifie les informations d'un livre existant.
     *
     * @param livre Le livre avec les nouvelles informations.
     */
    public void modifierLivre(Livre livre) {
        livreModel.modifierLivre(livre);
    }

    /**
     * Supprime un livre du système en fonction de son identifiant.
     *
     * @param id L'identifiant du livre à supprimer.
     */
    public void supprimerLivre(int id) {
        livreModel.supprimerLivre(id);
    }

    /**
     * Récupère une liste contenant tous les livres dans le système.
     *
     * @return Une liste des livres.
     */
    public List<Livre> listerLivres() {
        return livreModel.listerLivres();
    }

    /**
     * Recherche des livres en fonction d'une requête.
     * La recherche peut être effectuée par titre, auteur ou ISBN.
     *
     * @param query Le texte de la requête de recherche.
     * @return Une liste des livres correspondant à la requête.
     */
    public List<Livre> rechercherLivres(String query) {
        List<Livre> resultats = livreModel.chercherLivresParTitre(query);
        resultats.addAll(livreModel.chercherLivresParAuteur(query));
        Livre livreParIsbn = livreModel.chercherLivreParIsbn(query);
        if (livreParIsbn != null && !resultats.contains(livreParIsbn)) {
            resultats.add(livreParIsbn);
        }
        return resultats;
    }
}
