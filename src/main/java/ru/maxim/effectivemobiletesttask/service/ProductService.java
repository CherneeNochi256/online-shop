package ru.maxim.effectivemobiletesttask.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.repository.OrganizationsRepository;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;
import ru.maxim.effectivemobiletesttask.repository.PurchaseHistoryRepository;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final OrganizationsRepository organizationsRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;

    public ProductService(ProductRepository productRepository, OrganizationsRepository organizationsRepository, PurchaseHistoryRepository purchaseHistoryRepository) {
        this.productRepository = productRepository;
        this.organizationsRepository = organizationsRepository;
        this.purchaseHistoryRepository = purchaseHistoryRepository;
    }

    public Product productById(Long id){
        return productRepository.findById(id).orElse(null);
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

    public void buyProduct(Product product, User user) {
        if (product.getPrice() < user.getBalance()) {
            user.setBalance(user.getBalance() - product.getPrice());

            product.getOrganization().getUser().setBalance(product.getOrganization().getUser().getBalance() + product.getPrice() - (product.getPrice() * 0.05));
            product.setQuantity(product.getQuantity() - 1);

            PurchaseHistory purchaseHistory = new PurchaseHistory();

            purchaseHistory.setUser(user);
            purchaseHistory.setProduct(product);
            purchaseHistory.setDate(new Date());
            purchaseHistoryRepository.save(purchaseHistory);
        }
    }

    public void refundProduct(Product product, User user, Set<PurchaseHistory> purchaseHistory) {
        for (PurchaseHistory purchase : purchaseHistory) {
            if (purchase.getProduct().equals(product) && TimeUnit.MILLISECONDS.toHours(new Date().getTime() - purchase.getDate().getTime()) <= 24L) {
                user.setBalance(user.getBalance() + product.getPrice());
                product.getOrganization().getUser().setBalance(product.getOrganization().getUser().getBalance() - product.getPrice());
                product.setQuantity(product.getQuantity() + 1);
                purchaseHistoryRepository.removeById(purchase.getId());
            }
        }
    }

    private static boolean isOwner(User user, Organization organization) {
        return organization.getUser().equals(user);
    }

    private static boolean isOwner(Product productFromDb, User user) {
        return isOwner(user, productFromDb.getOrganization());
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
