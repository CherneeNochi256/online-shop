package ru.maxim.effectivemobiletesttask.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.ApiResponse;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoResponse;
import ru.maxim.effectivemobiletesttask.dto.purchaseHistory.PurchaseHistoryDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.*;

import java.util.Set;

@RestController
@RequestMapping("api/main/purchases")
@RequiredArgsConstructor
public class PurchaseHistoryController {
    private final PurchaseHistoryService purchaseHistoryService;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{userId}")
    public ResponseEntity<Set<ProductDtoResponse>> specificUserHistory(@PathVariable Long userId) {
        return purchaseHistoryService.findByUserId(userId);
    }

    @GetMapping
    public ResponseEntity<Set<ProductDtoResponse>> history(@AuthenticationPrincipal User user) {
        return purchaseHistoryService.findByUserId(user.getId());
    }


    @PostMapping("{productId}")
    public ResponseEntity<PurchaseHistoryDtoResponse> buy(@PathVariable Long productId,
                                                          @AuthenticationPrincipal User user) {
        return purchaseHistoryService.buyProduct(productId, user);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> refund(@PathVariable("id") Long id,
                                              @AuthenticationPrincipal User user) {
       return purchaseHistoryService.refundProduct(id, user);
    }
}
