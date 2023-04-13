package ru.maxim.effectivemobiletesttask.rest;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.service.ProductService;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("api/main/admin/product")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductService productService;
    private final ModelMapper mapper;


    @PostMapping
    public void create(@RequestBody ProductDtoRequest productDto) {
        RestPreconditions.checkNotNull(productDto);
        Product product = mapper.map(productDto,Product.class);
        productService.createProductByAdmin(product);
    }

    @PutMapping("{id}")
    public void update(@RequestBody ProductDtoRequest productDto,
                       @PathVariable("id") Long productId) {
        RestPreconditions.checkNotNull(productDto);
        Product product = mapper.map(productDto,Product.class);

        productService.updateProductByAdmin(product,productId);
    }

    @GetMapping("{id}")
    public ProductDtoResponse get(@PathVariable("id") Long id) {
        Product product = RestPreconditions.checkProduct(productService.productById(id));
        return mapper.map(product,ProductDtoResponse.class);
    }
}
