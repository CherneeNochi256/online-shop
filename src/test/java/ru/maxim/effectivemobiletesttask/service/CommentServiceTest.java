package ru.maxim.effectivemobiletesttask.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import ru.maxim.effectivemobiletesttask.dto.comment.CommentDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.comment.CommentDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.*;
import ru.maxim.effectivemobiletesttask.exception.CanNotPerformActionException;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.CommentRepository;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;
import ru.maxim.effectivemobiletesttask.repository.PurchaseHistoryRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static ru.maxim.effectivemobiletesttask.utils.AppConstants.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private PurchaseHistoryRepository purchaseHistoryRepository;
    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private CommentService underTest;

    private final static String YOU_CAN_NOT_COMMENT_THE_PRODUCT = "You can't comment the product,because you didn't buy it";


    private User orgOwner;
    private User customer;
    private PurchaseHistory purchaseHistory;
    private HashSet<PurchaseHistory> purchaseHistories;
    private Product boughtProduct;
    private Product unBoughtProduct;
    private HashSet<Product> products;
    private Organization organization;
    private Long productId;
    private CommentDtoRequest commentDto;


    @BeforeEach
    void setUp() {
        //given
        orgOwner = User.builder()
                .username("owner")
                .build();

        customer = User.builder()
                .username("customer")
                .build();

        purchaseHistory = PurchaseHistory.builder()
                .date(new Date())
                .build();

        purchaseHistories = new HashSet<>();
        purchaseHistories.add(purchaseHistory);

        boughtProduct = Product.builder()
                .id(1L)
                .price(100.0)
                .quantity(12L)
                .build();

        unBoughtProduct = Product.builder()
                .id(2L)
                .price(200.0)
                .quantity(22L)
                .build();

        products = new HashSet<>();
        products.add(boughtProduct);

        organization = Organization.builder()
                .name("org")
                .products(products)
                .status("ACTIVE")
                .build();

        commentDto = new CommentDtoRequest();
        commentDto.setMessage("message");


        organization.setUser(orgOwner);
        boughtProduct.setOrganization(organization);

        purchaseHistory.setUser(customer);
        purchaseHistory.setProduct(boughtProduct);


        productId = 1L;
        commentDto = CommentDtoRequest.builder()
                .message("this is a comment")
                .build();

    }


    @Test
    void commentProduct() {

        given(purchaseHistoryRepository.findByUser(customer))
                .willReturn(Optional.of(purchaseHistories));

        given(productRepository.findById(productId))
                .willReturn(Optional.of(boughtProduct));

        given(mapper.map(commentDto, Comment.class))
                .willReturn(Comment.builder()
                        .message(commentDto.getMessage())
                        .build());


        //when
        underTest.commentProduct(productId, customer, commentDto);

        //then
        verify(commentRepository).save(any());
    }

    @Test
    void NotCommentProduct_WhenCustomerDidNotBuyIt() {

        given(purchaseHistoryRepository.findByUser(customer))
                .willReturn(Optional.of(purchaseHistories));

        given(productRepository.findById(productId))
                .willReturn(Optional.of(unBoughtProduct));

        given(mapper.map(commentDto, Comment.class))
                .willReturn(Comment.builder()
                        .message(commentDto.getMessage())
                        .build());


        //when
        assertThrows(CanNotPerformActionException.class, () -> underTest.commentProduct(productId, customer, commentDto));

        //then
        verify(commentRepository, never()).save(any());
    }


    @Test
    void WhenCommentProduct_ThenThrowsCanNotPerformActionException() {

        given(purchaseHistoryRepository.findByUser(customer))
                .willThrow(new CanNotPerformActionException(YOU_CAN_NOT_COMMENT_THE_PRODUCT));

        //when
        assertThrows(CanNotPerformActionException.class, () -> underTest.commentProduct(productId, customer, commentDto));

        //then
        verify(commentRepository, never()).save(any());
    }

    @Test
    void WhenCommentProduct_ThenThrowsResourceNotFoundException() {

        when(purchaseHistoryRepository.findByUser(customer))
                .thenReturn(Optional.of(purchaseHistories));

        when(productRepository.findById(productId))
                .thenThrow(new ResourceNotFoundException(PRODUCT, ID, productId));

        //when
        assertThrows(ResourceNotFoundException.class, () -> underTest.commentProduct(productId, customer, commentDto));

        //then
        verify(commentRepository, never()).save(any());
    }

    @Test
    void getCommentById() {
        Comment comment = Comment.builder()
                .message("message")
                .build();

        given(commentRepository.findById(1L))
                .willReturn(Optional.of(comment));

        CommentDtoResponse expectedResponse = mapper.map(comment, CommentDtoResponse.class);

        //when
        ResponseEntity<CommentDtoResponse> response = underTest.getCommentById(1L);

        //then
        verify(commentRepository).findById(1L);
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void WhenGetCommentById_ThrowsResourceNotFoundExcepotion() {

        given(commentRepository.findById(1L))
                .willThrow(new ResourceNotFoundException(COMMENT, ID, 1L));

        //when
        assertThrows(ResourceNotFoundException.class, () -> underTest.getCommentById(1L));
    }
}