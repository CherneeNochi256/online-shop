package ru.maxim.effectivemobiletesttask.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.entity.Comment;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.repository.CommentRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void commentProduct(Product product, User user, Comment comment, Set<PurchaseHistory> purchases) {
        for (PurchaseHistory purchase : purchases) {
            if (purchase.getProduct().equals(product)) {

                Comment resultComment = new Comment();

                resultComment.setUser(user);
                resultComment.setMessage(comment.getMessage());
                resultComment.setProduct(product);

                commentRepository.save(resultComment);
            }
        }
    }
    public Comment getCommentById(Long id){
        return commentRepository.findById(id).orElse(null);
    }
}
