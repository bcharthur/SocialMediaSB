package dev.bcharthur.socialmediaspringboot.security;

import dev.bcharthur.socialmediaspringboot.model.Utilisateur;
import dev.bcharthur.socialmediaspringboot.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * A partir du moment ou on a dans le contexte Spring un service qui implémente UserDetailsService
 * => Spring security va l'utiliser pour aller chercher les utilisateurs dans le processus d'authentification
 */
@Service
public class MyUserDetailsService implements UserDetailsService {


    @Autowired
    private UtilisateurRepository userRepository;

    /**
     * Comment est-ce qu'on va chercher un utilisateur Spring Security à partir d'un pseudo?
     * => à partir du service membreService
     */
    @Override
    public UserDetails loadUserByUsername(String pseudo) {

        Utilisateur user;
        try {
            user = userRepository.findByPseudo(pseudo);
            if (user == null) {
                throw new UsernameNotFoundException(pseudo);
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException(pseudo);
        }
        return new MyUserDetail(user);
    }
}