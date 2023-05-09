package ru.maxim.effectivemobiletesttask.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoResponse;
import ru.maxim.effectivemobiletesttask.dto.product.ProductUpdateRequest;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.exception.CanNotPerformActionException;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.OrganizationsRepository;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService underTest;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrganizationsRepository organizationsRepository;
    @Mock
    private ModelMapper mapper;


    private Long productId;
    private Product product;
    private User user;
    private Organization organization;

    @BeforeEach
    void setUp() {
        productId = 1L;


        organization = Organization.builder()
                .name("organizationName")
                .status("ACTIVE")
                .build();

        product = Product.builder()
                .title("product")
                .organization(organization)
                .build();

        user = User.builder()
                .username("username")
                .organizations(List.of(organization))
                .build();

        organization.setUser(user);
    }


    @Test
    void productById() {
        //given

        ProductDtoResponse productDtoResponse = mapper.map(product, ProductDtoResponse.class);

        given(productRepository.findById(productId))
                .willReturn(Optional.ofNullable(product));

        given(mapper.map(product, ProductDtoResponse.class))
                .willReturn(productDtoResponse);

        //when
        ResponseEntity<ProductDtoResponse> response = underTest.productById(productId);

        //then
        verify(productRepository).findById(productId);
        assertEquals(response.getBody(), productDtoResponse);
    }

    @Test
    void notProductById_WhenProductNotFound_ThenThrowsException() {
        //given
        given(productRepository.findById(productId))
                .willReturn(Optional.ofNullable(product));
        //when
        assertThrows(ResourceNotFoundException.class, () -> underTest.productById(productId));

        //then
        verify(productRepository, never()).findById(productId);
    }

    @Test
    void createProductByAdmin() {
        ProductDtoResponse expectedResponse = mapper.map(product, ProductDtoResponse.class);

        ProductDtoRequest productDtoRequest = new ProductDtoRequest();

        given(mapper.map(productDtoRequest, Product.class))
                .willReturn(product);

        given(mapper.map(product, ProductDtoResponse.class))
                .willReturn(expectedResponse);
        //when
        ResponseEntity<ProductDtoResponse> response = underTest.createProductByAdmin(productDtoRequest);
        //then
        verify(productRepository).save(product);
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void createProductByUser() {
        //given
        ProductDtoRequest productDtoRequest = new ProductDtoRequest();

        ProductDtoResponse expectedResponse = mapper.map(product, ProductDtoResponse.class);

        given(organizationsRepository.findByName(any()))
                .willReturn(Optional.ofNullable(organization));

        given(mapper.map(productDtoRequest, Product.class))
                .willReturn(product);

        given(mapper.map(product, ProductDtoResponse.class))
                .willReturn(expectedResponse);
        //when
        ResponseEntity<ProductDtoResponse> response = underTest.createProductByUser(productDtoRequest, user);
        //then
        verify(productRepository).save(product);
        assertEquals(product.getOrganization(), organization);
        assertEquals(response.getBody(), expectedResponse);
    }

    @Test
    void notCreateProductByUser_WhenOrganizationIsNotFound_ThenThrowsException() {
        //given
        ProductDtoRequest productDtoRequest = new ProductDtoRequest();

        given(organizationsRepository.findByName(any()))
                .willThrow(ResourceNotFoundException.class);
        //when
        assertThrows(ResourceNotFoundException.class, () -> underTest.createProductByUser(productDtoRequest, user));
        //then
        verify(productRepository, never()).save(product);
    }

    @Test
    void notCreateProductByUser_WhenUserIsNotTheOwnerOfTheOrganization_ThenThrowsException() {
        //given
        ProductDtoRequest productDtoRequest = new ProductDtoRequest();

        Organization otherOrganization = new Organization();
        otherOrganization.setUser(new User());

        given(organizationsRepository.findByName(any()))
                .willReturn(Optional.of(otherOrganization));

        given(mapper.map(productDtoRequest, Product.class))
                .willReturn(product);

        //when
        assertThrows(CanNotPerformActionException.class, () -> underTest.createProductByUser(productDtoRequest, user));
        //then
        verify(productRepository, never()).save(product);
    }


    @Test
    void notCreateProductByUser_WhenOrganizationIsFrozen_ThenThrowsException() {
        //given
        ProductDtoRequest productDtoRequest = new ProductDtoRequest();

        organization.setStatus("FROZEN");

        given(organizationsRepository.findByName(any()))
                .willReturn(Optional.ofNullable(organization));

        given(mapper.map(productDtoRequest, Product.class))
                .willReturn(product);

        //when
        assertThrows(CanNotPerformActionException.class, () -> underTest.createProductByUser(productDtoRequest, user));
        //then
        verify(productRepository, never()).save(product);
    }

    @Test
    void updateProductByAdmin() {
        //given
        ProductDtoRequest productDtoRequest = new ProductDtoRequest();
        productDtoRequest.setQuantity(100L);

        String productTitle = product.getTitle();

        ProductDtoResponse expectedResponse = mapper.map(productDtoRequest, ProductDtoResponse.class);

        given(productRepository.findById(productId))
                .willReturn(Optional.ofNullable(product));

        given(mapper.map(any(), eq(ProductDtoResponse.class)))
                .willReturn(expectedResponse);


        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);

        //when
        ResponseEntity<ProductDtoResponse> response = underTest.updateProductByAdmin(productDtoRequest, productId);
        //then
        verify(productRepository).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getQuantity(), productDtoRequest.getQuantity());
        assertEquals(response.getBody(), expectedResponse);
        assertEquals(productTitle, product.getTitle());//check that method doesn't copy null parameters
    }

    @Test
    void notUpdateProductByAdmin_WhenProductIsNotFound_ThanThrowsException() {
        //given
        ProductDtoRequest productDtoRequest = new ProductDtoRequest();
        productDtoRequest.setQuantity(100L);

        given(productRepository.findById(productId))
                .willThrow(ResourceNotFoundException.class);

        //when
        assertThrows(ResourceNotFoundException.class, () -> underTest.updateProductByAdmin(productDtoRequest, productId));
        //then
        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProductByUser() {
        //given
        ProductUpdateRequest productDtoRequest = new ProductUpdateRequest();
        productDtoRequest.setQuantity(100L);

        String productTitle = product.getTitle();

        ProductDtoResponse expectedResponse = mapper.map(productDtoRequest, ProductDtoResponse.class);

        given(productRepository.findById(productId))
                .willReturn(Optional.ofNullable(product));

        given(mapper.map(any(), eq(ProductDtoResponse.class)))
                .willReturn(expectedResponse);


        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);

        //when
        ResponseEntity<ProductDtoResponse> response = underTest.updateProductByUser(productDtoRequest, productId, user);
        //then
        verify(productRepository).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getQuantity(), productDtoRequest.getQuantity());
        assertEquals(response.getBody(), expectedResponse);
        assertEquals(productTitle, product.getTitle());//check that method doesn't copy null parameters

    }

    @Test
    void notUpdateProductByUser_WhenProductIsNotFound_ThanThrowsException() {
        //given
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest();
        productUpdateRequest.setQuantity(100L);

        given(productRepository.findById(productId))
                .willThrow(ResourceNotFoundException.class);

        //when
        assertThrows(ResourceNotFoundException.class, () -> underTest.updateProductByUser(productUpdateRequest, productId, user));
        //then
        verify(productRepository, never()).save(any());
    }

    @Test
    void notUpdateProductByUser_WhenUserIsNotProductOwner_ThenThrowsException() {
        //given
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest();
        productUpdateRequest.setQuantity(100L);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));

        //when
        assertThrows(CanNotPerformActionException.class, () -> underTest.updateProductByUser(productUpdateRequest, productId, new User()));
        //then
        verify(productRepository, never()).save(any());
    }

    @Test
    void findAll() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 4);
        Set<Product> mockProducts = new HashSet<>();
        mockProducts.add(new Product());
        given(productRepository.findAllWhereOrganizationStatusIsActive(pageRequest))
                .willReturn(Optional.of(mockProducts));
        given(mapper.map(any(Product.class), any(Class.class)))
                .willReturn(new ProductDtoResponse());

        // when
        ResponseEntity<Set<ProductDtoResponse>> response = underTest.findAll(pageRequest);

        //then
        verify(productRepository, times(1)).findAllWhereOrganizationStatusIsActive(pageRequest);
        verify(mapper, times(1)).map(any(Product.class), any(Class.class));
        verifyNoMoreInteractions(productRepository);
        verifyNoMoreInteractions(mapper);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void notFindAll_WhenNoProductsFound_ThenThrowsException() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 4);
        given(productRepository.findAllWhereOrganizationStatusIsActive(pageRequest))
                .willThrow(ResourceNotFoundException.class);

        //when&then
        assertThrows(ResourceNotFoundException.class,()-> underTest.findAll(pageRequest));
    }

}