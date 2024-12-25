package controller;

import model.Emprunt;
import model.EmpruntModel;

import java.time.LocalDate;
import java.util.List;

public class EmpruntController {
    private EmpruntModel empruntModel;

    public EmpruntController() {
        this.empruntModel = new EmpruntModel();
    }

    public void ajouterEmprunt(Emprunt emprunt) {
        empruntModel.ajouterEmprunt(emprunt);
    }

    public void enregistrerRetour(int empruntId, LocalDate dateRetourEffective) {
        empruntModel.enregistrerRetour(empruntId, dateRetourEffective);
    }

    public List<Emprunt> listerEmprunts() {
        return empruntModel.listerEmprunts();
    }

    public List<Emprunt> rechercherEmprunts(String query) {
        try {
            int idQuery = Integer.parseInt(query);
            List<Emprunt> resultats = empruntModel.chercherEmpruntsParUtilisateur(idQuery);
            resultats.addAll(empruntModel.chercherEmpruntsParLivre(idQuery));
            return resultats;
        } catch (NumberFormatException e) {
            return empruntModel.listerEmprunts(); // Return all emprunts if query isn't numeric
        }
    }
}
