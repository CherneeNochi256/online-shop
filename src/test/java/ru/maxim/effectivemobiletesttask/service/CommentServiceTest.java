package ru.maxim.effectivemobiletesttask.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.maxim.effectivemobiletesttask.entity.*;
import ru.maxim.effectivemobiletesttask.repository.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

   private CommentService underTest;

    @Mock
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp(){
        underTest = new CommentService(commentRepository);
    }



    @Test
    void commentProduct() {
        //given
        User orgOwner = new User();
        orgOwner.setUsername("username");

        User user = new User();
        orgOwner.setUsername("username");

        PurchaseHistory purchaseHistory = new PurchaseHistory();
        purchaseHistory.setDate(new Date());

        HashSet<PurchaseHistory> purchaseHistories = new HashSet<>();
        purchaseHistories.add(purchaseHistory);

        Product boughtProduct = new Product();
        boughtProduct.setPrice(100.0);
        boughtProduct.setQuantity(12L);

        Product unBoughtProduct = new Product();
        boughtProduct.setPrice(200.0);
        boughtProduct.setQuantity(22L);

        HashSet<Product> products = new HashSet<>();
        products.add(boughtProduct);

        Organization organization = new Organization();
        organization.setName("org");
        organization.setProducts(products);
        organization.setStatus("ACTIVE");

        Comment comment = new Comment();
        comment.setMessage("message");


        organization.setUser(orgOwner);
        boughtProduct.setOrganization(organization);
        comment.setProduct(boughtProduct);
        purchaseHistory.setUser(user);
        purchaseHistory.setProduct(boughtProduct);


        //when
        underTest.commentProduct(boughtProduct,user,comment,purchaseHistories);

        //then
        verify(commentRepository).save(any());
    }

    @Test
    void notCommentProduct() {
        User orgOwner = new User();
        orgOwner.setUsername("username");

        User user = new User();
        orgOwner.setUsername("username");

        PurchaseHistory purchaseHistory = new PurchaseHistory();
        purchaseHistory.setDate(new Date());

        HashSet<PurchaseHistory> purchaseHistories = new HashSet<>();
        purchaseHistories.add(purchaseHistory);

        Product boughtProduct = new Product();
        boughtProduct.setPrice(100.0);
        boughtProduct.setQuantity(12L);

        Product unBoughtProduct = new Product();
        boughtProduct.setPrice(200.0);
        boughtProduct.setQuantity(22L);

        HashSet<Product> products = new HashSet<>();
        products.add(boughtProduct);

        Organization organization = new Organization();
        organization.setName("org");
        organization.setProducts(products);
        organization.setStatus("ACTIVE");

        Comment comment = new Comment();
        comment.setMessage("message");


        organization.setUser(orgOwner);
        boughtProduct.setOrganization(organization);
        comment.setProduct(boughtProduct);
        purchaseHistory.setUser(user);
        purchaseHistory.setProduct(boughtProduct);


        //when
        underTest.commentProduct(unBoughtProduct,user,comment,purchaseHistories);

        verify(commentRepository,never()).save(any());
    }

    @Test
    void getCommentById() {

        //when
        underTest.getCommentById(1L);

        //then
        verify(commentRepository).findById(1L);
    }
}