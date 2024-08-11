package dev.bcharthur.socialmediaspringboot.service.jpa;

import dev.bcharthur.socialmediaspringboot.model.Utilisateur;
import dev.bcharthur.socialmediaspringboot.repository.UtilisateurRepository;
import dev.bcharthur.socialmediaspringboot.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurServiceJpa implements UtilisateurService {
    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Autowired
    PasswordEncoder encodeur;

    @Override
    public List<Utilisateur> consulterUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    @Override
    public void creerUtilisateur(Utilisateur utilisateur) {
        utilisateur.setMot_de_passe(encodeur.encode(utilisateur.getMot_de_passe()));
        utilisateurRepository.save(utilisateur);
    }

    @Override
    public void supprimerUtilisateurParId(int no_utilisateur) {
        if (utilisateurRepository.existsById(no_utilisateur)){
            utilisateurRepository.deleteById(no_utilisateur);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "L'utilisateur n'existe pas");
        }
    }

    @Override
    public Utilisateur consulterUtilisateurParId(int no_utilisateur) {
        return utilisateurRepository.findById(no_utilisateur)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé avec l'ID spécifié"));
    }

    @Override
    public Utilisateur consulterUtilisateurParPseudo(String pseudo) {
        return utilisateurRepository.findByPseudo(pseudo);
    }

    @Override
    public void modifierUtilisateur(Utilisateur utilisateur) {
        if (utilisateurRepository.existsById(utilisateur.getNo_utilisateur())) {
            utilisateurRepository.save(utilisateur);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "L'utilisateur n'existe pas");
        }
    }

    @Override
    public List<Utilisateur> searchUsers(String query) {
        return utilisateurRepository.findByPseudoContainingIgnoreCase(query);
    }
}
