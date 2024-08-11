package dev.bcharthur.socialmediaspringboot.repository;

import dev.bcharthur.socialmediaspringboot.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT COUNT(p) FROM Post p WHERE p.sender = :pseudo")
    int countBySender(@Param("pseudo") String pseudo);

    @Query("SELECT COUNT(p) FROM Post p WHERE :pseudo MEMBER OF p.likedBy")
    int countLikesBySender(@Param("pseudo") String pseudo);

    List<Post> findBySender(String sender);

    @Query("SELECT p FROM Post p WHERE :username MEMBER OF p.likedBy")
    List<Post> findLikedPostsByUser(@Param("username") String username);
}
