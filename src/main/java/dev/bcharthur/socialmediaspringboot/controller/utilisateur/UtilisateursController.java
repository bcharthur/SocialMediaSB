package dev.bcharthur.socialmediaspringboot.controller.utilisateur;

import dev.bcharthur.socialmediaspringboot.model.*;
import dev.bcharthur.socialmediaspringboot.service.*;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/utilisateurs")
public class UtilisateursController {
    @Autowired
    UtilisateurService utilisateurService;

    @Autowired
    PostService postService;  // Injection du PostService
    @Autowired
    private CommentService commentService;

    @GetMapping("/api/utilisateurs/recherche")
    public ResponseEntity<List<Utilisateur>> searchUsers(@RequestParam("query") String query) {
        List<Utilisateur> users = utilisateurService.searchUsers(query);
        return ResponseEntity.ok(users);
    }

    @GetMapping
    public String getUtilisateurs(Model model) {
        model.addAttribute("listeUtilisateurs", utilisateurService.consulterUtilisateurs());
        return "utilisateurs/liste";
    }

    @GetMapping("/creer")
    public String getUtilisateurForm(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "utilisateurs/creation";
    }

    @PostMapping("/creer")
    public String postUtilisateurForm(@Valid @ModelAttribute("utilisateur") Utilisateur utilisateur, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "utilisateurs/creation";
        }

        utilisateurService.creerUtilisateur(utilisateur);
        return "redirect:/utilisateurs";
    }

    @GetMapping("/{idUtilisateur}/supprimer")
    public String confirmerSuppressionUtilisateur(@PathVariable int idUtilisateur, Model model) {
        Utilisateur utilisateur = utilisateurService.consulterUtilisateurParId(idUtilisateur);
        model.addAttribute("message", "Êtes-vous sûr de vouloir supprimer l'utilisateur : " + utilisateur.getPseudo());
        model.addAttribute("action", "/utilisateurs/" + idUtilisateur + "/supprimer");
        model.addAttribute("back", "/utilisateurs");
        return "interfaces/confirmation";
    }

    @PostMapping("/{idUtilisateur}/supprimer")
    public String supprimerUtilisateur(@PathVariable int idUtilisateur) {
        utilisateurService.supprimerUtilisateurParId(idUtilisateur);
        return "redirect:/utilisateurs";
    }

    @GetMapping("/{utilisateurId}/edit")
    public String getUtilisateurEditForm(@PathVariable("utilisateurId") int utilisateurId, Model model) {
        Utilisateur utilisateur = utilisateurService.consulterUtilisateurParId(utilisateurId);
        model.addAttribute("utilisateur", utilisateur);
        return "utilisateurs/modification";
    }

    @PostMapping("/edit")
    public String postUtilisateurEditForm(@Valid @ModelAttribute("utilisateur") Utilisateur utilisateur, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "utilisateurs/modification";
        }

        utilisateurService.modifierUtilisateur(utilisateur);
        return "redirect:/utilisateurs";
    }

    @GetMapping("/{utilisateurId}/detail")
    public String getUtilisateurDetailForm(@PathVariable("utilisateurId") int utilisateurId, Model model) {
        Utilisateur utilisateur = utilisateurService.consulterUtilisateurParId(utilisateurId);
        model.addAttribute("utilisateur", utilisateur);

        // Ajoute les messages postés et les messages likés
        List<Post> posts = postService.getPostsBySender(utilisateur.getPseudo());
        List<Post> likedPosts = postService.getLikedPostsByUser(utilisateur.getPseudo());
        List<Comment> comments = commentService.findCommentsBySender(utilisateur.getPseudo());

        model.addAttribute("posts", posts);
        model.addAttribute("likedPosts", likedPosts);
        model.addAttribute("comments", comments);
        return "utilisateurs/detail";
    }

    @PostMapping("/detail")
    public String postUtilisateurDetailForm(@Valid @ModelAttribute("utilisateur") Utilisateur utilisateur, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "utilisateurs/detail";
        }

        utilisateurService.modifierUtilisateur(utilisateur);
        return "redirect:/utilisateurs";
    }
}
