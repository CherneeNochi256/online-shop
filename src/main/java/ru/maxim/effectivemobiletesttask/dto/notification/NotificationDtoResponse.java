package ru.maxim.effectivemobiletesttask.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maxim.effectivemobiletesttask.dto.user.UserDtoResponseIdAndUsername;
import ru.maxim.effectivemobiletesttask.entity.Notification;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link Notification} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDtoResponse implements Serializable {
    private Long id;
    private Date dateOfCreation;
    private String header;
    private String text;
    private UserDtoResponseIdAndUsername user;
}