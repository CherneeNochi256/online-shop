package ru.maxim.effectivemobiletesttask.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.ProductDto;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.ProductService;
import ru.maxim.effectivemobiletesttask.service.PurchaseHistoryService;
import ru.maxim.effectivemobiletesttask.utils.EntityMapper;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/main/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final PurchaseHistoryService purchaseHistoryService;
    private final EntityMapper entityMapper;



    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORG_OWNER')")
    @PostMapping("{orgName}")
    public void createProduct(@RequestBody ProductDto.Request productDto,
                              @PathVariable String orgName,
                              @AuthenticationPrincipal User user) {
        RestPreconditions.checkNotNull(productDto);
        Product product = entityMapper.productDtoToEntity(productDto);
        productService.createProductByUser(product, orgName, user);
    }

    @PreAuthorize("hasAuthority('ORG_OWNER')")
    @PutMapping("{id}")
    public void update(@RequestBody ProductDto.Request productDto,
                       @PathVariable("id") Long id,
                       @AuthenticationPrincipal User user) {
        RestPreconditions.checkNotNull(productDto);
        Product product = entityMapper.productDtoToEntity(productDto);
        Product productFromDb = productService.productById(id);
        RestPreconditions.checkProduct(productFromDb);

        productService.updateProductByUser(product, productFromDb, user);

    }

    @GetMapping("{id}")
    public ProductDto.Response get(@PathVariable("id") Long id) {

        Product product = RestPreconditions.checkProduct(productService.productById(id));
        return entityMapper.entityToProductDto(product);
    }

    @GetMapping()
    public List<ProductDto.Response> getAll() {
        List<Product> products = RestPreconditions.checkNotNull(productService.findAll());
        return products.stream()
                .map(entityMapper::entityToProductDto)
                .collect(Collectors.toList());
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
