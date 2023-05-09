package ru.maxim.effectivemobiletesttask.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.dto.validation_form.ValidationFormRequest;
import ru.maxim.effectivemobiletesttask.dto.validation_form.ValidationFormResponse;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.ValidationForm;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.OrganizationsRepository;
import ru.maxim.effectivemobiletesttask.repository.ValidationFormRepository;

import java.util.List;
import java.util.Set;

import static ru.maxim.effectivemobiletesttask.utils.AppConstants.ID;
import static ru.maxim.effectivemobiletesttask.utils.AppConstants.VALIDATION_FORM;

@Service
@RequiredArgsConstructor
public class ValidationFormService {

    private final ValidationFormRepository validationFormRepository;
    private final ModelMapper mapper;
    private final OrganizationsRepository organizationsRepository;

    private static final String NO_VALIDATION_FORM_FOUND = "No validation form found";


    public ResponseEntity<List<ValidationFormResponse>> getAll(PageRequest pageRequest) {
        Set<ValidationForm> forms = validationFormRepository.findAllCustom(pageRequest)
                .orElseThrow(() -> new ResourceNotFoundException(NO_VALIDATION_FORM_FOUND));

        List<ValidationFormResponse> response = forms.stream()
                .map(f -> mapper.map(f, ValidationFormResponse.class))
                .toList();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ValidationFormResponse> validateOrganization(Long validationFormId, ValidationFormRequest validationDto) {

        ValidationForm validationForm = validationFormRepository.findById(validationFormId)
                .orElseThrow(() -> new ResourceNotFoundException(VALIDATION_FORM, ID, validationFormId));

        validationForm.setApproved(validationDto.getApproved());
        validationForm.setMessage(validationDto.getMessage());

        Organization organization = validationForm.getOrganization();
        organization.setStatus("ACTIVE");

        organizationsRepository.save(organization);
        validationFormRepository.save(validationForm);

        ValidationFormResponse response = mapper.map(validationForm, ValidationFormResponse.class);


        return ResponseEntity.ok(response);
    }
}
