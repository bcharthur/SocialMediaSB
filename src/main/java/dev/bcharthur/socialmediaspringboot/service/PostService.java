package dev.bcharthur.socialmediaspringboot.service;

import dev.bcharthur.socialmediaspringboot.model.Comment;
import dev.bcharthur.socialmediaspringboot.model.Post;
import dev.bcharthur.socialmediaspringboot.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Value("${upload.path}")
    private String uploadPath;

    public List<Post> listAll() {
        return postRepository.findAll();
    }

    @Transactional
    public Post savePost(String sender, String post, MultipartFile image) {
        Post newPost = new Post();
        newPost.setSender(sender);
        newPost.setPost(post);

        if (image != null && !image.isEmpty()) {
            try {
                String imagePath = saveImage(image, newPost.getId());
                newPost.setImageUrl(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Post savedPost = postRepository.save(newPost);
        messagingTemplate.convertAndSend("/topic/posts", savedPost);
        return savedPost;
    }

    private String saveImage(MultipartFile image, Long postId) throws IOException {
        String originalFilename = image.getOriginalFilename();
        String extension = "";
        int i = originalFilename.lastIndexOf('.');
        if (i >= 0) {
            extension = originalFilename.substring(i);
        }

        String newFilename = UUID.randomUUID().toString() + extension;  // Generate a unique filename
        Path uploadPath = Paths.get(this.uploadPath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path destinationFile = uploadPath.resolve(newFilename).normalize().toAbsolutePath();
        Files.copy(image.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

        return "/upload/" + newFilename;
    }

    @Transactional
    public Post updatePost(Long id, String newPost) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            post.setPost(newPost);
            postRepository.save(post);
            messagingTemplate.convertAndSend("/topic/posts", post);
        }
        return post;
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
        messagingTemplate.convertAndSend("/topic/posts/delete", id);
    }

    @Transactional
    public Post hidePost(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            post.setPost("Ce post a été masqué par un modérateur");
            post.setImageUrl(null); // Set imageUrl to null to indicate the image is hidden
            postRepository.save(post);
            messagingTemplate.convertAndSend("/topic/posts", post);
        }
        return post;
    }

    @Transactional
    public Post likePost(Long id, String username) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            post.like(username);
            postRepository.save(post);
            messagingTemplate.convertAndSend("/topic/posts", post);
        }
        return post;
    }

    @Transactional
    public Post unlikePost(Long id, String username) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            post.unlike(username);
            postRepository.save(post);
            messagingTemplate.convertAndSend("/topic/posts", post);
        }
        return post;
    }

    @Transactional
    public Post addComment(Long postId, String sender, String commentText) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            Comment comment = new Comment();
            comment.setSender(sender);
            comment.setComment(commentText);
            comment.setOrder(post.getComments().size() + 1);
            comment.setPost(post); // Ensure the comment is linked to the post
            post.getComments().add(comment);
            postRepository.save(post);
            messagingTemplate.convertAndSend("/topic/posts", post);
            return post;
        }
        return null;
    }

    public int countPostsByUser(String pseudo) {
        return postRepository.countBySender(pseudo);
    }

    public int countLikesByUser(String pseudo) {
        return postRepository.countLikesBySender(pseudo);
    }

    public List<Post> getPostsBySender(String sender) {
        return postRepository.findBySender(sender);
    }

    public List<Post> getLikedPostsByUser(String username) {
        // Assumes you have a method to get liked posts by username
        return postRepository.findLikedPostsByUser(username);
    }
}
