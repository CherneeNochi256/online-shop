package ru.maxim.effectivemobiletesttask.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.ProductDto;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.service.ProductService;
import ru.maxim.effectivemobiletesttask.utils.EntityMapper;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("api/main/admin/product")
public class ProductAdminController {

    private final ProductService productService;
    private final EntityMapper entityMapper;

    public ProductAdminController(ProductService productService, EntityMapper entityMapper) {
        this.productService = productService;
        this.entityMapper = entityMapper;
    }


    @PostMapping
    public void create(@RequestBody ProductDto.Request productDto) {
        RestPreconditions.checkNotNull(productDto);
        Product product = entityMapper.productDtoToEntity(productDto);
        productService.createProductByAdmin(product);
    }

    @PutMapping("{id}")
    public void update(@RequestBody ProductDto.Request productDto,
                       @PathVariable("id") Long productId) {
        RestPreconditions.checkNotNull(productDto);
        Product product = entityMapper.productDtoToEntity(productDto);

        productService.updateProductByAdmin(product,productId);
    }

    @GetMapping("{id}")
    public ProductDto.Response get(@PathVariable("id") Long id) {
        Product product = RestPreconditions.checkProduct(productService.productById(id));
        return entityMapper.entityToProductDto(product);
    }
}
