package model;

/**
 * Représente un livre dans le système de bibliothèque.
 * Contient des informations telles que l'ID, l'ISBN, le titre, l'auteur, l'année de publication, le genre et la quantité disponible.
 */
public class Livre {
    private int id; // Identifiant unique du livre
    private String isbn; // Numéro ISBN du livre
    private String titre; // Titre du livre
    private String auteur; // Auteur du livre
    private int anneePublication; // Année de publication du livre
    private String genre; // Genre du livre
    private int quantite; // Quantité disponible du livre

    /**
     * Constructeur de la classe Livre.
     *
     * @param id               Identifiant unique du livre.
     * @param isbn             Numéro ISBN du livre.
     * @param titre            Titre du livre.
     * @param auteur           Auteur du livre.
     * @param anneePublication Année de publication du livre.
     * @param genre            Genre du livre.
     * @param quantite         Quantité disponible du livre.
     */
    public Livre(int id, String isbn, String titre, String auteur, int anneePublication, String genre, int quantite) {
        this.id = id;
        this.isbn = isbn;
        this.titre = titre;
        this.auteur = auteur;
        this.anneePublication = anneePublication;
        this.genre = genre;
        this.quantite = quantite;
    }

    /**
     * Obtient l'identifiant unique du livre.
     *
     * @return L'identifiant du livre.
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant unique du livre.
     *
     * @param id L'identifiant du livre.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtient le numéro ISBN du livre.
     *
     * @return Le numéro ISBN du livre.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Définit le numéro ISBN du livre.
     *
     * @param isbn Le numéro ISBN du livre.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Obtient le titre du livre.
     *
     * @return Le titre du livre.
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Définit le titre du livre.
     *
     * @param titre Le titre du livre.
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Obtient l'auteur du livre.
     *
     * @return L'auteur du livre.
     */
    public String getAuteur() {
        return auteur;
    }

    /**
     * Définit l'auteur du livre.
     *
     * @param auteur L'auteur du livre.
     */
    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    /**
     * Obtient l'année de publication du livre.
     *
     * @return L'année de publication du livre.
     */
    public int getAnneePublication() {
        return anneePublication;
    }

    /**
     * Définit l'année de publication du livre.
     *
     * @param anneePublication L'année de publication du livre.
     */
    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
    }

    /**
     * Obtient le genre du livre.
     *
     * @return Le genre du livre.
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Définit le genre du livre.
     *
     * @param genre Le genre du livre.
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Obtient la quantité disponible du livre.
     *
     * @return La quantité disponible du livre.
     */
    public int getQuantite() {
        return quantite;
    }

    /**
     * Définit la quantité disponible du livre.
     *
     * @param quantite La quantité disponible du livre.
     */
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du livre.
     *
     * @return Une chaîne représentant le livre.
     */
    @Override
    public String toString() {
        return "Livre{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", titre='" + titre + '\'' +
                ", auteur='" + auteur + '\'' +
                ", anneePublication=" + anneePublication +
                ", genre='" + genre + '\'' +
                ", quantite=" + quantite +
                '}';
    }
}
