package ru.maxim.effectivemobiletesttask.rest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.Comment;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.ProductService;
import ru.maxim.effectivemobiletesttask.service.NotificationService;
import ru.maxim.effectivemobiletesttask.service.PurchaseHistoryService;
import ru.maxim.effectivemobiletesttask.service.UserService;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

import java.util.Set;

@RestController
@RequestMapping("api/main/comment")
public class CommentController {

    private final PurchaseHistoryService purchaseHistoryService;
    private final ProductService productService;
    private final UserService userService;

    public CommentController( PurchaseHistoryService purchaseHistoryService, ProductService productService, UserService userService) {
        this.purchaseHistoryService = purchaseHistoryService;
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping("{id}")
    public void comment(@PathVariable("id") Long id,
                        @AuthenticationPrincipal User user,
                        @RequestBody Comment comment) {
        Set<PurchaseHistory> purchases = RestPreconditions.checkPurchaseHistory(purchaseHistoryService.findByUser(user));
        Product product = RestPreconditions.checkProduct(productService.productById(id));

        userService.commentProduct(product, user, comment, purchases);

    }
}
