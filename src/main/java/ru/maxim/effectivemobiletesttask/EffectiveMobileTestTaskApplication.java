package ru.maxim.effectivemobiletesttask;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.maxim.effectivemobiletesttask.entity.*;
import ru.maxim.effectivemobiletesttask.repository.*;

import java.util.*;

@SpringBootApplication
public class EffectiveMobileTestTaskApplication {
    private final PurchaseHistoryRepository purchaseHistoryRepository;

    public EffectiveMobileTestTaskApplication(PurchaseHistoryRepository purchaseHistoryRepository) {
        this.purchaseHistoryRepository = purchaseHistoryRepository;
    }


//    @Bean
//    CommandLineRunner initUsers(UserRepository userRepository
//    ) {
//        return (evt) -> Arrays.asList(
//                        "a,dsyer,pwebb,ogierke,rwinch,mfisher,mpollack,jlong".split(","))
//                .forEach(
//                        a -> {
//                            User user = new User();
//                            System.out.println(a);
//
//                            if (a.toString().equals("a")) {
//                                user.setUsername(a);
//                                user.setPassword("1");
//                                user.setEmail("dsfasd");
//                                user.setBalance(0.0);
//                                user.setRoles(Set.of(Role.ADMIN));
//                                userRepository.save(user);
//                            } else {
//
//                                user.setUsername(a);
//                                user.setPassword("1");
//                                user.setEmail("dsfasd");
//                                user.setBalance(0.0);
//                                user.setRoles(Collections.singleton(Role.USER));
//                                userRepository.save(user);
//                            }
//                        });
//    }
//
//    @Bean
//    CommandLineRunner initDiscounts(DiscountRepository discountRepository) {
//        return (evt) -> Arrays.asList(
//                        "discount1,dsyer,pwebb,ogierke,rwinch,mfisher,mpollack,jlong".split(","))
//                .forEach(
//                        a -> {
//                            Discount discount = new Discount();
//
//                            discount.setDiscount(0.15);
//                            discount.setInterval(3600L);
//
//                            discountRepository.save(discount);
//
//                        });
//    }
//
//    @Bean
//    CommandLineRunner initOrganizations(OrganizationsRepository organizationsRepository) {
//        return (evt) -> Arrays.asList(
//                        "Organizations1,Org2,Org3,Org4,Org5,Org6,Org7,Org8".split(","))
//                .forEach(
//                        a -> {
//
//                            Organization organization = new Organization();
//
//                            organization.setName(a);
//
//                            organizationsRepository.save(organization);
//                        });
//    }
//
//    @Bean
//    CommandLineRunner initTags(OrganizationsRepository organizationsRepository,
//                               TagRepository tagRepository) {
//        return (evt) -> Arrays.asList(
//                        "Tag1,Tag2,Tag3,Tag4,Tag5,Tag6,Tag7,Tag8".split(","))
//                .forEach(
//                        a -> {
//
//                            Tag tag = new Tag();
//
//                            tag.setTag(a);
//
//                            tagRepository.save(tag);
//                        });
//    }
//
//
//    @Bean
//    CommandLineRunner initProducts(DiscountRepository discountRepository,
//                                   ProductRepository productRepository,
//                                   OrganizationsRepository organizationsRepository,
//                                   TagRepository tagRepository) {
//        return (evt) -> Arrays.asList(
//                        "product1,prod2,prod3,prod4,prod5,prod6,prod7,prod8".split(","))
//                .forEach(
//                        a -> {
//                            Product product = new Product();
//
//                            product.setTitle(a);
//                            product.setOrganizations(organizationsRepository.findById(1L).get());
//                            product.setDiscount(discountRepository.findById(1L).get());
//                            product.setTags(Set.of(tagRepository.findById(1L).get()));
//                            product.setPrice(100.0);
//                            product.setQuantity(12L);
//
//                            productRepository.save(product);
//
//                        });
//    }

    public static void main(String[] args) {
        SpringApplication.run(EffectiveMobileTestTaskApplication.class, args);
    }

}
