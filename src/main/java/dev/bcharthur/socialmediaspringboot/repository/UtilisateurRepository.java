package dev.bcharthur.socialmediaspringboot.repository;

import dev.bcharthur.socialmediaspringboot.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Utilisateur findByPseudo(String pseudo);

    List<Utilisateur> findByPseudoContainingIgnoreCase(String pseudo);
}
