package ru.maxim.effectivemobiletesttask.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maxim.effectivemobiletesttask.dto.user.UserDtoResponse;
import ru.maxim.effectivemobiletesttask.dto.user.UserDtoResponseIdAndUsername;
import ru.maxim.effectivemobiletesttask.entity.Comment;

import java.io.Serializable;

/**
 * A DTO for the {@link Comment} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoResponseIdMessageUser implements Serializable {
    private Long id;
    private String message;
    private UserDtoResponseIdAndUsername user;
}