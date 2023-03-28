package ru.maxim.effectivemobiletesttask.rest;

import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("api/main/product")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public void create(@RequestBody Product product) {
      repository.save(product);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public void update(@RequestBody Product product,
                       @PathVariable String id) {
        Optional<Product> productFromDb= repository.findById(Long.parseLong(id));

        if (productFromDb.isPresent()){
            BeanUtils.copyProperties(product, productFromDb.get(), "id");

            repository.save(productFromDb.get());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{id}")
    public Product get(@PathVariable String id) {
        Optional<Product> product = repository.findById(Long.parseLong(id));
        if (product.isPresent()){
            return product.get();
        }
        else throw new NoSuchElementException();
    }
}
