package dev.bcharthur.socialmediaspringboot.controller.chat;

import dev.bcharthur.socialmediaspringboot.model.Chat;
import dev.bcharthur.socialmediaspringboot.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/chat")
    public String chat(@RequestParam("user") String otherUser, Principal principal, Model model) {
        String currentUser = principal.getName();

        model.addAttribute("receiver", otherUser);
        model.addAttribute("sender", currentUser);

        List<Chat> conversations = chatService.getConversationBetween(currentUser, otherUser);
        model.addAttribute("conversations", conversations);

        return "chat/chat";
    }

    @GetMapping("/conversation/{sender}/{receiver}")
    public String conversation(@PathVariable String sender, @PathVariable String receiver, Model model) {
        List<Chat> conversations = chatService.getConversationBetween(sender, receiver);
        model.addAttribute("conversations", conversations);
        model.addAttribute("sender", sender);
        model.addAttribute("receiver", receiver);
        return "chat/chat";
    }
}

@RestController
@RequestMapping("/api/chats")
class ChatAPIController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/list")
    public List<Chat> listAllChats() {
        return chatService.listAll();
    }

    @GetMapping("/conversation")
    public List<Chat> getConversation(@RequestParam String sender, @RequestParam String receiver) {
        return chatService.getConversationBetween(sender, receiver);
    }

    @PostMapping("/send")
    public Chat sendMessage(@RequestBody Chat chat) {
        return chatService.saveMessage(chat);
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/chats")
    public Chat broadcastMessage(Chat chat) {
        return chatService.saveMessage(chat);
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Chat updateMessage(@PathVariable Long id, @RequestBody String newMessage) {
        return chatService.updateMessage(id, newMessage);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        chatService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/hide/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Chat hideMessage(@PathVariable Long id) {
        return chatService.hideMessage(id);
    }

    @GetMapping("/active-conversations")
    public List<String> getActiveConversations(Principal principal) {
        String currentUser = principal.getName();
        return chatService.getActiveConversations(currentUser);
    }
}
