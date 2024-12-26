package model;

import java.time.LocalDate;

/**
 * Représente un emprunt dans le système de bibliothèque.
 * Contient des informations sur le livre emprunté, l'utilisateur, et les dates associées.
 */
public class Emprunt {
    private int id; // Identifiant unique de l'emprunt
    private int livreId; // ID du livre emprunté
    private int utilisateurId; // ID de l'utilisateur qui a emprunté le livre
    private LocalDate dateEmprunt; // Date de l'emprunt
    private LocalDate dateRetourPrevue; // Date prévue pour le retour
    private LocalDate dateRetourEffective; // Date effective du retour (null si non retourné)

    /**
     * Constructeur de la classe Emprunt.
     *
     * @param id                  Identifiant unique de l'emprunt.
     * @param livreId             Identifiant du livre emprunté.
     * @param utilisateurId       Identifiant de l'utilisateur emprunteur.
     * @param dateEmprunt         Date de l'emprunt.
     * @param dateRetourPrevue    Date prévue pour le retour.
     * @param dateRetourEffective Date effective du retour (null si pas encore retourné).
     */
    public Emprunt(int id, int livreId, int utilisateurId, LocalDate dateEmprunt, LocalDate dateRetourPrevue, LocalDate dateRetourEffective) {
        this.id = id;
        this.livreId = livreId;
        this.utilisateurId = utilisateurId;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = dateRetourEffective;
    }

    /**
     * Obtient l'identifiant unique de l'emprunt.
     *
     * @return L'identifiant de l'emprunt.
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant unique de l'emprunt.
     *
     * @param id L'identifiant de l'emprunt.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtient l'identifiant du livre emprunté.
     *
     * @return L'identifiant du livre.
     */
    public int getLivreId() {
        return livreId;
    }

    /**
     * Définit l'identifiant du livre emprunté.
     *
     * @param livreId L'identifiant du livre.
     */
    public void setLivreId(int livreId) {
        this.livreId = livreId;
    }

    /**
     * Obtient l'identifiant de l'utilisateur emprunteur.
     *
     * @return L'identifiant de l'utilisateur.
     */
    public int getUtilisateurId() {
        return utilisateurId;
    }

    /**
     * Définit l'identifiant de l'utilisateur emprunteur.
     *
     * @param utilisateurId L'identifiant de l'utilisateur.
     */
    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    /**
     * Obtient la date de l'emprunt.
     *
     * @return La date de l'emprunt.
     */
    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    /**
     * Définit la date de l'emprunt.
     *
     * @param dateEmprunt La date de l'emprunt.
     */
    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    /**
     * Obtient la date prévue pour le retour.
     *
     * @return La date prévue pour le retour.
     */
    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    /**
     * Définit la date prévue pour le retour.
     *
     * @param dateRetourPrevue La date prévue pour le retour.
     */
    public void setDateRetourPrevue(LocalDate dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    /**
     * Obtient la date effective du retour.
     *
     * @return La date effective du retour ou null si le livre n'a pas été retourné.
     */
    public LocalDate getDateRetourEffective() {
        return dateRetourEffective;
    }

    /**
     * Définit la date effective du retour.
     *
     * @param dateRetourEffective La date effective du retour.
     */
    public void setDateRetourEffective(LocalDate dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'emprunt.
     *
     * @return Une chaîne représentant l'emprunt.
     */
    @Override
    public String toString() {
        return "Emprunt{" +
                "id=" + id +
                ", livreId=" + livreId +
                ", utilisateurId=" + utilisateurId +
                ", dateEmprunt=" + dateEmprunt +
                ", dateRetourPrevue=" + dateRetourPrevue +
                ", dateRetourEffective=" + dateRetourEffective +
                '}';
    }
}
