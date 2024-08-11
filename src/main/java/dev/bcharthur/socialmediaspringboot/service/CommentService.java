package dev.bcharthur.socialmediaspringboot.service;

import dev.bcharthur.socialmediaspringboot.model.Comment;
import dev.bcharthur.socialmediaspringboot.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> findCommentsBySender(String sender) {
        return commentRepository.findBySender(sender);
    }
}
