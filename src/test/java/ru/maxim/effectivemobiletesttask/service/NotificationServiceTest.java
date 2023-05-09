package ru.maxim.effectivemobiletesttask.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import ru.maxim.effectivemobiletesttask.dto.notification.NotificationDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.notification.NotificationDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.Notification;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.NotificationRepository;
import ru.maxim.effectivemobiletesttask.repository.UserRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    private NotificationService underTest;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper mapper;


    private User user;
    private Notification notification;
    private Set<Notification> notifications;
    private NotificationDtoRequest notificationDtoRequest;
    private Long userId;

    @BeforeEach
    void setUp() {

        userId = 1L;

        user = User.builder()
                .id(userId)
                .username("username")
                .build();

        notification = Notification.builder()
                .text("text")
                .header("header")
                .build();

        notificationDtoRequest = new NotificationDtoRequest("header", "text");

        notifications = new HashSet<>();
        notifications.add(notification);
    }

    @Test
    void findByUser() {
        //given

        Set<NotificationDtoResponse> expectedResponse = notifications.stream()
                .map(n -> mapper.map(n, NotificationDtoResponse.class))
                .collect(Collectors.toSet());


        given(notificationRepository.findByUser(user))
                .willReturn(Optional.of(notifications));

        notifications.forEach(n -> {
            NotificationDtoResponse notificationDtoResponse = mapper.map(n, NotificationDtoResponse.class);
            given(mapper.map(n, NotificationDtoResponse.class))
                    .willReturn(notificationDtoResponse);
        });

        //when
        ResponseEntity<Set<NotificationDtoResponse>> response = underTest.findByUser(user);
        //then
        assertEquals(response.getBody(), expectedResponse);
    }

    @Test
    void notFindByUser_ThenThrowsException() {
        //given

        given(notificationRepository.findByUser(user))
                .willThrow(ResourceNotFoundException.class);
        //when and then
        assertThrows(ResourceNotFoundException.class, () -> underTest.findByUser(user));
    }

    @Test
    void notifyUser() {

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        given(mapper.map(notificationDtoRequest, Notification.class))
                .willReturn(notification);


        //when
        underTest.notifyUser(userId, notificationDtoRequest);
        //then
        verify(notificationRepository).save(notification);
        assertEquals(notification.getUser(), user);
        assertTrue(notification.getDateOfCreation().before(new Date()));
    }

    @Test
    void notNotifyUser_WhenUserDoesNotFound_ThenThrowsException() {

        given(userRepository.findById(userId))
                .willThrow(ResourceNotFoundException.class);

        //when
        assertThrows(ResourceNotFoundException.class, () -> underTest.notifyUser(userId, notificationDtoRequest));
        //then
        verify(notificationRepository, never()).save(notification);
    }
}