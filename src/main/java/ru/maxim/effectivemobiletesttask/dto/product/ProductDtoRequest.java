package ru.maxim.effectivemobiletesttask.dto.product;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maxim.effectivemobiletesttask.entity.Product;

import java.io.Serializable;

/**
 * A DTO for the {@link Product} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDtoRequest implements Serializable {
    private String title;
    private Double price;
    private Long quantity;
    @NotBlank
    private String organizationName;
}