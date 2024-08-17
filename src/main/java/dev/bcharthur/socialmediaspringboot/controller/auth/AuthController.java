//package dev.bcharthur.socialmediaspringboot.controller.auth;
//
//import dev.bcharthur.socialmediaspringboot.model.Utilisateur;
//import dev.bcharthur.socialmediaspringboot.service.UtilisateurService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//@Controller
//@RequestMapping("/auth")
//public class AuthController {
//
//    @Autowired
//    private UtilisateurService utilisateurService;
//
//    @GetMapping
//    public String getAuthPage(Model model) {
//        if (!model.containsAttribute("utilisateur")) {
//            model.addAttribute("utilisateur", new Utilisateur());
//        }
//        return "auth/auth"; // Renvoie vers la page combinée auth.html
//    }
//
//    @PostMapping("/register")
//    public String postUtilisateurForm(@Valid Utilisateur utilisateur, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("registerError", "Vérifiez les informations saisies.");
//            return "auth/auth";
//        }
//        try {
//            utilisateurService.creerUtilisateur(utilisateur);
//            redirectAttributes.addFlashAttribute("successMessage", "Utilisateur créé avec succès.");
//            return "redirect:/auth"; // Redirige après l'inscription
//        } catch (DataIntegrityViolationException e) {
//            model.addAttribute("registerError", "Un utilisateur avec ce pseudo, email ou téléphone existe déjà.");
//            return "auth/auth";
//        } catch (Exception e) {
//            model.addAttribute("registerError", "Une erreur inattendue s'est produite. Veuillez réessayer.");
//            return "auth/auth";
//        }
//    }
//
//    @ExceptionHandler(Exception.class)
//    public String handleAllExceptions(Exception ex, Model model) {
//        model.addAttribute("registerError", "Une erreur s'est produite. Veuillez réessayer.");
//        return "auth/auth";
//    }
//
//    @PostMapping("/login")
//    public String postLoginForm(Model model, RedirectAttributes redirectAttributes) {
//        // La logique de connexion est généralement gérée par Spring Security, donc cette méthode peut être vide
//        return "redirect:/";
//    }
//
//    @RequestMapping("/login-error")
//    public String loginError(Model model) {
//        model.addAttribute("loginError", "Pseudo ou mot de passe incorrect.");
//        return "auth/auth";
//    }
//}
