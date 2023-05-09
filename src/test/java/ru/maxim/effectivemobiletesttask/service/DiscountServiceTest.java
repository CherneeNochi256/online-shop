package ru.maxim.effectivemobiletesttask.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import ru.maxim.effectivemobiletesttask.dto.discount.DiscountDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.discount.DiscountDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.Discount;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.Tag;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.DiscountRepository;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static ru.maxim.effectivemobiletesttask.utils.AppConstants.*;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {

    @InjectMocks
    private DiscountService underTest;

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ModelMapper mapper;


    private Discount discount;
    private Long discountId;
    private Long productId;
    private DiscountDtoRequest discountDto;
    private DiscountDtoRequest discountDtoUpdate;
    private Product product;
    private Product product2;
    private Product product3;
    private String requestTag;
    private Set<Product> products;
    private HashSet<Tag> tags;
    private Tag tag;

    @BeforeEach
    void setUp() {

        discountId = 1L;

        discount = Discount.builder()
                .id(discountId)
                .discount(0.12)
                .interval(100060L)
                .build();

        discountDto = new DiscountDtoRequest(0.12, 100060L);
        discountDtoUpdate = new DiscountDtoRequest(0.15, 8000L);

        productId = 1L;
        requestTag = "tag";

        tag = Tag.builder()
                .tag(requestTag)
                .build();

        tags = new HashSet<>();
        tags.add(tag);

        product = Product.builder()
                .tags(tags)
                .build();

        product2 = Product.builder()
                .tags(tags)
                .build();

        product3 = Product.builder()
                .tags(tags)
                .discount(discount)
                .build();


        products = new HashSet<>();

        products.add(product);
        products.add(product2);

    }

    @Test
    void getDiscountById() {
        given(discountRepository.findById(discountId))
                .willReturn(Optional.of(discount));

        DiscountDtoResponse expectedResponse = mapper.map(discount, DiscountDtoResponse.class);
        //when
        ResponseEntity<DiscountDtoResponse> response = underTest.getDiscountById(discountId);

        //then
        verify(discountRepository).findById(discountId);
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void WhenGetCommentById_ThrowsResourceNotFoundException() {

        given(discountRepository.findById(discountId))
                .willThrow(new ResourceNotFoundException(DISCOUNT, ID, discountId));

        //when
        assertThrows(ResourceNotFoundException.class, () -> underTest.getDiscountById(discountId));
    }

    @Test
    void createDiscountForProduct() {
        //given
        given(mapper.map(discountDto, Discount.class)).
                willReturn(discount);

        DiscountDtoResponse expectedResponse = mapper.map(discountDto, DiscountDtoResponse.class);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));
        //when
        ResponseEntity<DiscountDtoResponse> response = underTest.createDiscountForProduct(discountDto, productId);
        //then
        assertEquals(product.getDiscount(), discount);
        assertEquals(response.getBody(), expectedResponse);
        verify(discountRepository).save(discount);
        verify(productRepository).save(product);
    }

    @Test
    void NotCreateDiscountForProduct_WhenProductNotFound_ThenThrowsException() {
        given(productRepository.findById(productId))
                .willThrow(new ResourceNotFoundException(PRODUCT, ID, productId));
        //when
        assertThrows(ResourceNotFoundException.class, () -> underTest.createDiscountForProduct(discountDto, productId));
        //then
        verify(discountRepository, never()).save(discount);
        verify(productRepository, never()).save(product);
    }

    @Test
    void createDiscountForGroup() {
        //given

        DiscountDtoResponse expectedResponse = mapper.map(discountDto, DiscountDtoResponse.class);

        given(mapper.map(discountDto, Discount.class))
                .willReturn(discount);

        given(productRepository.findProductsByTag(requestTag))
                .willReturn(Optional.of(products));

        //when
        ResponseEntity<DiscountDtoResponse> response = underTest.createDiscountForGroup(discountDto, requestTag);
        //then
        products.forEach(p -> {
            assertEquals(p.getDiscount(), discount);
            verify(productRepository).save(p);
        });
        verify(discountRepository).save(discount);
        assertEquals(expectedResponse, response.getBody());

    }

    @Test
    void NotCreateDiscountForGroup_WhenProductNotFound_ThenThrowsException() {
        given(productRepository.findProductsByTag(requestTag))
                .willThrow(new ResourceNotFoundException(PRODUCT, ID, productId));
        //when
        assertThrows(ResourceNotFoundException.class, () -> underTest.createDiscountForGroup(discountDto, requestTag));
        //then
        verify(discountRepository, never()).save(discount);
        verify(productRepository, never()).save(product);

    }


    @Test
    void updateDiscount() {
        given(discountRepository.findById(discountId))
                .willReturn(Optional.of(discount));

        DiscountDtoResponse expectedResponse = mapper.map(discountDtoUpdate, DiscountDtoResponse.class);


        //when
        ResponseEntity<DiscountDtoResponse> response = underTest.updateDiscount(discountDtoUpdate, discountId);
        //then
        verify(discountRepository).save(discount);
        assertEquals(discount.getDiscount(), discountDtoUpdate.getDiscount());
        assertEquals(discount.getInterval(), discountDtoUpdate.getInterval());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void NotUpdateDiscount_WhenDiscountIsNotFound() {
        given(discountRepository.findById(discountId))
                .willThrow(new ResourceNotFoundException(DISCOUNT, ID, discountId));

        //when
        assertThrows(ResourceNotFoundException.class, () -> underTest.updateDiscount(discountDtoUpdate, discountId));
        //then
        verify(discountRepository, never()).save(discount);
    }

    @Test
    void updateDiscountForGroup() {
        given(productRepository.findProductByTag(requestTag))
                .willReturn(Optional.of(product3));

        given(discountRepository.findById(product3.getDiscount().getId()))
                .willReturn(Optional.of(discount));

        DiscountDtoResponse expectedResponse = mapper.map(discountDtoUpdate, DiscountDtoResponse.class);

        //when
        ResponseEntity<DiscountDtoResponse> response = underTest.updateDiscountForGroup(discountDtoUpdate, requestTag);

        //then

        verify(discountRepository).save(discount);
        assertEquals(discount.getDiscount(), discountDtoUpdate.getDiscount());
        assertEquals(discount.getInterval(), discountDtoUpdate.getInterval());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void NotUpdateDiscountForGroup_WhenThereIsNoProductsByTheTag_ThenThrowsException() {
        given(productRepository.findProductByTag(requestTag))
                .willThrow(new ResourceNotFoundException(PRODUCT, TAG, requestTag));

        //when
        assertThrows(ResourceNotFoundException.class, () -> underTest.updateDiscountForGroup(discountDtoUpdate, requestTag));

        //then

        verify(discountRepository, never()).save(discount);
    }

}