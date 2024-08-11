package dev.bcharthur.socialmediaspringboot.controller;

import dev.bcharthur.socialmediaspringboot.model.Post;
import dev.bcharthur.socialmediaspringboot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
public class IndexController {
    @GetMapping("/post")
    public String post(Model model, Principal principal) {
        return "index";
    }
}
@RestController
@RequestMapping("/api/posts")
class PostAPIController {

    @Autowired
    private PostService postService;

    @GetMapping("/list")
    public List<Post> listAllPosts() {
        return postService.listAll();
    }

    @PostMapping("/send")
    public Post sendPost(@RequestParam("sender") String sender,
                         @RequestParam("post") String post,
                         @RequestParam(value = "image", required = false) MultipartFile image) {
        Post savedPost = postService.savePost(sender, post, image);
        broadcastPost(savedPost);  // Broadcast the post after saving
        return savedPost;
    }

    @MessageMapping("/sendPost")
    @SendTo("/topic/posts")
    public Post broadcastPost(Post post) {
        return post;
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Post updatePost(@PathVariable Long id, @RequestBody String newPost) {
        return postService.updatePost(id, newPost);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/hide/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Post hidePost(@PathVariable Long id) {
        return postService.hidePost(id);
    }

    @PostMapping("/like/{id}")
    public Post likePost(@PathVariable Long id, Principal principal) {
        return postService.likePost(id, principal.getName());
    }

    @PostMapping("/unlike/{id}")
    public Post unlikePost(@PathVariable Long id, Principal principal) {
        return postService.unlikePost(id, principal.getName());
    }

    @PostMapping("/comment/{id}")
    public Post addComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO, Principal principal) {
        return postService.addComment(id, principal.getName(), commentDTO.getComment());
    }
}

class CommentDTO {
    private String comment;

    // getters and setters...

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

