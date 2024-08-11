//package dev.bcharthur.loginetchat.controller.post;
//
//import dev.bcharthur.loginetchat.model.Comment;
//import dev.bcharthur.loginetchat.model.Post;
//import dev.bcharthur.loginetchat.service.PostService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.util.List;
//
//@Controller
//public class PostController {
//    @GetMapping("/post")
//    public String post(Model model, Principal principal) {
//        return "post/post";
//    }
//}
//
//@RestController
//@RequestMapping("/api/posts")
//class PostAPIController {
//
//    @Autowired
//    private PostService postService;
//
//    @GetMapping("/list")
//    public List<Post> listAllPosts() {
//        return postService.listAll();
//    }
//
//    @PostMapping("/send")
//    public Post sendPost(@RequestBody Post post) {
//        return postService.savePost(post);
//    }
//
//    @MessageMapping("/sendPost")
//    @SendTo("/topic/posts")
//    public Post broadcastPost(Post post) {
//        return postService.savePost(post);
//    }
//
//    @PostMapping("/update/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public Post updatePost(@PathVariable Long id, @RequestBody String newPost) {
//        return postService.updatePost(id, newPost);
//    }
//
//    @PostMapping("/delete/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
//        postService.deletePost(id);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/hide/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public Post hidePost(@PathVariable Long id) {
//        return postService.hidePost(id);
//    }
//
//    @PostMapping("/like/{id}")
//    public Post likePost(@PathVariable Long id, Principal principal) {
//        return postService.likePost(id, principal.getName());
//    }
//
//    @PostMapping("/unlike/{id}")
//    public Post unlikePost(@PathVariable Long id, Principal principal) {
//        return postService.unlikePost(id, principal.getName());
//    }
//
//    @PostMapping("/comment/{id}")
//    public Post addComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO, Principal principal) {
//        return postService.addComment(id, principal.getName(), commentDTO.getComment());
//    }
//}
//
//class CommentDTO {
//    private String comment;
//
//    // getters and setters...
//
//    public String getComment() {
//        return comment;
//    }
//
//    public void setComment(String comment) {
//        this.comment = comment;
//    }
//}
