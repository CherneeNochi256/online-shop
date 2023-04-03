package ru.maxim.effectivemobiletesttask.rest;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.*;
import ru.maxim.effectivemobiletesttask.repository.*;
import ru.maxim.effectivemobiletesttask.service.UserService;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

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
    private final UserService userService;

    public UserController(PurchaseHistoryRepository purchaseHistoryRepository, ProductRepository productRepository,
                          CommentRepository commentRepository,
                          GradeRepository gradeRepository,
                          UserRepository userRepository,
                          NotificationRepository notificationRepository,
                          OrganizationsRepository organizationsRepository, UserService userService) {
        this.purchaseHistoryRepository = purchaseHistoryRepository;
        this.productRepository = productRepository;
        this.commentRepository = commentRepository;
        this.gradeRepository = gradeRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.organizationsRepository = organizationsRepository;
        this.userService = userService;
    }

    @PostMapping("buy/{id}")
    public void buy(@PathVariable("id") Product product,
                    @AuthenticationPrincipal User user) {
        RestPreconditions.checkProduct(product);

        userService.buyProduct(product, user);

    }

    @PostMapping("refund/{id}")
    public void refund(@PathVariable("id") Product product,
                       @AuthenticationPrincipal User user) {
        RestPreconditions.checkProduct(product);
        Set<PurchaseHistory> purchaseHistory = RestPreconditions.checkPurchaseHistory(purchaseHistoryRepository.findByUser(user).get());

        userService.refundProduct(product, user, purchaseHistory);
    }


    @PostMapping("comment/{id}")
    public void comment(@PathVariable("id") Product product,
                        @AuthenticationPrincipal User user,
                        @RequestBody Comment comment) {
        Set<PurchaseHistory> purchases = RestPreconditions.checkPurchaseHistory(purchaseHistoryRepository.findByUser(user).get());

        userService.commentProduct(product, user, comment, purchases);

    }


    @PostMapping("grade/{id}")
    public void estimate(@PathVariable("id") Product product,
                         @AuthenticationPrincipal User user,
                         @RequestBody Grade grade) {
        Set<PurchaseHistory> purchases = RestPreconditions.checkPurchaseHistory(purchaseHistoryRepository.findByUser(user).get());

        userService.estimateProduct(product, user, grade, purchases);
    }


    @GetMapping("history")
    public Set<PurchaseHistory> history(@AuthenticationPrincipal User user) {
        return RestPreconditions.checkPurchaseHistory(purchaseHistoryRepository.findByUser(user).get());
    }

    @GetMapping("notification")
    public Set<Notification> getNotifications(@AuthenticationPrincipal User user) {
        return RestPreconditions.checkNotifications(notificationRepository.findByUser(user));
    }

    @PostMapping("organization")
    public void createOrganization(@AuthenticationPrincipal User user,
                                   @RequestBody Organization organization) {

        RestPreconditions.checkOrganization(organization);

        userService.createOrganizationByUser(user, organization);

    }

}
