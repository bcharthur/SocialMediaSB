package dev.bcharthur.socialmediaspringboot.controller.auth;

import dev.bcharthur.socialmediaspringboot.model.Utilisateur;
import dev.bcharthur.socialmediaspringboot.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/login")
    public String login(Model model) {
        if (!model.containsAttribute("utilisateur")) {
            model.addAttribute("utilisateur", new Utilisateur());
        }
        // Ajouter le nombre d'utilisateurs au modèle
        long userCount = utilisateurService.countUtilisateurs();
        model.addAttribute("userCount", userCount);
        return "auth/auth";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        // Ajouter le nombre d'utilisateurs au modèle
        long userCount = utilisateurService.countUtilisateurs();
        model.addAttribute("userCount", userCount);
        return "auth/auth";
    }
}