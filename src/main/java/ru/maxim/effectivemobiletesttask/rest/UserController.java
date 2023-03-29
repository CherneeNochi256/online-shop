package ru.maxim.effectivemobiletesttask.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.*;
import ru.maxim.effectivemobiletesttask.repository.*;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/main/user")
public class UserController {

    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final ProductRepository productRepository;
    private final CommentRepository commentRepository;
    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;

    public UserController(PurchaseHistoryRepository purchaseHistoryRepository, ProductRepository productRepository,
                          CommentRepository commentRepository,
                          GradeRepository gradeRepository,
                          UserRepository userRepository) {
        this.purchaseHistoryRepository = purchaseHistoryRepository;
        this.productRepository = productRepository;
        this.commentRepository = commentRepository;
        this.gradeRepository = gradeRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("buy/{id}")
    public void buy(@PathVariable("id") String id,
                    @AuthenticationPrincipal User user) {
        Optional<Product> productFromDb = productRepository.findById(Long.parseLong(id));

        if (productFromDb.isPresent() && productFromDb.get().getPrice() < user.getBalance()) {
            user.setBalance(user.getBalance() - productFromDb.get().getPrice());
            productFromDb.get().setQuantity(productFromDb.get().getQuantity() - 1);
            PurchaseHistory purchaseHistory = new PurchaseHistory();
            purchaseHistory.setUser(user);
            purchaseHistory.setProduct(productFromDb.get());
            purchaseHistoryRepository.save(purchaseHistory);
        }
    }

    @PostMapping("refund/{id}")
    public void refund(@PathVariable("id") String id,
                       @AuthenticationPrincipal User user) {
        Optional<Product> productFromDb = productRepository.findById(Long.parseLong(id));
        Set<PurchaseHistory> purchaseHistory = purchaseHistoryRepository.findByUser(user);

        if (productFromDb.isPresent()) {

            for (PurchaseHistory purchase : purchaseHistory) {
                if (purchase.getProduct().equals(productFromDb.get()) && TimeUnit.MILLISECONDS.toHours(new Date().getTime() - purchase.getDate().getTime()) <= 24L ){
                    user.setBalance(user.getBalance() + productFromDb.get().getPrice());
                    productFromDb.get().setQuantity(productFromDb.get().getQuantity() + 1);
                    purchaseHistoryRepository.removeById(purchase.getId());
                }
            }

        }
    }

    @PostMapping("comment/{id}")
    public void comment(@PathVariable("id") Product product,
                        @AuthenticationPrincipal User user,
                        @RequestBody Comment comment
    ) {
        Comment resultComment = new Comment();

        resultComment.setUser(user);
        resultComment.setMessage(comment.getMessage());
        resultComment.setProduct(product);

        commentRepository.save(resultComment);
    }

    @PostMapping("grade/{id}")
    public void estimate(@PathVariable("id") Product product,
                         @AuthenticationPrincipal User user,
                         @RequestBody Grade grade
    ) {
        Grade resultGrade = new Grade();

        resultGrade.setUser(user);
        resultGrade.setValue(grade.getValue());
        resultGrade.setProduct(product);

        gradeRepository.save(resultGrade);

    }


    @GetMapping("history")
    public Set<PurchaseHistory> history(@AuthenticationPrincipal User user) {
        return purchaseHistoryRepository.findByUser(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("history/{id}")
    public Set<PurchaseHistory> specificUserHistory(@PathVariable("id") User user) {
        return purchaseHistoryRepository.findByUser(user);
    }


}
