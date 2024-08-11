package dev.bcharthur.socialmediaspringboot.listener.chat;

import dev.bcharthur.socialmediaspringboot.model.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ChatEventListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener
    public void handleAfterCreate(Chat chat) {
        // Send message to both users involved in the conversation
        String topicPath1 = "/topic/chats/" + chat.getSender() + "/" + chat.getReceiver();
        String topicPath2 = "/topic/chats/" + chat.getReceiver() + "/" + chat.getSender();

        messagingTemplate.convertAndSend(topicPath1, chat);
        messagingTemplate.convertAndSend(topicPath2, chat);
    }

    @TransactionalEventListener
    public void handleAfterUpdate(Chat chat) {
        String topicPath1 = "/topic/chats/" + chat.getSender() + "/" + chat.getReceiver();
        String topicPath2 = "/topic/chats/" + chat.getReceiver() + "/" + chat.getSender();

        messagingTemplate.convertAndSend(topicPath1, chat);
        messagingTemplate.convertAndSend(topicPath2, chat);
    }

    @TransactionalEventListener
    public void handleAfterDelete(Chat chat) {
        String topicPath1 = "/topic/chats/" + chat.getSender() + "/" + chat.getReceiver();
        String topicPath2 = "/topic/chats/" + chat.getReceiver() + "/" + chat.getSender();

        messagingTemplate.convertAndSend(topicPath1, "Deleted: " + chat.getId());
        messagingTemplate.convertAndSend(topicPath2, "Deleted: " + chat.getId());
    }
}
