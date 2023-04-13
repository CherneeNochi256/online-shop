package ru.maxim.effectivemobiletesttask.dto.grade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoResponse;
import ru.maxim.effectivemobiletesttask.dto.user.UserDtoResponseIdAndUsername;
import ru.maxim.effectivemobiletesttask.entity.Grade;

import java.io.Serializable;

/**
 * A DTO for the {@link Grade} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeDtoResponse implements Serializable {
    private Long id;
    private Double value;
    private ProductDtoResponse product;
    private UserDtoResponseIdAndUsername user;
}