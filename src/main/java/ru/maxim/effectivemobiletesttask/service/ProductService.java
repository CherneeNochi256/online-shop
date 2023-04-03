package ru.maxim.effectivemobiletesttask.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.repository.OrganizationsRepository;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final OrganizationsRepository organizationsRepository;

    public ProductService(ProductRepository productRepository, OrganizationsRepository organizationsRepository) {
        this.productRepository = productRepository;
        this.organizationsRepository = organizationsRepository;
    }

    public void createProductByAdmin(Product product) {
        productRepository.save(product);
    }

    public void createProductByUser(Product product, String orgName, User user) {
        Organization organization = RestPreconditions.checkOrganization(organizationsRepository.findByName(orgName).get());

        if (isOwner(user, organization) && organization.getStatus().equals("ACTIVE")) {
            product.setOrganization(organization);
            productRepository.save(product);
        }

    }

    public void updateProductByAdmin(Product product, Long productId) {
        Product productFromDb = RestPreconditions.checkProduct(productRepository.findById(productId).get());


        BeanUtils.copyProperties(product, productFromDb, "id");

        productRepository.save(productFromDb);
    }

    public void updateProductByUser(Product product, Product productFromDb, User user) {

        Organization organization = RestPreconditions.checkOrganization(organizationsRepository.findByName(product.getOrganization().getName()).get());


        if (isOwner(productFromDb, user)) {
            BeanUtils.copyProperties(product, productFromDb, "id", "organization");

            if (!organization.equals(product.getOrganization()) && isOwner(user, organization)) {
                productFromDb.setOrganization(organization);
            }

            productRepository.save(productFromDb);
        }
    }

    private static boolean isOwner(User user, Organization organization) {
        return organization.getUser().equals(user);
    }

    private static boolean isOwner(Product productFromDb, User user) {
        return isOwner(user, productFromDb.getOrganization());
    }
}
