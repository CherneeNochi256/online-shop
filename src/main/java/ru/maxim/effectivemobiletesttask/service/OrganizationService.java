package ru.maxim.effectivemobiletesttask.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.Role;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.repository.OrganizationsRepository;
import ru.maxim.effectivemobiletesttask.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationsRepository organizationsRepository;
    private final UserRepository userRepository;


    public Organization organizationById(Long id){
        return organizationsRepository.findById(id).orElse(null);
    }

    public void createOrganizationByUser(User user, Organization organization) {
        Organization resultOrganization = new Organization();

        BeanUtils.copyProperties(organization, resultOrganization, "id", "status");
        user.getRoles().add(Role.ORG_OWNER);
        resultOrganization.setUser(user);
        resultOrganization.setStatus("ACTIVE");

        userRepository.save(user);

        organizationsRepository.save(resultOrganization);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void freezeOrganization(Organization organization){

        organization.setStatus("FROZEN");
        organizationsRepository.save(organization);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteOrganization(Organization organization) {
        organization.setStatus("DELETED");
    }
}
