package ru.maxim.effectivemobiletesttask.rest;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoResponse;
import ru.maxim.effectivemobiletesttask.dto.product.ProductUpdateRequest;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.OrganizationService;
import ru.maxim.effectivemobiletesttask.service.ProductService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/main/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final OrganizationService organizationService;
    private final ModelMapper mapper;


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

    @GetMapping()
    public ResponseEntity<Set<ProductDtoResponse>> getAll() {
        return productService.findAll();
    }
}
