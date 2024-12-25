package model;

import java.time.LocalDate;
import java.util.List;

public interface EmpruntModelInterface {
    void ajouterEmprunt(Emprunt emprunt);
    void modifierEmprunt(Emprunt emprunt);
    void supprimerEmprunt(int id);
    List<Emprunt> listerEmprunts();
    List<Emprunt> chercherEmpruntsParUtilisateur(int utilisateurId);
    List<Emprunt> chercherEmpruntsParLivre(int livreId);
    void enregistrerRetour(int empruntId,LocalDate dateRetourEffective);
}
