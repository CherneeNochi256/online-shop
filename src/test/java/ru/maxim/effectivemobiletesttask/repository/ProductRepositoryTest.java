package ru.maxim.effectivemobiletesttask.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.maxim.effectivemobiletesttask.AbstractTestcontainers;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.Tag;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest extends AbstractTestcontainers {
    @Autowired
    private ProductRepository underTest;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private OrganizationsRepository organizationsRepository;

    @Test
    void findById() {

        //given

        Product product = Product.builder()
                .quantity(15L)
                .price(123.0)
                .build();

        underTest.save(product);
        //when

        Product productFromDb = underTest.findById(product.getId()).orElse(null);

        //then
        assertEquals(product, productFromDb);
    }

    @Test
    void findProductsByTag() {
        //given

        Product product = Product.builder()
                .quantity(11L)
                .price(121.0)
                .build();


        Product product2 = Product.builder()
                .quantity(13L)
                .price(156.0)
                .build();

        underTest.save(product);
        underTest.save(product2);

        Tag tag = Tag.builder()
                .tag("tag")
                .product(product)
                .build();

        tagRepository.save(tag);

        Tag tag2 = Tag.builder()
                .tag("tag")
                .product(product2)
                .build();
        tagRepository.save(tag2);


        HashSet<Product> productSet = new HashSet<>();
        productSet.add(product);
        productSet.add(product2);


        //when

        Set<Product> productsByTag = underTest.findProductsByTag("tag").orElse(null);

        //then
        assertEquals(productSet,productsByTag);
    }

    @Test
    void findProductByTag() {
        //given

        Product product = new Product();
        product.setQuantity(11L);
        product.setPrice(121.0);

        underTest.save(product);

        Tag tag = new Tag();
        tag.setTag("tag");
        tag.setProduct(product);
        tagRepository.save(tag);

        //when

        Product productByTag = underTest.findProductByTag("tag").orElse(null);

        //then
        assertEquals(product,productByTag);
    }

    @Test
    void findAll() {

        Organization organization = new Organization();
        organization.setStatus("ACTIVE");

        Organization organization2 = new Organization();
        organization2.setStatus("FREEZE");

        organizationsRepository.save(organization);
        organizationsRepository.save(organization2);


        Set<Product> products = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setPrice((double) i);
            product.setQuantity(Long.parseLong(String.valueOf(i)));

            if (i % 2 == 0) {
                product.setOrganization(organization);
                products.add(product);
            } else {
                product.setOrganization(organization2);
            }

            underTest.save(product);
        }

        //when
        Set<Product> productList = underTest.findAllWhereOrganizationStatusIsActive(PageRequest.of(0,10))
                .orElse(null);

        //then

        assertEquals(5,productList.size());
        assertTrue(productList.containsAll(products));

    }
}