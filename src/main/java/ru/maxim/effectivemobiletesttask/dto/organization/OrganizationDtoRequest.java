package ru.maxim.effectivemobiletesttask.dto.organization;

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
    private String name;
    private String description;
}