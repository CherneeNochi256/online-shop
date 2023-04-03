package ru.maxim.effectivemobiletesttask.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;

import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createProduct(Product product){
        productRepository.save(product);
    }

    public void updateProduct(Product product, Long productId) {
        Optional<Product> productFromDb = productRepository.findById(productId);

        if (productFromDb.isPresent()) {
            BeanUtils.copyProperties(product, productFromDb.get(), "id");

            productRepository.save(productFromDb.get());
        }
    }
}
