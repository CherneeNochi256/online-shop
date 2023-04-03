package ru.maxim.effectivemobiletesttask.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.Notification;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.AdminService;
import ru.maxim.effectivemobiletesttask.service.NotificationService;
import ru.maxim.effectivemobiletesttask.service.UserService;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

import java.util.Set;

@RestController
@RequestMapping("api/main/notification")
public class NotificationController {
    private final UserService userService;
    private final AdminService adminService;
    private final NotificationService notificationService;

    public NotificationController(UserService userService, AdminService adminService, NotificationService notificationService) {
        this.userService = userService;
        this.adminService = adminService;
        this.notificationService = notificationService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("{id}")
    public void notifyUser(@PathVariable("id") Long id,
                           @RequestBody Notification notification) {
        User user = RestPreconditions.checkUser(userService.userById(id));
        adminService.notifyUser(user, notification);
    }


    @GetMapping
    public Set<Notification> getNotifications(@AuthenticationPrincipal User user) {
        return RestPreconditions.checkNotifications(notificationService.findByUser(user));
    }
}
