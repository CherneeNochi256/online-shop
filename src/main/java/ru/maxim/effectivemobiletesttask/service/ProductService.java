package ru.maxim.effectivemobiletesttask.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoResponse;
import ru.maxim.effectivemobiletesttask.dto.product.ProductUpdateRequest;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.OrganizationsRepository;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;

import java.util.Set;
import java.util.stream.Collectors;

import static ru.maxim.effectivemobiletesttask.utils.AppConstants.*;
import static ru.maxim.effectivemobiletesttask.utils.EntityUtils.*;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper mapper;
    private final OrganizationsRepository organizationsRepository;


    public ResponseEntity<ProductDtoResponse> productById(Long productId) {
        Product productFromDb = getProduct(productId);
        ProductDtoResponse response = mapper.map(productFromDb, ProductDtoResponse.class);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ProductDtoResponse> createProductByAdmin(ProductDtoRequest productDto) {
        Product product = mapper.map(productDto, Product.class);
        productRepository.save(product);
        ProductDtoResponse response = mapper.map(product, ProductDtoResponse.class);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ProductDtoResponse> createProductByUser(ProductDtoRequest productDto, User user) {

        String organizationName = productDto.getOrganizationName();
        Organization organizationFromDb = organizationsRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException(ORGANIZATION, NAME, organizationName));

        Product product = mapper.map(productDto, Product.class);

        isOrganizationOwner(user, organizationFromDb);
        isOrganizationActive(organizationFromDb);

        product.setOrganization(organizationFromDb);
        Product savedProduct = productRepository.save(product);

        ProductDtoResponse response = mapper.map(savedProduct, ProductDtoResponse.class);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ProductDtoResponse> updateProductByAdmin(ProductDtoRequest productDto, Long productId) {
        Product productFromDb = getProduct(productId);

        copyProperties(productDto, productFromDb);

        Product updatedProduct = productRepository.save(productFromDb);

        ProductDtoResponse response = mapper.map(updatedProduct, ProductDtoResponse.class);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ProductDtoResponse> updateProductByUser(ProductUpdateRequest productDto, Long productId, User user) {
        Product productFromDb = getProduct(productId);

        isProductOwner(productFromDb, user);

        copyProperties(productDto, productFromDb);

        Product updatedProduct = productRepository.save(productFromDb);

        ProductDtoResponse response = mapper.map(updatedProduct, ProductDtoResponse.class);
        return ResponseEntity.ok(response);

    }

    public ResponseEntity<Set<ProductDtoResponse>> findAll() {
        Set<Product> products = productRepository.findAllWhereOrganizationStatusIsActive()
                .orElseThrow(()->new ResourceNotFoundException(PRODUCT,"organization status","ACTIVE"));
        Set<ProductDtoResponse> response = products
                .stream().map(p -> mapper.map(p, ProductDtoResponse.class))
                .collect(Collectors.toSet());
        return ResponseEntity.ok(response);
    }


    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT, ID, productId));
    }
}
