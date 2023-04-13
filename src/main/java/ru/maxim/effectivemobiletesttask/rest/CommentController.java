package ru.maxim.effectivemobiletesttask.rest;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.comment.CommentDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.comment.CommentDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.Comment;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.CommentService;
import ru.maxim.effectivemobiletesttask.service.ProductService;
import ru.maxim.effectivemobiletesttask.service.PurchaseHistoryService;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

import java.util.Set;

@RestController
@RequestMapping("api/main/comment")
@RequiredArgsConstructor
public class CommentController {

    private final PurchaseHistoryService purchaseHistoryService;
    private final ProductService productService;
    private final CommentService commentService;
    private final ModelMapper mapper;


    @PostMapping("{id}")
    public void createComment(@PathVariable("id") Long id,
                              @AuthenticationPrincipal User user,
                              @RequestBody CommentDtoRequest commentDto) {
        RestPreconditions.checkNotNull(commentDto);
        Set<PurchaseHistory> purchases = RestPreconditions.checkPurchaseHistory(purchaseHistoryService.findByUser(user));
        Product product = RestPreconditions.checkProduct(productService.productById(id));

        Comment comment = mapper.map(commentDto,Comment.class);

        commentService.commentProduct(product, user, comment, purchases);

    }


    @GetMapping("{id}")
    public CommentDtoResponse getComment(@PathVariable Long id) {
        Comment comment = RestPreconditions.checkComment(commentService.getCommentById(id));

        return mapper.map(comment, CommentDtoResponse.class);
    }
}
