package ru.maxim.effectivemobiletesttask.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.AdminService;
import ru.maxim.effectivemobiletesttask.service.OrganizationService;
import ru.maxim.effectivemobiletesttask.service.UserService;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

@RestController
@RequestMapping("api/main/organization")
public class OrganizationController {
    private final OrganizationService organizationService;
    private final AdminService adminService;
    private final UserService userService;

    public OrganizationController(OrganizationService organizationService, AdminService adminService, UserService userService) {
        this.organizationService = organizationService;
        this.adminService = adminService;
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("freeze/{id}")
    public void freezeOrganization(@PathVariable("id") Long organizationId) {
        Organization organization = RestPreconditions.checkOrganization(organizationService.organizationById(organizationId));

        adminService.freezeOrganization(organization);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public void deleteOrganization(@PathVariable("id") Long organizationId) {
        Organization organization = RestPreconditions.checkOrganization(organizationService.organizationById(organizationId));

        adminService.deleteOrganization(organization);

    }


    @PostMapping
    public void createOrganization(@AuthenticationPrincipal User user,
                                   @RequestBody Organization organization) {

        RestPreconditions.checkOrganization(organization);

        userService.createOrganizationByUser(user, organization);

    }
}
