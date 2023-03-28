package ru.maxim.effectivemobiletesttask.rest;

import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.Discount;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.Tag;
import ru.maxim.effectivemobiletesttask.repository.DiscountRepository;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/main/discount")
public class DiscountController {

    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;


    public DiscountController(DiscountRepository discountRepository, ProductRepository productRepository) {
        this.discountRepository = discountRepository;
        this.productRepository = productRepository;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{id}")
    public Discount get(@PathVariable String id) {
        Optional<Discount> discount = discountRepository.findById(Long.parseLong(id));
        if (discount.isPresent()) {
            return discount.get();
        } else throw new NoSuchElementException();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("{product}")
    public void createForOne(@RequestBody Discount discount,
                             @PathVariable Product product) {
        Optional<Product> productFromDb = productRepository.findById(product.getId());

        productFromDb.ifPresent(value -> value.setDiscount(discount));
        discountRepository.save(discount);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("tags/{tag}")
    public void createForGroup(@RequestBody Discount discount,
                               @PathVariable String tag) {
        Set<Product> productsFromDb = productRepository.findProductsByTag(tag);

        for (Product product : productsFromDb) {
            product.setDiscount(discount);
        }
        discountRepository.save(discount);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public void update(@RequestBody Discount discount,
                       @PathVariable String id) {
        Optional<Discount> discountFromDb = discountRepository.findById(Long.parseLong(id));

        copyProperties(discount, discountFromDb);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("tags/{tag}")
    public void updateForGroup(@RequestBody Discount discount,
                               @PathVariable String tag) {
        Product productFromDb = productRepository.findProductByTag(tag);

        Optional<Discount> discountFromDb = discountRepository.findById(productFromDb.getDiscount().getId());

        copyProperties(discount, discountFromDb);


    }


    private void copyProperties(Discount discount, Optional<Discount> discountFromDb) {
        if (discountFromDb.isPresent()) {
            BeanUtils.copyProperties(discount, discountFromDb.get(), "id");

            discountRepository.save(discountFromDb.get());
        }
    }

}

