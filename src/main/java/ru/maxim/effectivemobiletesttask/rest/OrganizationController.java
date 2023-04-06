package ru.maxim.effectivemobiletesttask.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.OrganizationService;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

@RestController
@RequestMapping("api/main/organization")
public class OrganizationController {
    private final OrganizationService organizationService;


    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("freeze/{id}")
    public void freezeOrganization(@PathVariable("id") Long organizationId) {
        Organization organization = RestPreconditions.checkOrganization(organizationService.organizationById(organizationId));

        organizationService.freezeOrganization(organization);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public void deleteOrganization(@PathVariable("id") Long organizationId) {
        Organization organization = RestPreconditions.checkOrganization(organizationService.organizationById(organizationId));

        organizationService.deleteOrganization(organization);

    }


    @PostMapping
    public void createOrganization(@AuthenticationPrincipal User user,
                                   @RequestBody Organization organization) {

        RestPreconditions.checkOrganization(organization);

        organizationService.createOrganizationByUser(user, organization);

    }
}
