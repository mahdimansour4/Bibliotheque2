package model;

/**
 * Représente un utilisateur dans le système de bibliothèque.
 * Contient des informations sur l'utilisateur, telles que l'ID, le nom et l'email.
 */
public class Utilisateur {
    private int id; // Identifiant unique de l'utilisateur
    private String nom; // Nom de l'utilisateur
    private String email; // Adresse e-mail de l'utilisateur

    /**
     * Constructeur de la classe Utilisateur.
     *
     * @param id    Identifiant unique de l'utilisateur.
     * @param nom   Nom de l'utilisateur.
     * @param email Adresse e-mail de l'utilisateur.
     */
    public Utilisateur(int id, String nom, String email) {
        this.id = id;
        this.nom = nom;
        this.email = email;
    }

    /**
     * Obtient l'identifiant unique de l'utilisateur.
     *
     * @return L'identifiant de l'utilisateur.
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant unique de l'utilisateur.
     *
     * @param id L'identifiant de l'utilisateur.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtient le nom de l'utilisateur.
     *
     * @return Le nom de l'utilisateur.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de l'utilisateur.
     *
     * @param nom Le nom de l'utilisateur.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Obtient l'adresse e-mail de l'utilisateur.
     *
     * @return L'adresse e-mail de l'utilisateur.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'adresse e-mail de l'utilisateur.
     *
     * @param email L'adresse e-mail de l'utilisateur.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'utilisateur.
     *
     * @return Une chaîne représentant l'utilisateur.
     */
    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
