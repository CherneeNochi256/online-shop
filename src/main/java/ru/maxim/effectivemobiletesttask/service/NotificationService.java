package ru.maxim.effectivemobiletesttask.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.dto.notification.NotificationDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.notification.NotificationDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.Notification;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.NotificationRepository;
import ru.maxim.effectivemobiletesttask.repository.UserRepository;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.maxim.effectivemobiletesttask.utils.AppConstants.*;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;


    public ResponseEntity<Set<NotificationDtoResponse>> findByUser(User user) {
        Set<Notification> notifications = notificationRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(NOTIFICATION, USER, user.getId()));

        Set<NotificationDtoResponse> response = notifications.stream()
                .map(n -> mapper.map(n, NotificationDtoResponse.class))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<NotificationDtoResponse> notifyUser(Long userId, NotificationDtoRequest notificationDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER, ID, userId));

        Notification notification = mapper.map(notificationDto, Notification.class);

        notification.setDateOfCreation(new Date());
        notification.setUser(user);

        notificationRepository.save(notification);

        NotificationDtoResponse response = mapper.map(notification, NotificationDtoResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
}
