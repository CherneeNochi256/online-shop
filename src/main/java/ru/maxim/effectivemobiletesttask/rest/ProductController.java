package ru.maxim.effectivemobiletesttask.rest;


import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.repository.OrganizationsRepository;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;

import java.util.*;


@RestController
@RequestMapping("api/main/product")
public class ProductController {

    private final ProductRepository productRepository;
    private final OrganizationsRepository organizationsRepository;

    public ProductController(ProductRepository productRepository, OrganizationsRepository organizationsRepository) {
        this.productRepository = productRepository;
        this.organizationsRepository = organizationsRepository;
    }


    @PreAuthorize("hasAuthority('ORG_OWNER')")
    @PostMapping("{orgName}")
    public void createProduct(@RequestBody Product product,
                       @PathVariable String orgName,
                       @AuthenticationPrincipal User user) {
        Optional<Organization> organizationFromDb = organizationsRepository.findByName(orgName);

        if (organizationFromDb.isPresent() && organizationFromDb.get().getUser().equals(user) && organizationFromDb.get().getStatus().equals("ACTIVE")) {
            product.setOrganization(organizationFromDb.get());
            productRepository.save(product);
        }
    }

    @PreAuthorize("hasAuthority('ORG_OWNER')")
    @PutMapping("{id}")
    public void update(@RequestBody Product product,
                       @PathVariable String id,
                       @AuthenticationPrincipal User user) {
        Optional<Product> productFromDb = productRepository.findById(Long.parseLong(id));
        Optional<Organization> organizationFromDb = organizationsRepository.findByName(product.getOrganization().getName());



        if (productFromDb.isPresent() && productFromDb.get().getOrganization().getUser().equals(user)) {
            BeanUtils.copyProperties(product, productFromDb.get(), "id","organization");

            if (organizationFromDb.isPresent() && !organizationFromDb.get().equals(product.getOrganization()) && organizationFromDb.get().getUser().equals(user)) {
               productFromDb.get().setOrganization(organizationFromDb.get());
            }

            productRepository.save(productFromDb.get());
        }
    }

    @GetMapping("{id}")
    public Product get(@PathVariable String id) {
        Optional<Product> product = productRepository.findById(Long.parseLong(id));
        if (product.isPresent() && product.get().getOrganization().getStatus().equals("ACTIVE")) {
            return product.get();
        } else throw new NoSuchElementException();
    }

    @GetMapping()
    public List<Product> getAll(){
        return productRepository.findAll();
    }
}
