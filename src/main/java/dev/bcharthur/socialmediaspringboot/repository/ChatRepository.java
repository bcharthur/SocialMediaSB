package dev.bcharthur.socialmediaspringboot.repository;

import dev.bcharthur.socialmediaspringboot.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    // Méthode pour récupérer les messages entre deux utilisateurs indépendamment de l'ordre
    @Query("SELECT c FROM Chat c WHERE (c.sender = :user1 AND c.receiver = :user2) OR (c.sender = :user2 AND c.receiver = :user1) ORDER BY c.createdDate ASC")
    List<Chat> findBySenderAndReceiverOrReceiverAndSender(@Param("user1") String user1, @Param("user2") String user2);

    // Méthode pour trouver les conversations actives d'un utilisateur
    @Query("SELECT DISTINCT c.sender FROM Chat c WHERE c.receiver = :currentUser " +
            "UNION SELECT DISTINCT c.receiver FROM Chat c WHERE c.sender = :currentUser")
    List<String> findActiveConversations(@Param("currentUser") String currentUser);

    @Query("SELECT COUNT(c) FROM Chat c WHERE c.receiver = :receiver AND c.sender = :sender AND c.readMessage = false")
    int countUnreadMessages(@Param("receiver") String receiver, @Param("sender") String sender);

    @Modifying
    @Query("UPDATE Chat c SET c.readMessage = true WHERE c.receiver = :receiver AND c.sender = :sender AND c.readMessage = false")
    void markMessagesAsRead(@Param("sender") String sender, @Param("receiver") String receiver);

    @Query("SELECT DISTINCT c.sender FROM Chat c WHERE c.receiver = :receiver")
    List<String> findDistinctSenders(@Param("receiver") String receiver);

}
