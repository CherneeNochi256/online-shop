package ru.maxim.effectivemobiletesttask.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

import static ru.maxim.effectivemobiletesttask.utils.AppConstants.ID;
import static ru.maxim.effectivemobiletesttask.utils.AppConstants.ORGANIZATION;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationsRepository organizationsRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final ValidationFormRepository validationFormRepository;


    public ResponseEntity<OrganizationDtoResponse> organizationById(Long id) {
        Organization organization = organizationsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ORGANIZATION, ID, id));
        OrganizationDtoResponse response = mapper.map(organization, OrganizationDtoResponse.class);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<OrganizationDtoResponse> createOrganizationByUser(User user, OrganizationDtoRequest organizationDto) {

        Organization organization = mapper.map(organizationDto, Organization.class);

        user.getRoles().add(Role.ORG_OWNER);
        organization.setUser(user);
        organization.setStatus("UNCHECKED");

        ValidationForm form = ValidationForm.builder()
                .organization(organization)
                .build();

        userRepository.save(user);
        organizationsRepository.save(organization);
        validationFormRepository.save(form);

        OrganizationDtoResponse response = mapper.map(organization, OrganizationDtoResponse.class);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    public ResponseEntity<ApiResponse> freezeOrganization(Long organizationId) {
        Organization organization = organizationsRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException(ORGANIZATION, ID, organizationId));

        organization.setStatus("FROZEN");

        organizationsRepository.save(organization);

        return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, "You have successfully frozen the organization with id: " + organizationId));
    }

    public ResponseEntity<ApiResponse> deleteOrganization(Long organizationId) {
        Organization organization = organizationsRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException(ORGANIZATION, ID, organizationId));

        organization.setStatus("DELETED");

        organizationsRepository.save(organization);
        return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, "You have successfully deleted the organization with id: " + organizationId));

    }
}
