package ru.maxim.effectivemobiletesttask.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoResponse;
import ru.maxim.effectivemobiletesttask.service.ProductService;

@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("api/main/admin/product")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductService productService;


    @PostMapping
    public ResponseEntity<ProductDtoResponse> create(@RequestBody @Valid ProductDtoRequest productDto) {
        return productService.createProductByAdmin(productDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductDtoResponse> update(@RequestBody @Valid ProductDtoRequest productDto,
                                                     @PathVariable("id") Long productId) {
        return productService.updateProductByAdmin(productDto, productId);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDtoResponse> get(@PathVariable("id") Long id) {
        return productService.productById(id);
    }
}
