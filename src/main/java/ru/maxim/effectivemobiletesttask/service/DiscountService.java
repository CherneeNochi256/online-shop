package ru.maxim.effectivemobiletesttask.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.dto.discount.DiscountDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.discount.DiscountDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.Discount;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.DiscountRepository;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;

import java.util.Set;

import static ru.maxim.effectivemobiletesttask.utils.AppConstants.*;
import static ru.maxim.effectivemobiletesttask.utils.EntityUtils.copyProperties;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;
    private final ModelMapper mapper;


    public ResponseEntity<DiscountDtoResponse> getDiscountById(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(DISCOUNT, ID, id));

        return ResponseEntity.ok(mapper.map(discount, DiscountDtoResponse.class));
    }

    public ResponseEntity<DiscountDtoResponse> createDiscountForProduct(DiscountDtoRequest discountDto, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT, ID, productId));

        Discount discount = mapper.map(discountDto, Discount.class);

        product.setDiscount(discount);

        Discount newDiscount = discountRepository.save(discount);
        productRepository.save(product);

        DiscountDtoResponse response = mapper.map(newDiscount, DiscountDtoResponse.class);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<DiscountDtoResponse> createDiscountForGroup(DiscountDtoRequest discountDto, String tag) {
        Discount discount = mapper.map(discountDto, Discount.class);

        Set<Product> productsFromDb = productRepository.findProductsByTag(tag)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT, TAG, tag));

        for (Product product : productsFromDb) {
            product.setDiscount(discount);
        }
        Discount newDiscount = discountRepository.save(discount);

        DiscountDtoResponse response = mapper.map(newDiscount, DiscountDtoResponse.class);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    public ResponseEntity<DiscountDtoResponse> updateDiscount(DiscountDtoRequest discountDto, Long discountId) {
        Discount discountFromDb = discountRepository.findById(discountId)
                .orElseThrow(() -> new ResourceNotFoundException(DISCOUNT, ID, discountId));

        copyProperties(discountDto, discountFromDb);

        Discount updatedDiscount = discountRepository.save(discountFromDb);

        DiscountDtoResponse response = mapper.map(updatedDiscount, DiscountDtoResponse.class);

        return ResponseEntity.ok(response);
    }


    public ResponseEntity<DiscountDtoResponse> updateDiscountForGroup(DiscountDtoRequest discountDto, String tag) {
        Product productFromDb = productRepository.findProductByTag(tag)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT, TAG, tag));

        Long discountId = productFromDb.getDiscount().getId();

        return updateDiscount(discountDto, discountId);
    }


}
