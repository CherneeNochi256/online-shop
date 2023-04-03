package ru.maxim.effectivemobiletesttask.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.service.ProductService;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("api/main/admin/product")
public class ProductAdminController {

    private final ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping
    public void create(@RequestBody Product product) {
        RestPreconditions.checkNotNull(product);
        productService.createProduct(product);
    }

    @PutMapping("{id}")
    public void update(@RequestBody Product product,
                       @PathVariable("id") Long productId) {
        RestPreconditions.checkNotNull(product);

      productService.updateProduct(product,productId);
    }

    @GetMapping("{id}")
    public Product get(@PathVariable("id") Product product) {
       return RestPreconditions.checkProduct(product);
    }
}
