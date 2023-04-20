package ru.maxim.effectivemobiletesttask.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.dto.ApiResponse;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoResponse;
import ru.maxim.effectivemobiletesttask.dto.purchaseHistory.PurchaseHistoryDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.exception.CanNotPerformActionException;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;
import ru.maxim.effectivemobiletesttask.repository.PurchaseHistoryRepository;
import ru.maxim.effectivemobiletesttask.repository.UserRepository;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.maxim.effectivemobiletesttask.utils.AppConstants.*;
import static ru.maxim.effectivemobiletesttask.utils.EntityUtils.canBuyAProduct;
import static ru.maxim.effectivemobiletesttask.utils.EntityUtils.checkRefundDateNotExpired;

@Service
@RequiredArgsConstructor
public class PurchaseHistoryService {
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper mapper;


    public ResponseEntity<Set<ProductDtoResponse>> findByUserId(Long userId) {
        User userFromDb = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER, ID, userId));

        Set<PurchaseHistory> purchases = purchaseHistoryRepository.findByUser(userFromDb)
                .orElseThrow(() -> new ResourceNotFoundException(PURCHASE, USER, userId));

        Set<ProductDtoResponse> response = purchases.stream()
                .map(p -> mapper.map(p, ProductDtoResponse.class))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(response);
    }


    public ResponseEntity<PurchaseHistoryDtoResponse> buyProduct(Long productId, User user) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT, ID, productId));

        canBuyAProduct(user, product);

        User owner = product.getOrganization().getUser();

        user.setBalance(user.getBalance() - product.getPrice());
        owner.setBalance(owner.getBalance() + product.getPrice() - (product.getPrice() * 0.05));
        product.setQuantity(product.getQuantity() - 1);

        PurchaseHistory purchaseHistory = new PurchaseHistory();

        purchaseHistory.setUser(user);
        purchaseHistory.setProduct(product);
        purchaseHistory.setDate(new Date());

        PurchaseHistory savedPurchase = purchaseHistoryRepository.save(purchaseHistory);

        PurchaseHistoryDtoResponse response = mapper.map(savedPurchase, PurchaseHistoryDtoResponse.class);

        return ResponseEntity.ok(response);

    }

    public ResponseEntity<ApiResponse> refundProduct(Long productId, User user) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT, ID, productId));

        Set<PurchaseHistory> purchases = purchaseHistoryRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(PURCHASE, USER, user.getId()));

        for (PurchaseHistory purchase : purchases) {
            if (purchase.getProduct().equals(product)) {

                checkRefundDateNotExpired(purchase);

                User owner = product.getOrganization().getUser();

                user.setBalance(user.getBalance() + product.getPrice());
                owner.setBalance(owner.getBalance() - product.getPrice() + (product.getPrice() * 0.05));
                product.setQuantity(product.getQuantity() + 1);

                purchaseHistoryRepository.removeById(purchase.getId());

                return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, "You have successfully refund the product with id: " + productId));
            }
        }
        throw new CanNotPerformActionException("User didn't buy this product");
    }

}
