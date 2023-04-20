package ru.maxim.effectivemobiletesttask.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.grade.GradeDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.grade.GradeDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.GradeService;

@RestController
@RequestMapping("api/main/grade")
@RequiredArgsConstructor
public class GradeController {
       private final GradeService gradeService;


    @PostMapping("{userId}")
    public ResponseEntity<GradeDtoResponse> estimate(@PathVariable Long userId,
                                                     @AuthenticationPrincipal User user,
                                                     @RequestBody @Valid GradeDtoRequest gradeDto) {
       return gradeService.estimateProduct(userId, user, gradeDto);
    }
}
