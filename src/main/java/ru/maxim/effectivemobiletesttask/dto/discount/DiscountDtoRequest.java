package ru.maxim.effectivemobiletesttask.dto.discount;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maxim.effectivemobiletesttask.entity.Discount;

import java.io.Serializable;

/**
 * A DTO for the {@link Discount} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDtoRequest implements Serializable {
    @NotNull
    private Double discount;
    @NotNull
    private Long interval;
}