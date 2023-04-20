package ru.maxim.effectivemobiletesttask.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.dto.ApiResponse;
import ru.maxim.effectivemobiletesttask.dto.organization.OrganizationDtoRequest;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.Role;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.OrganizationsRepository;
import ru.maxim.effectivemobiletesttask.repository.UserRepository;

import static ru.maxim.effectivemobiletesttask.utils.AppConstants.*;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationsRepository organizationsRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;


    public Organization organizationById(Long id){
        return organizationsRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(ORGANIZATION,ID,id));
    }
    public Organization organizationByName(String name){
        return organizationsRepository.findByName(name)
                .orElseThrow(()->new ResourceNotFoundException(ORGANIZATION,NAME,name));
    }
    public ResponseEntity<Organization> createOrganizationByUser(User user, OrganizationDtoRequest organizationDto) {

        Organization organization = mapper.map(organizationDto,Organization.class);

        user.getRoles().add(Role.ORG_OWNER);
        organization.setUser(user);
        organization.setStatus("ACTIVE");

        userRepository.save(user);
        Organization savedOrganization = organizationsRepository.save(organization);

        return new ResponseEntity<>(savedOrganization, HttpStatus.CREATED);
    }


    public ResponseEntity<ApiResponse> freezeOrganization(Long organizationId){
        Organization organization = organizationsRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException(ORGANIZATION, ID, organizationId));

        organization.setStatus("FROZEN");

        Organization savedOrganization = organizationsRepository.save(organization);

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
