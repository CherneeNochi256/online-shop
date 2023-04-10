package ru.maxim.effectivemobiletesttask.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.maxim.effectivemobiletesttask.entity.Discount;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.DiscountRepository;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {
    private DiscountService underTest;

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp(){
        underTest = new DiscountService(discountRepository,productRepository);
    }

    @Test
    void getDiscountById() {
        //given
        Long id = 1L;
        //when
        underTest.getDiscountById(id);

        //then
        verify(discountRepository).findById(id);

    }

    @Test
    void createDiscountForProduct() {
        //given
        Discount discount =new Discount();
        Product product = new Product();
        //when
        underTest.createDiscountForProduct(product,discount);
        //then
        assertEquals(product.getDiscount(), discount);

        verify(discountRepository).save(discount);
    }

    @Test
    void createDiscountForGroup() {
        //given
        String tag = "tag";
        Discount discount = new Discount();

        //when
        underTest.createDiscountForGroup(discount,tag);
        //then
        verify(productRepository).findProductsByTag(tag);
        verify(discountRepository).save(discount);


    }

    @Test
    void updateDiscount() {
        //given
        Long id = 1L;
        Discount discount = new Discount();

        given(discountRepository.findById(id))
                .willReturn(Optional.of(new Discount()));
        //when
        underTest.updateDiscount(discount,id);
        //then
        verify(discountRepository).findById(id);
        verify(discountRepository).save(any());
    }
    @Test
    void throwResourceNotFoundOnMethod_updateDiscount() {
        //given
        Long id = 1L;
        Discount discount = new Discount();
        //when
        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            underTest.updateDiscount(discount,id);
        });
        //then

        Assertions.assertEquals("Discount not found", exception.getMessage());
    }

    @Test
    void updateDiscountForGroup() {
        //given
        String tag = "tag";

        Discount discount = new Discount();
        discount.setId(1L);

        Discount discountFromDb = new Discount();

        Product product = new Product();
        product.setDiscount(discount);

        given(productRepository.findProductByTag(tag))
                .willReturn(product);
        given(discountRepository.findById(product.getDiscount().getId()))
                .willReturn(Optional.of(discountFromDb));
        //when
        underTest.updateDiscountForGroup(discount,tag);
        //then
        verify(productRepository).findProductByTag(tag);
        verify(discountRepository).findById(product.getDiscount().getId());
        verify(discountRepository).save(any());
    }

    @Test
    void throwResourceNotFoundOnMethod_updateDiscountForGroup() {
        //given
        String tag = "tag";

        Discount discount = new Discount();
        discount.setId(1L);

        Product product = new Product();
        product.setDiscount(discount);

        given(productRepository.findProductByTag(tag))
                .willReturn(product);
        given(discountRepository.findById(product.getDiscount().getId()))
                .willReturn(Optional.empty());
        //when

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            underTest.updateDiscountForGroup(discount,tag);
        });
        //then

        Assertions.assertEquals("Discount not found", exception.getMessage());
    }

}