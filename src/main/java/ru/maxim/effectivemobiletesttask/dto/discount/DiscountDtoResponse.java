package ru.maxim.effectivemobiletesttask.dto.discount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoResponseIdAndTitle;

import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link ru.maxim.effectivemobiletesttask.entity.Discount} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDtoResponse implements Serializable {
    private Long id;
    private Set<ProductDtoResponseIdAndTitle> products;
    private Double discount;
    private Long interval;
}