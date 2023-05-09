package ru.maxim.effectivemobiletesttask.service;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.maxim.effectivemobiletesttask.dto.validation_form.ValidationFormRequest;
import ru.maxim.effectivemobiletesttask.dto.validation_form.ValidationFormResponse;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.ValidationForm;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.OrganizationsRepository;
import ru.maxim.effectivemobiletesttask.repository.ValidationFormRepository;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static ru.maxim.effectivemobiletesttask.utils.AppConstants.ID;
import static ru.maxim.effectivemobiletesttask.utils.AppConstants.VALIDATION_FORM;

@ExtendWith(MockitoExtension.class)
class ValidationFormServiceTest {
    @Mock
    private ValidationFormRepository validationFormRepository;
    @Mock
    private ModelMapper mapper;
    @Mock
    private OrganizationsRepository organizationsRepository;
    @InjectMocks
    private ValidationFormService underTest;

    @Test
    public void testGetAll() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 4);
        Set<ValidationForm> forms = new HashSet<>();

        ValidationForm form1 = ValidationForm.builder()
                .id(1L)
                .message("form1")
                .build();
        ValidationForm form2 = ValidationForm.builder()
                .id(2L)
                .message("form2")
                .build();
        forms.add(form1);
        forms.add(form2);

        given(validationFormRepository.findAllCustom(pageRequest))
                .willReturn(Optional.of(forms));


        ValidationFormResponse formResponse1 = new ValidationFormResponse();
        ValidationFormResponse formResponse2 = new ValidationFormResponse();

        given(mapper.map(any(), eq(ValidationFormResponse.class)))
                .willReturn(formResponse1,formResponse2);

        List<ValidationFormResponse> expectedResponse = List.of(formResponse1, formResponse2);


        // when
        ResponseEntity<List<ValidationFormResponse>> response = underTest.getAll(pageRequest);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testGetAll_WhenValidationFromIsNotFound_ThenThrowsException() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 4);

        given(validationFormRepository.findAllCustom(pageRequest))
                .willReturn(Optional.empty());
        // when
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> underTest.getAll(pageRequest));

        // then
        assertEquals("No validation form found",e.getApiResponse().getMessage());
    }

    @Test
    public void testValidateOrganizationWithApprovedStatusAndMessage() {
        // given
        Long validationFormId = 1L;
        ValidationFormRequest validationDto = new ValidationFormRequest();
        validationDto.setApproved(true);
        validationDto.setMessage("Validated successfully");

        ValidationForm validationForm = new ValidationForm();
        validationForm.setId(validationFormId);
        validationForm.setApproved(false);
        validationForm.setMessage(null);

        Organization organization = new Organization();
        organization.setStatus("INACTIVE");

        validationForm.setOrganization(organization);

        given(validationFormRepository.findById(validationFormId))
                .willReturn(Optional.of(validationForm));

        ValidationFormResponse expectedResponse = new ValidationFormResponse();
        expectedResponse.setId(validationFormId);
        expectedResponse.setApproved(true);
        expectedResponse.setMessage("Validated successfully");

        given(mapper.map(any(),eq(ValidationFormResponse.class)))
                .willReturn(expectedResponse);
        // when
        ResponseEntity<ValidationFormResponse> responseEntity = underTest.validateOrganization(validationFormId, validationDto);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    public void testValidateOrganizationWithRejectedStatusAndMessage() {
        // given
        Long validationFormId = 1L;
        ValidationFormRequest validationDto = new ValidationFormRequest();
        validationDto.setApproved(false);
        validationDto.setMessage("Not validated");

        ValidationForm validationForm = new ValidationForm();
        validationForm.setId(validationFormId);
        validationForm.setApproved(false);
        validationForm.setMessage(null);

        Organization organization = new Organization();
        organization.setStatus("INACTIVE");

        validationForm.setOrganization(organization);

        given(validationFormRepository.findById(validationFormId))
                .willReturn(Optional.of(validationForm));

        ValidationFormResponse expectedResponse = new ValidationFormResponse();
        expectedResponse.setId(validationFormId);
        expectedResponse.setApproved(false);
        expectedResponse.setMessage("Not validated");

        given(mapper.map(any(),eq(ValidationFormResponse.class)))
                .willReturn(expectedResponse);

        // when
        ResponseEntity<ValidationFormResponse> responseEntity = underTest.validateOrganization(validationFormId, validationDto);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    public void testValidateOrganizationWithInvalidId() {
        // given
        Long validationFormId = 100L;
        ValidationFormRequest validationDto = new ValidationFormRequest();
        validationDto.setApproved(true);
        validationDto.setMessage("Validated successfully");

        given(validationFormRepository.findById(validationFormId))
                .willReturn(Optional.empty());

        // when and then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> underTest.validateOrganization(validationFormId, validationDto));

        assertEquals("Validation form not found with id: '100'", exception.getApiResponse().getMessage());
    }


}