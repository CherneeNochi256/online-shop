package ru.maxim.effectivemobiletesttask.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.notification.NotificationDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.notification.NotificationDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.NotificationService;

import java.util.Set;

@RestController
@RequestMapping("api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<NotificationDtoResponse> notifyUser(@RequestParam Long userId,
                                                              @RequestBody @Valid NotificationDtoRequest notificationDto) {
        return notificationService.notifyUser(userId, notificationDto);
    }


    @GetMapping
    public ResponseEntity<Set<NotificationDtoResponse>> getNotifications(@AuthenticationPrincipal User user) {
        return notificationService.findByUser(user);
    }
}
