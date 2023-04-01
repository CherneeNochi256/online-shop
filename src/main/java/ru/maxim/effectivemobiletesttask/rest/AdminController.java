package ru.maxim.effectivemobiletesttask.rest;


import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.*;
import ru.maxim.effectivemobiletesttask.repository.NotificationRepository;
import ru.maxim.effectivemobiletesttask.repository.OrganizationsRepository;
import ru.maxim.effectivemobiletesttask.repository.PurchaseHistoryRepository;
import ru.maxim.effectivemobiletesttask.repository.UserRepository;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("api/main/admin")
public class AdminController {

    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final OrganizationsRepository organizationsRepository;

    public AdminController(PurchaseHistoryRepository purchaseHistoryRepository, UserRepository userRepository, NotificationRepository notificationRepository,
                           OrganizationsRepository organizationsRepository) {
        this.purchaseHistoryRepository = purchaseHistoryRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.organizationsRepository = organizationsRepository;
    }


    @GetMapping("history/{id}")
    public Set<PurchaseHistory> specificUserHistory(@PathVariable("id") User user) {
        return purchaseHistoryRepository.findByUser(user);
    }

    @GetMapping("topUp/{id}/{moneyAmount}")
    public void topUp(@PathVariable("id") User user,
                      @PathVariable Double moneyAmount) {
        Optional<User> userFromDb = userRepository.findById(user.getId());

        userFromDb.ifPresent(u -> u.setBalance(u.getBalance() + moneyAmount));
    }


    @GetMapping("{id}")
    public User getUser(@PathVariable("id") User user) {
        return userRepository.findById(user.getId()).get();
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable("id") User user) {
        Optional<User> userFromDb = userRepository.findById(user.getId());

        userFromDb.ifPresent(userRepository::delete);
    }

    @PutMapping("freeze/{id}")
    public void freezeUser(@PathVariable("id") String id) {
        Optional<User> userFromDb = userRepository.findById(Long.parseLong(id));


        if (userFromDb.isPresent()) {
            userFromDb.get().getRoles().remove(Role.USER);
            userFromDb.get().getRoles().add(Role.FROZEN);
            userRepository.save(userFromDb.get());
        }
    }


    @PostMapping("notify/{id}")
    public void notifyUser(@PathVariable("id") String id,
                           @RequestBody Notification notification) {
        Optional<User> userFromDb = userRepository.findById(Long.parseLong(id));


        if (userFromDb.isPresent()) {
            Notification resultNotification = new Notification();

            BeanUtils.copyProperties(notification, resultNotification, "id", "user");

            resultNotification.setDateOfCreation(new Date());

            resultNotification.setUser(userFromDb.get());

            notificationRepository.save(resultNotification);
        }
    }

    @PutMapping("freeze/organization/{id}")
    public void freezeOrganization(@PathVariable("id") String id) {
        Optional<Organization> organizationFromDb = organizationsRepository.findById(Long.parseLong(id));

        if (organizationFromDb.isPresent()) {
            organizationFromDb.get().setStatus("FROZEN");
            organizationsRepository.save(organizationFromDb.get());
        }

    }

    @DeleteMapping("organization/{id}")
    public void deleteOrganization(@PathVariable("id") String id) {
        Optional<Organization> organizationFromDb = organizationsRepository.findById(Long.parseLong(id));

        organizationFromDb.ifPresent(organization -> organization.setStatus("DELETED"));

    }




}
