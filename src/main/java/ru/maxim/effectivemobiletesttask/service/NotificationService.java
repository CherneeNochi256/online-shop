package ru.maxim.effectivemobiletesttask.service;

import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.entity.Notification;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.repository.NotificationRepository;
import ru.maxim.effectivemobiletesttask.repository.PurchaseHistoryRepository;

import java.util.Date;
import java.util.Set;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Set<Notification> findByUser(User user){
        return notificationRepository.findByUser(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void notifyUser(User user, Notification notification) {

        Notification resultNotification = new Notification();

        BeanUtils.copyProperties(notification, resultNotification, "id", "user");

        resultNotification.setDateOfCreation(new Date());
        resultNotification.setUser(user);

        notificationRepository.save(resultNotification);

    }
}
