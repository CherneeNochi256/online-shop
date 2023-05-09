package ru.maxim.effectivemobiletesttask.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.ApiResponse;
import ru.maxim.effectivemobiletesttask.dto.organization.OrganizationDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.organization.OrganizationDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.OrganizationService;

@RestController
@RequestMapping("api/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;


    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}/frozen")
    public ResponseEntity<ApiResponse> freezeOrganization(@PathVariable("id") Long organizationId) {
        return organizationService.freezeOrganization(organizationId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> deleteOrganization(@PathVariable("id") Long organizationId) {
        return organizationService.deleteOrganization(organizationId);
    }


    @PostMapping
    public ResponseEntity<OrganizationDtoResponse> createOrganization(@AuthenticationPrincipal User user,
                                                                      @RequestBody @Valid OrganizationDtoRequest organizationDto) {
        return organizationService.createOrganizationByUser(user, organizationDto);
    }
}
