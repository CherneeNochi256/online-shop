package ru.maxim.effectivemobiletesttask.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.entity.Discount;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.DiscountRepository;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;


    public Discount getDiscountById(Long id) {
        return discountRepository.findById(id).orElse(null);
    }

    public void createDiscountForProduct(Product product, Discount discount) {
        product.setDiscount(discount);
        discountRepository.save(discount);
    }

    public void createDiscountForGroup(Discount discount, String tag) {
        Set<Product> productsFromDb = productRepository.findProductsByTag(tag);

        for (Product product : productsFromDb) {
            product.setDiscount(discount);
        }
        discountRepository.save(discount);
    }


    public void updateDiscount(Discount discount, Long discountId) {
        Optional<Discount> discountFromDb = discountRepository.findById(discountId);

        copyProperties(discount, discountFromDb);
    }


    public void updateDiscountForGroup(Discount discount, String tag) {

        Product productFromDb = productRepository.findProductByTag(tag);

        Optional<Discount> discountFromDb = discountRepository.findById(productFromDb.getDiscount().getId());

        copyProperties(discount, discountFromDb);
    }


    private void copyProperties(Discount discount, Optional<Discount> discountFromDb) {
        RestPreconditions.checkDiscount(discountFromDb.orElse(null));

        BeanUtils.copyProperties(discount, discountFromDb.get(), "id");

        discountRepository.save(discountFromDb.get());
    }
}