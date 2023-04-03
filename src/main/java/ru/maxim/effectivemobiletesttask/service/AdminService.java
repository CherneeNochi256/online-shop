package ru.maxim.effectivemobiletesttask.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.entity.*;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.NotificationRepository;
import ru.maxim.effectivemobiletesttask.repository.OrganizationsRepository;
import ru.maxim.effectivemobiletesttask.repository.PurchaseHistoryRepository;
import ru.maxim.effectivemobiletesttask.repository.UserRepository;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

import java.util.Date;
import java.util.Set;

@Service
public class AdminService {
    private final PurchaseHistoryRepository purchaseHistoryRepository;

    private final UserRepository userRepository;

    private final NotificationRepository notificationRepository;

    private final OrganizationsRepository organizationsRepository;

    public AdminService(PurchaseHistoryRepository purchaseHistoryRepository, UserRepository userRepository, NotificationRepository notificationRepository, OrganizationsRepository organizationsRepository) {
        this.purchaseHistoryRepository = purchaseHistoryRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.organizationsRepository = organizationsRepository;
    }

    public Set<PurchaseHistory> purchaseHistoryByUser(User user) {
        return RestPreconditions.checkPurchaseHistory(purchaseHistoryRepository.findByUser(user).get());
    }

    public void topUpUserBalance(User user, Double moneyAmount) {
        user.setBalance(user.getBalance() + moneyAmount);
    }


    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void freezeUser(User user) {
        user.getRoles().remove(Role.USER);
        user.getRoles().add(Role.FROZEN);
        userRepository.save(user);
    }

    public void notifyUser(User user, Notification notification) {

        Notification resultNotification = new Notification();

        BeanUtils.copyProperties(notification, resultNotification, "id", "user");

        resultNotification.setDateOfCreation(new Date());
        resultNotification.setUser(user);

        notificationRepository.save(resultNotification);

    }

    public void freezeOrganization(Organization organization){

        organization.setStatus("FROZEN");
        organizationsRepository.save(organization);
    }

    public void deleteOrganization(Organization organization) {
        organization.setStatus("DELETED");
    }
}
