package ru.maxim.effectivemobiletesttask.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.NotificationDto;
import ru.maxim.effectivemobiletesttask.entity.Notification;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.NotificationService;
import ru.maxim.effectivemobiletesttask.service.UserService;
import ru.maxim.effectivemobiletesttask.utils.EntityMapper;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/main/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final UserService userService;
    private final NotificationService notificationService;
    private final EntityMapper entityMapper;


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("{id}")
    public void notifyUser(@PathVariable("id") Long id,
                           @RequestBody NotificationDto.Request notificationDto) {
        RestPreconditions.checkNotNull(notificationDto);
        User user = RestPreconditions.checkUser(userService.userById(id));
        Notification notification = entityMapper.notificationDtoToEntity(notificationDto);
        notificationService.notifyUser(user, notification);
    }


    @GetMapping
    public Set<NotificationDto.Response> getNotifications(@AuthenticationPrincipal User user) {
        Set<Notification> notifications = RestPreconditions.checkNotifications(notificationService.findByUser(user));
        return notifications.stream()
                .map(entityMapper::entityToNotificationDto)
                .collect(Collectors.toSet());
    }
}
