package ru.maxim.effectivemobiletesttask.dto.notification;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.maxim.effectivemobiletesttask.entity.Notification} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDtoRequest implements Serializable {
    @NotBlank
    private String header;
    @NotBlank
    private String text;
}