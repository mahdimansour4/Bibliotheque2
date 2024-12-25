package controller;

import model.Livre;
import model.LivreModel;

import java.util.List;

public class LivreController {
    private LivreModel livreModel;

    public LivreController() {
        this.livreModel = new LivreModel();
    }

    public void ajouterLivre(Livre livre) {
        livreModel.ajouterLivre(livre);
    }

    public void modifierLivre(Livre livre) {
        livreModel.modifierLivre(livre);
    }

    public void supprimerLivre(int id) {
        livreModel.supprimerLivre(id);
    }

    public List<Livre> listerLivres() {
        return livreModel.listerLivres();
    }

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
