package ru.maxim.effectivemobiletesttask.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maxim.effectivemobiletesttask.entity.User;

import java.io.Serializable;

/**
 * A DTO for the {@link User} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoResponse implements Serializable {
    private Long id;
    private String username;
    private String email;
    private Double balance;
}