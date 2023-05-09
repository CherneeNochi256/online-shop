package ru.maxim.effectivemobiletesttask.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import ru.maxim.effectivemobiletesttask.dto.ApiResponse;
import ru.maxim.effectivemobiletesttask.dto.organization.OrganizationDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.organization.OrganizationDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.Role;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.entity.ValidationForm;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.OrganizationsRepository;
import ru.maxim.effectivemobiletesttask.repository.UserRepository;
import ru.maxim.effectivemobiletesttask.repository.ValidationFormRepository;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {
    @InjectMocks
    private OrganizationService underTest;
    @Mock
    private OrganizationsRepository organizationsRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper mapper;
    @Mock
    private ValidationFormRepository validationFormRepository;


    private Long organizationId;
    private Organization organization;
    private User user;

    @BeforeEach
    void setUp() {
        organizationId = 1L;

        organization = Organization.builder()
                .id(organizationId)
                .build();

        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.USER);

        user = User.builder()
                .username("username")
                 .roles(roles)
                .build();
    }


    @Test
    void organizationById() {
        //given

        OrganizationDtoResponse expectedResponse = mapper.map(organization, OrganizationDtoResponse.class);

        given(organizationsRepository.findById(organizationId))
                .willReturn(Optional.of(organization));

        given(mapper.map(organization, OrganizationDtoResponse.class))
                .willReturn(expectedResponse);
        //when
        ResponseEntity<OrganizationDtoResponse> response = underTest.organizationById(organizationId);
        //then
        verify(organizationsRepository).findById(organizationId);
        assertEquals(response.getBody(), expectedResponse);

    }

    @Test
    void NotOrganizationById_WhenOrganizationIsNotFound_ThenThrowsException() {
        //given
        Long organizationId = 1L;

        given(organizationsRepository.findById(organizationId))
                .willThrow(ResourceNotFoundException.class);
        //when&then
        assertThrows(ResourceNotFoundException.class, () -> underTest.organizationById(organizationId));
    }

    @Test
    void createOrganizationByUser() {

        ArgumentCaptor<ValidationForm> validationFormArgumentCaptor = ArgumentCaptor.forClass(ValidationForm.class);

        OrganizationDtoRequest organizationDtoRequest = new OrganizationDtoRequest("orgName", "orgDescription");

        OrganizationDtoResponse expectedResponse = mapper.map(organization, OrganizationDtoResponse.class);

        given(mapper.map(organizationDtoRequest,Organization.class))
                .willReturn(organization);

        given(mapper.map(organization,OrganizationDtoResponse.class))
                .willReturn(expectedResponse);

        //when
        ResponseEntity<OrganizationDtoResponse> response = underTest.createOrganizationByUser(user, organizationDtoRequest);
        //then
        verify(organizationsRepository).save(organization);
        verify(validationFormRepository).save(validationFormArgumentCaptor.capture());
        verify(userRepository).save(user);
        assertTrue(user.getRoles().contains(Role.ORG_OWNER));
        assertEquals(organization.getUser(),user);
        assertNotEquals(organization.getStatus(),"ACTIVE");
        assertEquals(validationFormArgumentCaptor.getValue().getOrganization(),organization);
        assertEquals(expectedResponse,response.getBody());
    }

    @Test
    void freezeOrganization() {
        given(organizationsRepository.findById(organizationId))
                .willReturn(Optional.ofNullable(organization));
        //when
        ResponseEntity<ApiResponse> response = underTest.freezeOrganization(organizationId);
        //then
        verify(organizationsRepository).save(organization);
        assertEquals(organization.getStatus(),"FROZEN");
        assertEquals(response.getBody().getMessage(),"You have successfully frozen the organization with id: " + organizationId);
        assertEquals(response.getBody().getSuccess(),Boolean.TRUE);
    }

    @Test
    void notFreezeOrganization_WhenOrganizationIsNotFound_ThenThrowsException() {
        given(organizationsRepository.findById(organizationId))
                .willThrow(ResourceNotFoundException.class);
        //when
        assertThrows(ResourceNotFoundException.class,()->underTest.freezeOrganization(organizationId));
        //then
        verify(organizationsRepository,never()).save(organization);
    }

    @Test
    void deleteOrganization() {
        given(organizationsRepository.findById(organizationId))
                .willReturn(Optional.ofNullable(organization));
        //when
        ResponseEntity<ApiResponse> response = underTest.deleteOrganization(organizationId);
        //then
        verify(organizationsRepository).save(organization);
        assertEquals(organization.getStatus(),"DELETED");
        assertEquals(response.getBody().getMessage(),"You have successfully deleted the organization with id: " + organizationId);
        assertEquals(response.getBody().getSuccess(),Boolean.TRUE);
    }


    @Test
    void notDeleteOrganization_WhenOrganizationNotFound_ThenThrowsException() {
        given(organizationsRepository.findById(organizationId))
                .willThrow(ResourceNotFoundException.class);
        //when
        assertThrows(ResourceNotFoundException.class,()->underTest.deleteOrganization(organizationId));
        //then
        verify(organizationsRepository,never()).save(organization);
    }
}