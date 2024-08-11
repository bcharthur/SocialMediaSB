package dev.bcharthur.socialmediaspringboot.listener.post;

import dev.bcharthur.socialmediaspringboot.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PostEventListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener
    public void handleAfterCreate(Post post) {
        messagingTemplate.convertAndSend("/topic/posts", post);
    }

    @TransactionalEventListener
    public void handleAfterUpdate(Post post) {
        messagingTemplate.convertAndSend("/topic/posts", post);
    }

    @TransactionalEventListener
    public void handleAfterDelete(Post post) {
        messagingTemplate.convertAndSend("/topic/posts", post);
    }
}
