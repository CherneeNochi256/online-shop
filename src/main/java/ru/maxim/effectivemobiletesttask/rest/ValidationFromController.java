package ru.maxim.effectivemobiletesttask.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.validation_form.ValidationFormRequest;
import ru.maxim.effectivemobiletesttask.dto.validation_form.ValidationFormResponse;
import ru.maxim.effectivemobiletesttask.service.ValidationFormService;

import java.util.List;

@RestController
@RequestMapping("api/v1/validation-forms")
@RequiredArgsConstructor
public class ValidationFromController {

    private final ValidationFormService validationFormService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ValidationFormResponse>> getAllValidationForm(@RequestParam(required = false, defaultValue = "10") Integer size,
                                                                             @RequestParam(required = false, defaultValue = "0") Integer page) {
        return validationFormService.getAll(PageRequest.of(page,size));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("{id}")
    public ResponseEntity<ValidationFormResponse> validateOrganization(@RequestBody ValidationFormRequest validationDto,
                                                                       @PathVariable Long id) {
        return validationFormService.validateOrganization(id, validationDto);
    }


}
