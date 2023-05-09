package ru.maxim.effectivemobiletesttask.rest;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoResponse;
import ru.maxim.effectivemobiletesttask.dto.product.ProductUpdateRequest;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.ProductService;

import java.util.Set;


@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORG_OWNER')")
    @PostMapping
    public ResponseEntity<ProductDtoResponse> createProduct(@RequestBody @Valid ProductDtoRequest productDto,
                                                            @AuthenticationPrincipal User user) {
        return productService.createProductByUser(productDto, user);
    }

    @PreAuthorize("hasAuthority('ORG_OWNER')")
    @PutMapping("{id}")
    public ResponseEntity<ProductDtoResponse> update(@RequestBody @Valid ProductUpdateRequest productDto,
                                                     @PathVariable("id") Long id,
                                                     @AuthenticationPrincipal User user) {
        return productService.updateProductByUser(productDto, id, user);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDtoResponse> get(@PathVariable("id") Long id) {
        return productService.productById(id);
    }

    @GetMapping
    public ResponseEntity<Set<ProductDtoResponse>> getAll(@RequestParam(required = false, defaultValue = "10") Integer size,
                                                          @RequestParam(required = false, defaultValue = "0") Integer page)

    {
        return productService.findAll(PageRequest.of(page,size));
    }
}
