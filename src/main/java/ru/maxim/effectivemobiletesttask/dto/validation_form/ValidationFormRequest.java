package ru.maxim.effectivemobiletesttask.dto.validation_form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maxim.effectivemobiletesttask.dto.organization.OrganizationDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.ValidationForm;

import java.io.Serializable;

/**
 * A DTO for the {@link ValidationForm} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidationFormRequest implements Serializable {
    private String message;
    private Boolean approved;
}