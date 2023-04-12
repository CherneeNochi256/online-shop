package ru.maxim.effectivemobiletesttask.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.AdminService;
import ru.maxim.effectivemobiletesttask.service.NotificationService;
import ru.maxim.effectivemobiletesttask.service.PurchaseHistoryService;
import ru.maxim.effectivemobiletesttask.service.UserService;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

import java.util.Set;

@RestController
@RequestMapping("api/main/purchases")
@RequiredArgsConstructor
public class PurchaseHistoryController {
    private final UserService userService;
    private final PurchaseHistoryService purchaseHistoryService;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{userId}")
    public Set<PurchaseHistory> specificUserHistory(@PathVariable Long userId) {
        User user = RestPreconditions.checkUser(userService.userById(userId));

        return RestPreconditions.checkPurchaseHistory(purchaseHistoryService.findByUser(user));
    }

    @GetMapping
    public Set<PurchaseHistory> history(@AuthenticationPrincipal User user) {
        return RestPreconditions.checkPurchaseHistory(purchaseHistoryService.findByUser(user));
    }
}
