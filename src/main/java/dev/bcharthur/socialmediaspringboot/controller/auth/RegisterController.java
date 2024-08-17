package dev.bcharthur.socialmediaspringboot.controller.auth;

import dev.bcharthur.socialmediaspringboot.model.Utilisateur;
import dev.bcharthur.socialmediaspringboot.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping
    public String getUtilisateurForm(Model model) {
        if (!model.containsAttribute("utilisateur")) {
            model.addAttribute("utilisateur", new Utilisateur());
        }

        // Ajout du nombre d'utilisateurs au modèle
        long userCount = utilisateurService.countUtilisateurs();
        model.addAttribute("userCount", userCount);

        return "auth/auth"; // Renvoie vers la page combinée auth.html
    }

    @PostMapping("/creer")
    public String postUtilisateurForm(@Valid Utilisateur utilisateur, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            long userCount = utilisateurService.countUtilisateurs();
            model.addAttribute("userCount", userCount);
            model.addAttribute("registerError", "Vérifiez les informations saisies.");
            return "auth/auth";
        }
        try {
            utilisateurService.creerUtilisateur(utilisateur);
            redirectAttributes.addFlashAttribute("successMessage", "Utilisateur créé avec succès.");
            return "redirect:/login";
        } catch (DataIntegrityViolationException e) {
            long userCount = utilisateurService.countUtilisateurs();
            model.addAttribute("userCount", userCount);
            model.addAttribute("registerError", "Un utilisateur avec ce pseudo, email ou téléphone existe déjà.");
            return "auth/auth";
        } catch (Exception e) {
            long userCount = utilisateurService.countUtilisateurs();
            model.addAttribute("userCount", userCount);
            model.addAttribute("registerError", "Une erreur inattendue s'est produite. Veuillez réessayer.");
            return "auth/auth";
        }
    }

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, Model model) {
        long userCount = utilisateurService.countUtilisateurs();
        model.addAttribute("userCount", userCount);
        model.addAttribute("registerError", "Une erreur s'est produite. Veuillez réessayer.");
        return "auth/auth";
    }
}