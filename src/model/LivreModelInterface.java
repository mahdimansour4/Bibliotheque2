package model;

import java.util.List;

public interface LivreModelInterface {
    void ajouterLivre(Livre livre);
    void modifierLivre(Livre livre);
    void supprimerLivre(int id);
    List<Livre> listerLivres();
    Livre chercherLivreParId(int id);
    Livre chercherLivreParIsbn(String isbn); // New method
    List<Livre> chercherLivresParTitre(String titre);
    List<Livre> chercherLivresParAuteur(String auteur);
}
