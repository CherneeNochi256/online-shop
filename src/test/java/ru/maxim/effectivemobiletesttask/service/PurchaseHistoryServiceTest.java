package ru.maxim.effectivemobiletesttask.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.maxim.effectivemobiletesttask.dto.ApiResponse;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoResponseIdAndTitle;
import ru.maxim.effectivemobiletesttask.dto.purchaseHistory.PurchaseHistoryDtoResponse;
import ru.maxim.effectivemobiletesttask.dto.user.UserDtoResponseIdAndUsername;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.exception.CanNotPerformActionException;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;
import ru.maxim.effectivemobiletesttask.repository.PurchaseHistoryRepository;
import ru.maxim.effectivemobiletesttask.repository.UserRepository;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseHistoryServiceTest {

    @InjectMocks
    private PurchaseHistoryService underTest;
    @Mock
    private PurchaseHistoryRepository purchaseHistoryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ModelMapper mapper;

    @Test
    void testFindByUserId() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        Set<PurchaseHistory> purchases = new HashSet<>();

        Product product = new Product();
        product.setId(1L);

        PurchaseHistory purchase = new PurchaseHistory();
        purchase.setProduct(product);
        purchase.setUser(user);
        purchases.add(purchase);

        Set<PurchaseHistoryDtoResponse> expectedResponse = new HashSet<>();
        PurchaseHistoryDtoResponse productDtoResponse = new PurchaseHistoryDtoResponse();
        productDtoResponse.setId(1L);
        expectedResponse.add(productDtoResponse);

        given(purchaseHistoryRepository.findByUser(user))
                .willReturn(Optional.of(purchases));

        purchases.forEach(p -> {
            given(mapper.map(p, PurchaseHistoryDtoResponse.class))
                    .willReturn(expectedResponse.stream().findFirst().orElse(null));
        });

        ResponseEntity<Set<PurchaseHistoryDtoResponse>> response = underTest.findByUserId(userId);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testFindByUserIdThrowsExceptionWithInvalidUserId() {
        Long userId = 1L;
        given(userRepository.findById(userId))
                .willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> underTest.findByUserId(userId));
        String expectedMessage = "User not found with id: '1'";
        String actualMessage = exception.getApiResponse().getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testFindByUserIdThrowsExceptionWithNoPurchases() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(purchaseHistoryRepository.findByUser(user)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            underTest.findByUserId(userId);
        });
        String expectedMessage = "Purchase not found with User: '1'";
        String actualMessage = exception.getApiResponse().getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testBuyProductSuccess() {
        //given
        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);
        product.setPrice(100.0);
        product.setQuantity(1L);

        Organization organization = new Organization();

        User owner = new User();
        owner.setBalance(0.0);

        organization.setUser(owner);
        product.setOrganization(organization);

        User user = new User();
        user.setBalance(200.0);

        PurchaseHistory purchaseHistory = new PurchaseHistory();

        PurchaseHistoryDtoResponse expectedResponse = new PurchaseHistoryDtoResponse();
        expectedResponse.setProduct(new ProductDtoResponseIdAndTitle(productId, null));
        expectedResponse.setUser(new UserDtoResponseIdAndUsername(null, null));
        expectedResponse.setDate(new Date());


        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));

        given(purchaseHistoryRepository.save(any(PurchaseHistory.class)))
                .willReturn(purchaseHistory);

        given(mapper.map(purchaseHistory, PurchaseHistoryDtoResponse.class))
                .willReturn(expectedResponse);

        //when
        ResponseEntity<PurchaseHistoryDtoResponse> response = underTest.buyProduct(productId, user);
        //then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        PurchaseHistoryDtoResponse purchaseHistoryDtoResponse = response.getBody();

        assertNotNull(purchaseHistoryDtoResponse);
        assertEquals(user.getId(), purchaseHistoryDtoResponse.getUser().getId());
        assertEquals(product.getId(), purchaseHistoryDtoResponse.getProduct().getId());
        assertNotNull(purchaseHistoryDtoResponse.getDate());

        verify(userRepository).save(owner);
        verify(userRepository).save(user);
    }


    @Test
    public void testBuyProduct_WhenProductNotFound_ThenThrowsException() {
        //given
        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);
        product.setPrice(100.0);
        product.setQuantity(1L);

        Organization organization = new Organization();

        User owner = new User();
        owner.setBalance(0.0);

        organization.setUser(owner);
        product.setOrganization(organization);

        User user = new User();
        user.setBalance(200.0);

        PurchaseHistoryDtoResponse expectedResponse = new PurchaseHistoryDtoResponse();
        expectedResponse.setProduct(new ProductDtoResponseIdAndTitle(productId, null));
        expectedResponse.setUser(new UserDtoResponseIdAndUsername(null, null));
        expectedResponse.setDate(new Date());


        given(productRepository.findById(productId))
                .willReturn(Optional.empty());

        //when
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> underTest.buyProduct(productId, user));
        //then
        assertEquals("Product not found with id: '1'",e.getApiResponse().getMessage());
        verify(userRepository,never()).save(any());
        verify(purchaseHistoryRepository,never()).save(any());
    }

    @Test
    public void testBuyProductNotEnoughBalance() {
        //given
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setPrice(100.0);
        product.setQuantity(1L);

        User user = new User();
        user.setBalance(50.0);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));
        //when
        CanNotPerformActionException e = assertThrows(CanNotPerformActionException.class, () -> underTest.buyProduct(productId, user));
        //then
        assertEquals("User doesn't have enough money", e.getApiResponse().getMessage());
        verify(userRepository,never()).save(any());
        verify(purchaseHistoryRepository,never()).save(any());
    }

    @Test
    public void testBuyProductNotAvailable() {
        //given
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setPrice(100.0);
        product.setQuantity(0L);

        User user = new User();
        user.setBalance(200.0);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));

        //when
        CanNotPerformActionException e = assertThrows(CanNotPerformActionException.class, () -> underTest.buyProduct(productId, user));
        //then
        assertEquals("Product has been sold",e.getApiResponse().getMessage());
        verify(userRepository,never()).save(any());
        verify(purchaseHistoryRepository,never()).save(any());
    }

    @Test
    public void testRefundProduct_Successful() {
        //given
        Long productId = 1L;

        User user = new User();
        User owner = new User();
        owner.setBalance(100.0);
        user.setBalance(100.0);

        Product product = new Product();
        Organization organization = new Organization();
        PurchaseHistory purchase = new PurchaseHistory();

        purchase.setId(1L);
        purchase.setProduct(product);
        purchase.setDate(new Date());
        organization.setUser(owner);
        product.setOrganization(organization);
        product.setPrice(100.0);
        product.setQuantity(0L);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));
        given(purchaseHistoryRepository.findByUser(user))
                .willReturn(Optional.of(Collections.singleton(purchase)));
        given(userRepository.save(any(User.class)))
                .willReturn(user);
        given(userRepository.save(owner))
                .willReturn(owner);

        //when
        ResponseEntity<ApiResponse> response = underTest.refundProduct(productId, user);

        //then
        verify(userRepository, times(2)).save(any(User.class));
        verify(purchaseHistoryRepository, times(1)).removeById(anyLong());
        assertEquals(200.0, user.getBalance());
        assertEquals(5.0, owner.getBalance());
        assertEquals(1, product.getQuantity());
        assertTrue(response.getBody().getSuccess());
        assertEquals("You have successfully refund the product with id: " + productId,response.getBody().getMessage());
    }

    @Test
    public void testRefundProduct_ThrowsResourceNotFoundException() {
        //given
        Long productId = 1L;
        User user = new User();
        given(productRepository.findById(productId))
                .willReturn(Optional.empty());
        //when
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> underTest.refundProduct(productId, user));
        //then
        assertEquals("Product not found with id: '1'",e.getApiResponse().getMessage());
        verify(userRepository,never()).save(any());
        verify(purchaseHistoryRepository,never()).removeById(any());
    }

    @Test
    public void testRefundProduct_ThrowsResourceNotFoundException_UserHasNotPurchased() {
        //given
        Long productId = 1L;
        User user = new User();
        user.setId(1L);
        Product product = new Product();

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));
        given(purchaseHistoryRepository.findByUser(user))
                .willReturn(Optional.empty());

        //when
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> underTest.refundProduct(productId, user));


        //then
        assertEquals("Purchase not found with User: '1'",e.getApiResponse().getMessage());
        verify(userRepository,never()).save(any());
        verify(purchaseHistoryRepository,never()).removeById(any());
    }


    @Test
    public void testRefundProduct_ThrowsCanNotPerformActionException_DateExpired() {
        //given
        Long productId = 1L;

        User user = new User();
        Product product = new Product();
        PurchaseHistory purchase = new PurchaseHistory();

        purchase.setProduct(product);
        purchase.setDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2)));

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));
        given(purchaseHistoryRepository.findByUser(user))
                .willReturn(Optional.of(Collections.singleton(purchase)));

        //when
        CanNotPerformActionException e = assertThrows(CanNotPerformActionException.class, () -> underTest.refundProduct(productId, user));
        //then
        assertEquals("Product refund date has expired",e.getApiResponse().getMessage());
        verify(userRepository,never()).save(any());
        verify(purchaseHistoryRepository,never()).removeById(any());
    }


}