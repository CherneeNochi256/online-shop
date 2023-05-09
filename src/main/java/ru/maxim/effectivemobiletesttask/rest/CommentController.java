package ru.maxim.effectivemobiletesttask.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.comment.CommentDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.comment.CommentDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.CommentService;

@RestController
@RequestMapping("api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping
    public ResponseEntity<CommentDtoResponse> createComment(@RequestParam Long productId,
                                                            @AuthenticationPrincipal User user,
                                                            @RequestBody @Valid CommentDtoRequest commentDto) {
        return commentService.commentProduct(productId, user, commentDto);
    }


    @GetMapping("{id}")
    public ResponseEntity<CommentDtoResponse> getComment(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }
}
