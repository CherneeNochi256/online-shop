package ru.maxim.effectivemobiletesttask.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.maxim.effectivemobiletesttask.entity.Product} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDtoResponseIdAndTitle implements Serializable {
    private Long id;
    private String title;
}