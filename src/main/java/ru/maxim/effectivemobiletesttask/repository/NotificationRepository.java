package ru.maxim.effectivemobiletesttask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maxim.effectivemobiletesttask.entity.Notification;
import ru.maxim.effectivemobiletesttask.entity.User;

import java.util.Set;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Set<Notification> findByUser(User user);
}