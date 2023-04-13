package ru.maxim.effectivemobiletesttask.rest;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.notification.NotificationDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.notification.NotificationDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.Notification;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.NotificationService;
import ru.maxim.effectivemobiletesttask.service.UserService;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/main/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final UserService userService;
    private final NotificationService notificationService;
    private final ModelMapper mapper;


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("{id}")
    public void notifyUser(@PathVariable("id") Long id,
                           @RequestBody NotificationDtoRequest notificationDto) {
        RestPreconditions.checkNotNull(notificationDto);
        User user = RestPreconditions.checkUser(userService.userById(id));
        Notification notification = mapper.map(notificationDto,Notification.class);
        notificationService.notifyUser(user, notification);
    }


    @GetMapping
    public Set<NotificationDtoResponse> getNotifications(@AuthenticationPrincipal User user) {
        Set<Notification> notifications = RestPreconditions.checkNotifications(notificationService.findByUser(user));
        return notifications.stream()
                .map(n-> mapper.map(n,NotificationDtoResponse.class))
                .collect(Collectors.toSet());
    }
}
