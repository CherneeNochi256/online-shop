package ru.maxim.effectivemobiletesttask.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.ApiResponse;
import ru.maxim.effectivemobiletesttask.dto.purchaseHistory.PurchaseHistoryDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.PurchaseHistoryService;

import java.util.Set;

@RestController
@RequestMapping("api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseHistoryController {
    private final PurchaseHistoryService purchaseHistoryService;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("admin")
    public ResponseEntity<Set<PurchaseHistoryDtoResponse>> specificUserHistory(@RequestParam Long userId) {
        return purchaseHistoryService.findByUserId(userId);
    }

    @GetMapping
    public ResponseEntity<Set<PurchaseHistoryDtoResponse>> history(@AuthenticationPrincipal User user) {
        return purchaseHistoryService.findByUserId(user.getId());
    }


    @PostMapping
    public ResponseEntity<PurchaseHistoryDtoResponse> buy(@RequestParam Long productId,
                                                          @AuthenticationPrincipal User user) {
        return purchaseHistoryService.buyProduct(productId, user);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> refund(@RequestParam Long productId,
                                              @AuthenticationPrincipal User user) {
        return purchaseHistoryService.refundProduct(productId, user);
    }
}
