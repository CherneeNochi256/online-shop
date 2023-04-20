package ru.maxim.effectivemobiletesttask.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.dto.comment.CommentDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.comment.CommentDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.Comment;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.exception.CanNotPerformActionException;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.CommentRepository;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;
import ru.maxim.effectivemobiletesttask.repository.PurchaseHistoryRepository;

import java.util.Set;

import static ru.maxim.effectivemobiletesttask.utils.AppConstants.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final static String YOU_CAN_NOT_COMMENT_THE_PRODUCT = "You can't comment the product,because you didn't buy it";

    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final ModelMapper mapper;

    public ResponseEntity<CommentDtoResponse> commentProduct(Long productId, User user, CommentDtoRequest commentDto) {

        Set<PurchaseHistory> purchases = purchaseHistoryRepository.findByUser(user)
                .orElseThrow(() -> new CanNotPerformActionException(YOU_CAN_NOT_COMMENT_THE_PRODUCT));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT, ID, productId));

        Comment comment = mapper.map(commentDto, Comment.class);


        for (PurchaseHistory purchase : purchases) {
            if (purchase.getProduct().equals(product)) {

                Comment resultComment = new Comment();

                resultComment.setUser(user);
                resultComment.setMessage(comment.getMessage());
                resultComment.setProduct(product);

                Comment savedComment = commentRepository.save(resultComment);

                CommentDtoResponse response = mapper.map(savedComment, CommentDtoResponse.class);

                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
        }
        throw new CanNotPerformActionException(YOU_CAN_NOT_COMMENT_THE_PRODUCT);
    }

    public ResponseEntity<CommentDtoResponse> getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT, ID, id));

        CommentDtoResponse response = mapper.map(comment, CommentDtoResponse.class);
        return ResponseEntity.ok(response);
    }
}
