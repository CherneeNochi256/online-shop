package ru.maxim.effectivemobiletesttask.rest;


import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.repository.OrganizationsRepository;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;
import ru.maxim.effectivemobiletesttask.service.ProductService;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

import java.util.*;


@RestController
@RequestMapping("api/main/product")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
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
        return RestPreconditions.checkNotNull(productRepository.findAll());
    }
}
