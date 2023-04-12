package ru.maxim.effectivemobiletesttask.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.OrganizationDto;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.OrganizationService;
import ru.maxim.effectivemobiletesttask.utils.EntityMapper;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

@RestController
@RequestMapping("api/main/organization")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;
    private final EntityMapper entityMapper;


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
                                   @RequestBody OrganizationDto.Request organizationDto) {
        RestPreconditions.checkNotNull(organizationDto);
        Organization organization = entityMapper.organizationDtoToEntity(organizationDto);
        RestPreconditions.checkOrganization(organization);

        organizationService.createOrganizationByUser(user, organization);

    }
}
