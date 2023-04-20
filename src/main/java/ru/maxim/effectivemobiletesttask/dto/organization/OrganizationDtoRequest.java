package ru.maxim.effectivemobiletesttask.dto.organization;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.maxim.effectivemobiletesttask.entity.Organization} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDtoRequest implements Serializable {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}