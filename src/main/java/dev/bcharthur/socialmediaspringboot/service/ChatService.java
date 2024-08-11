package dev.bcharthur.socialmediaspringboot.service;

import dev.bcharthur.socialmediaspringboot.model.Chat;
import dev.bcharthur.socialmediaspringboot.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public List<Chat> listAll() {
        return chatRepository.findAll();
    }

    @Transactional
    public Chat saveMessage(Chat chat) {
        if (chat.getReceiver() == null || chat.getReceiver().isEmpty()) {
            throw new IllegalArgumentException("Receiver cannot be null or empty");
        }
        Chat savedChat = chatRepository.save(chat);
        // Notify both sender and receiver
        messagingTemplate.convertAndSend("/topic/chats/" + chat.getSender() + "/" + chat.getReceiver(), savedChat);
        messagingTemplate.convertAndSend("/topic/chats/" + chat.getReceiver() + "/" + chat.getSender(), savedChat);
        return savedChat;
    }

    public List<Chat> getConversationBetween(String user1, String user2) {
        return chatRepository.findBySenderAndReceiverOrReceiverAndSender(user1, user2);
    }

    @Transactional
    public Chat updateMessage(Long id, String newMessage) {
        Chat chat = chatRepository.findById(id).orElse(null);
        if (chat != null) {
            chat.setMessage(newMessage);
            chatRepository.save(chat);
            messagingTemplate.convertAndSend("/topic/chats/" + chat.getSender() + "/" + chat.getReceiver(), chat);
        }
        return chat;
    }

    @Transactional
    public void deleteMessage(Long id) {
        Chat chat = chatRepository.findById(id).orElse(null);
        if (chat != null) {
            chatRepository.deleteById(id);
            messagingTemplate.convertAndSend("/topic/chats/delete", id);
        }
    }

    @Transactional
    public Chat hideMessage(Long id) {
        Chat chat = chatRepository.findById(id).orElse(null);
        if (chat != null) {
            chat.setMessage("ce message a été masqué par un modérateur");
            chatRepository.save(chat);
            messagingTemplate.convertAndSend("/topic/chats/" + chat.getSender() + "/" + chat.getReceiver(), chat);
        }
        return chat;
    }

    public List<String> getActiveConversations(String currentUser) {
        List<String> activeConversations = chatRepository.findActiveConversations(currentUser);
        return activeConversations.stream().distinct().collect(Collectors.toList());
    }
}
