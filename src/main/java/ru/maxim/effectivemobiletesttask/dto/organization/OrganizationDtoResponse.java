package ru.maxim.effectivemobiletesttask.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoResponseIdAndTitle;
import ru.maxim.effectivemobiletesttask.dto.user.UserDtoResponseIdAndUsername;
import ru.maxim.effectivemobiletesttask.entity.Organization;

import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link Organization} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDtoResponse implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Set<ProductDtoResponseIdAndTitle> products;
    private UserDtoResponseIdAndUsername user;
    private String status;
}