package ru.maxim.effectivemobiletesttask.rest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.Grade;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.ProductService;
import ru.maxim.effectivemobiletesttask.service.PurchaseHistoryService;
import ru.maxim.effectivemobiletesttask.service.UserService;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

import java.util.Set;

@RestController
@RequestMapping("api/main/grade")
public class GradeController {
    private final UserService userService;
    private final ProductService productService;
    private final PurchaseHistoryService purchaseHistoryService;

    public GradeController(UserService userService, ProductService productService, PurchaseHistoryService purchaseHistoryService) {
        this.userService = userService;
        this.productService = productService;
        this.purchaseHistoryService = purchaseHistoryService;
    }

    @PostMapping("{id}")
    public void estimate(@PathVariable("id") Long id,
                         @AuthenticationPrincipal User user,
                         @RequestBody Grade grade) {
        Set<PurchaseHistory> purchases = RestPreconditions.checkPurchaseHistory(purchaseHistoryService.findByUser(user));
        Product product = RestPreconditions.checkProduct(productService.productById(id));

        userService.estimateProduct(product, user, grade, purchases);
    }
}
