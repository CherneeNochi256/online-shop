package ru.maxim.effectivemobiletesttask.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.CommentDto;
import ru.maxim.effectivemobiletesttask.entity.Comment;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.CommentService;
import ru.maxim.effectivemobiletesttask.service.ProductService;
import ru.maxim.effectivemobiletesttask.service.PurchaseHistoryService;
import ru.maxim.effectivemobiletesttask.utils.EntityMapper;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

import java.util.Set;

@RestController
@RequestMapping("api/main/comment")
@RequiredArgsConstructor
public class CommentController {

    private final PurchaseHistoryService purchaseHistoryService;
    private final ProductService productService;
    private final CommentService commentService;
    private final EntityMapper entityMapper;


    @PostMapping("{id}")
    public void createComment(@PathVariable("id") Long id,
                              @AuthenticationPrincipal User user,
                              @RequestBody CommentDto.Request commentDto) {
        RestPreconditions.checkNotNull(commentDto);
        Set<PurchaseHistory> purchases = RestPreconditions.checkPurchaseHistory(purchaseHistoryService.findByUser(user));
        Product product = RestPreconditions.checkProduct(productService.productById(id));

        Comment comment = entityMapper.commentDtoToEntity(commentDto);

        commentService.commentProduct(product, user, comment, purchases);

    }


    @GetMapping("{id}")
    public CommentDto.Response getComment(@PathVariable Long id) {
        Comment comment = RestPreconditions.checkComment(commentService.getCommentById(id));

        return entityMapper.entityToCommentDto(comment);
    }
}
