package ru.maxim.effectivemobiletesttask.dto.grade;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.maxim.effectivemobiletesttask.entity.Grade} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeDtoRequest implements Serializable {
    @NotNull
    private Double value;
}