package ru.maxim.effectivemobiletesttask.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoResponse;
import ru.maxim.effectivemobiletesttask.service.ProductService;

@RestController
@RequestMapping("api/v1/admin/products")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductService productService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDtoResponse> create(@RequestBody @Valid ProductDtoRequest productDto) {
        return productService.createProductByAdmin(productDto);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<ProductDtoResponse> update(@RequestBody @Valid ProductDtoRequest productDto,
                                                     @PathVariable("id") Long productId) {
        return productService.updateProductByAdmin(productDto, productId);
    }
}
