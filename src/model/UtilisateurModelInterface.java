package model;

import java.util.List;

public interface UtilisateurModelInterface {
    void ajouterUtilisateur(Utilisateur utilisateur); // Add a user
    void modifierUtilisateur(Utilisateur utilisateur); // Modify a user
    void supprimerUtilisateur(int id); // Delete a user by ID
    List<Utilisateur> listerUtilisateurs(); // List all users
    List<Utilisateur> rechercherUtilisateur(String query); // Search users by name or email
}
