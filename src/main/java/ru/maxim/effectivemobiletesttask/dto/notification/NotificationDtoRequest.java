package ru.maxim.effectivemobiletesttask.dto.notification;

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
    private String header;
    private String text;
}