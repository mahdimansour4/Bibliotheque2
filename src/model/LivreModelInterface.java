package model;

import java.util.List;

/**
 * Interface pour gérer les livres dans le système de bibliothèque.
 * Définit les méthodes nécessaires pour manipuler les livres.
 */
public interface LivreModelInterface {

    /**
     * Ajoute un nouveau livre au système.
     *
     * @param livre Le livre à ajouter.
     */
    void ajouterLivre(Livre livre);

    /**
     * Modifie les informations d'un livre existant.
     *
     * @param livre Le livre avec les nouvelles informations.
     */
    void modifierLivre(Livre livre);

    /**
     * Supprime un livre du système en fonction de son identifiant.
     *
     * @param id L'identifiant du livre à supprimer.
     */
    void supprimerLivre(int id);

    /**
     * Récupère une liste contenant tous les livres dans le système.
     *
     * @return Une liste des livres.
     */
    List<Livre> listerLivres();

    /**
     * Recherche un livre par son identifiant.
     *
     * @param id L'identifiant du livre recherché.
     * @return Le livre correspondant ou null s'il n'existe pas.
     */
    Livre chercherLivreParId(int id);

    /**
     * Recherche un livre par son numéro ISBN.
     *
     * @param isbn Le numéro ISBN du livre recherché.
     * @return Le livre correspondant ou null s'il n'existe pas.
     */
    Livre chercherLivreParIsbn(String isbn);

    /**
     * Recherche des livres par leur titre.
     *
     * @param titre Le titre ou une partie du titre à rechercher.
     * @return Une liste des livres correspondant au critère.
     */
    List<Livre> chercherLivresParTitre(String titre);

    /**
     * Recherche des livres par leur auteur.
     *
     * @param auteur Le nom de l'auteur ou une partie de celui-ci à rechercher.
     * @return Une liste des livres correspondant au critère.
     */
    List<Livre> chercherLivresParAuteur(String auteur);
}
