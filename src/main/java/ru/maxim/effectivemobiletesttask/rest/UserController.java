package ru.maxim.effectivemobiletesttask.rest;

import org.springframework.beans.BeanUtils;
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
    private final NotificationRepository notificationRepository;
    private final OrganizationsRepository organizationsRepository;

    public UserController(PurchaseHistoryRepository purchaseHistoryRepository, ProductRepository productRepository,
                          CommentRepository commentRepository,
                          GradeRepository gradeRepository,
                          UserRepository userRepository,
                          NotificationRepository notificationRepository,
                          OrganizationsRepository organizationsRepository) {
        this.purchaseHistoryRepository = purchaseHistoryRepository;
        this.productRepository = productRepository;
        this.commentRepository = commentRepository;
        this.gradeRepository = gradeRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.organizationsRepository = organizationsRepository;
    }

    @PostMapping("buy/{id}")
    public void buy(@PathVariable("id") String id,
                    @AuthenticationPrincipal User user) {
        Optional<Product> productFromDb = productRepository.findById(Long.parseLong(id));

        if (productFromDb.isPresent() && productFromDb.get().getPrice() < user.getBalance()) {
            user.setBalance(user.getBalance() - productFromDb.get().getPrice());
            productFromDb.get().getOrganization().getUser().setBalance(productFromDb.get().getOrganization().getUser().getBalance() + productFromDb.get().getPrice() - (productFromDb.get().getPrice() * 0.05));
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
                if (purchase.getProduct().equals(productFromDb.get()) && TimeUnit.MILLISECONDS.toHours(new Date().getTime() - purchase.getDate().getTime()) <= 24L) {
                    user.setBalance(user.getBalance() + productFromDb.get().getPrice());
                    productFromDb.get().getOrganization().getUser().setBalance(productFromDb.get().getOrganization().getUser().getBalance() - productFromDb.get().getPrice() );
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
        Set<PurchaseHistory> purchases = purchaseHistoryRepository.findByUser(user);

        for (PurchaseHistory purchase : purchases
        ) {
            if (purchase.getProduct().equals(product)) {

                Comment resultComment = new Comment();

                resultComment.setUser(user);
                resultComment.setMessage(comment.getMessage());
                resultComment.setProduct(product);

                commentRepository.save(resultComment);
            }
        }

    }

    @PostMapping("grade/{id}")
    public void estimate(@PathVariable("id") Product product,
                         @AuthenticationPrincipal User user,
                         @RequestBody Grade grade
    ) {
        Set<PurchaseHistory> purchases = purchaseHistoryRepository.findByUser(user);

        for (PurchaseHistory purchase : purchases
        ) {
            if (purchase.getProduct().equals(product)) {

                Grade resultGrade = new Grade();

                resultGrade.setUser(user);
                resultGrade.setValue(grade.getValue());
                resultGrade.setProduct(product);

                gradeRepository.save(resultGrade);
            }
        }
    }


    @GetMapping("history")
    public Set<PurchaseHistory> history(@AuthenticationPrincipal User user) {
        return purchaseHistoryRepository.findByUser(user);
    }

    @GetMapping("notification")
    public Set<Notification> getNotifications(@AuthenticationPrincipal User user){
        return notificationRepository.findByUser(user);
    }

    @PostMapping("organization")
    public void createOrganization(@AuthenticationPrincipal User user,
                                   @RequestBody Organization organization){

        Optional<User> userFromDb = userRepository.findById(user.getId());

        Organization resultOrganization = new Organization();

        BeanUtils.copyProperties(organization,resultOrganization,"id","status");
        userFromDb.get().getRoles().add(Role.ORG_OWNER);
        resultOrganization.setUser(userFromDb.get());
        resultOrganization.setStatus("ACTIVE");

        userRepository.save(userFromDb.get());
        organizationsRepository.save(resultOrganization);

    }
}
