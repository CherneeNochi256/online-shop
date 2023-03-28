package ru.maxim.effectivemobiletesttask.rest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.Comment;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;
import ru.maxim.effectivemobiletesttask.repository.PurchaseHistoryRepository;

import java.util.Optional;

@RestController
@RequestMapping("api/main/user")
public class UserController {

    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final ProductRepository productRepository;

    public UserController(PurchaseHistoryRepository purchaseHistoryRepository, ProductRepository productRepository) {
        this.purchaseHistoryRepository = purchaseHistoryRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("buy/{id}")
    public void buy(@PathVariable("id") String id,
                    @AuthenticationPrincipal User user) {
        Optional<Product> productFromDb = productRepository.findById(Long.parseLong(id));

        if (productFromDb.isPresent() && productFromDb.get().getPrice() < user.getBalance()) {
            user.setBalance(user.getBalance() - productFromDb.get().getPrice());
            PurchaseHistory purchaseHistory = new PurchaseHistory();
            purchaseHistory.setUser(user);
            purchaseHistory.setProduct(productFromDb.get());
            purchaseHistoryRepository.save(purchaseHistory);
        }
    }

    @PostMapping("comment/{id}")
    public void comment(@PathVariable("id") Product product,
                        @AuthenticationPrincipal User user,
                        @RequestBody Comment comment
                        ){

    }
}
