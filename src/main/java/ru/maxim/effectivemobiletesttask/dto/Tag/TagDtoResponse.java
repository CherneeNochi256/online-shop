package ru.maxim.effectivemobiletesttask.dto.Tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoResponseIdAndTitle;
import ru.maxim.effectivemobiletesttask.entity.Tag;

import java.io.Serializable;

/**
 * A DTO for the {@link Tag} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDtoResponse implements Serializable {
    private Long id;
    private String tag;
    private ProductDtoResponseIdAndTitle product;
}