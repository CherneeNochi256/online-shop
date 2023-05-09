package ru.maxim.effectivemobiletesttask.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import ru.maxim.effectivemobiletesttask.dto.grade.GradeDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.grade.GradeDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.Grade;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.exception.CanNotPerformActionException;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.GradeRepository;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;
import ru.maxim.effectivemobiletesttask.repository.PurchaseHistoryRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GradeServiceTest {

    private static final String YOU_CAN_NOT_GRADE_THIS_PRODUCT = "You can't grade this product, because you didn't buy it";

    @Mock
    private GradeRepository gradeRepository;
    @Mock
    private ModelMapper mapper;
    @Mock
    private PurchaseHistoryRepository purchaseHistoryRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private GradeService underTest;

    private User user;
    private Long productId;
    private Product product;
    private PurchaseHistory purchase;
    private Set<PurchaseHistory> purchases;
    private GradeDtoRequest gradeDtoRequest;
    private Grade grade;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("username")
                .build();

        productId = 1L;

        product = Product.builder()
                .id(productId)
                .price(123.0)
                .build();

        purchase = PurchaseHistory.builder()
                .user(user)
                .product(product)
                .build();

        purchases = new HashSet<>();
        purchases.add(purchase);

        gradeDtoRequest = new GradeDtoRequest(123.0);

        grade = Grade.builder()
                .value(123.0)
                .build();
    }

    @Test
    void estimateProduct() {

        GradeDtoResponse expectedResponse = mapper.map(grade, GradeDtoResponse.class);


        given(purchaseHistoryRepository.findByUser(user))
                .willReturn(Optional.of(purchases));

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));

        given(mapper.map(gradeDtoRequest, Grade.class))
                .willReturn(grade);

        //when
        ResponseEntity<GradeDtoResponse> response = underTest.estimateProduct(productId, user, gradeDtoRequest);

        //then
        assertEquals(grade.getUser(), user);
        assertEquals(grade.getProduct(), product);
        assertEquals(response.getBody(), expectedResponse);

        verify(gradeRepository).save(grade);
    }

    @Test
    void notEstimateProduct_WhenUserDoesNotHaveAnyPurchases_ThenThrowsException() {
        given(purchaseHistoryRepository.findByUser(any(User.class)))
                .willThrow(CanNotPerformActionException.class);
        //when
        assertThrows(CanNotPerformActionException.class, () -> underTest.estimateProduct(productId, user, gradeDtoRequest));
        //then
        verify(gradeRepository, never()).save(grade);
    }

    @Test
    void notEstimateProduct_WhenProductDoesNotExists_ThenThrowsException() {
        given(purchaseHistoryRepository.findByUser(user))
                .willReturn(Optional.of(purchases));

        given(productRepository.findById(productId))
                .willThrow(ResourceNotFoundException.class);

        //when
        assertThrows(ResourceNotFoundException.class, () -> underTest.estimateProduct(productId, user, gradeDtoRequest));
        //then
        verify(gradeRepository, never()).save(grade);
    }

    @Test
    void notEstimateProduct_WhenUserDidNotBuyThisProduct() {
        given(purchaseHistoryRepository.findByUser(user))
                .willReturn(Optional.of(purchases));

        given(productRepository.findById(productId))
                .willReturn(Optional.of(new Product()));

        //when
        assertThrows(CanNotPerformActionException.class, () -> underTest.estimateProduct(productId, user, gradeDtoRequest));
        //then
        verify(gradeRepository, never()).save(grade);
    }
}