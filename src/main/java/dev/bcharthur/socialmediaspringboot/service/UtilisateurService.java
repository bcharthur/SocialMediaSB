package dev.bcharthur.socialmediaspringboot.service;

import dev.bcharthur.socialmediaspringboot.model.Utilisateur;

import java.util.List;

public interface UtilisateurService {
    List<Utilisateur> consulterUtilisateurs();
    Utilisateur consulterUtilisateurParId(int no_utilisateur);
    void creerUtilisateur(Utilisateur utilisateur);
    void supprimerUtilisateurParId(int idUtilisateur);
    Utilisateur consulterUtilisateurParPseudo(String pseudo);
    void modifierUtilisateur(Utilisateur utilisateur);
    List<Utilisateur> searchUsers(String query);
    long countUtilisateurs();


}
