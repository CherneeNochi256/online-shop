package ru.maxim.effectivemobiletesttask.rest;


import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.ProductService;
import ru.maxim.effectivemobiletesttask.service.PurchaseHistoryService;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("api/main/product")
public class ProductController {

    private final ProductService productService;
    private final PurchaseHistoryService purchaseHistoryService;


    public ProductController(ProductService productService, PurchaseHistoryService purchaseHistoryService) {
        this.productService = productService;
        this.purchaseHistoryService = purchaseHistoryService;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORG_OWNER')")
    @PostMapping("{orgName}")
    public void createProduct(@RequestBody Product product,
                              @PathVariable String orgName,
                              @AuthenticationPrincipal User user) {
        RestPreconditions.checkNotNull(product);
        productService.createProductByUser(product, orgName, user);
    }

    @PreAuthorize("hasAuthority('ORG_OWNER')")
    @PutMapping("{id}")
    public void update(@RequestBody Product product,
                       @PathVariable("id") Product productFromDb,
                       @AuthenticationPrincipal User user) {
        RestPreconditions.checkNotNull(product);
        RestPreconditions.checkProduct(productFromDb);

        productService.updateProductByUser(product, productFromDb, user);

    }

    @GetMapping("{id}")
    public Product get(@PathVariable("id") Product product) {
        return RestPreconditions.checkProduct(product);
    }

    @GetMapping()
    public List<Product> getAll() {
        return RestPreconditions.checkNotNull(productService.findAll());
    }

    @PostMapping("buy/{id}")
    public void buy(@PathVariable("id") Long id,
                    @AuthenticationPrincipal User user) {
        Product product = RestPreconditions.checkProduct(productService.productById(id));

        productService.buyProduct(product, user);

    }

    @PostMapping("refund/{id}")
    public void refund(@PathVariable("id") Long id,
                       @AuthenticationPrincipal User user) {
        Product product = RestPreconditions.checkProduct(productService.productById(id));
        Set<PurchaseHistory> purchaseHistory = RestPreconditions.checkPurchaseHistory(purchaseHistoryService.findByUser(user));

        productService.refundProduct(product, user, purchaseHistory);
    }
}
