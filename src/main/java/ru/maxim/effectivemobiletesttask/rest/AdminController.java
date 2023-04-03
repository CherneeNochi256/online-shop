package ru.maxim.effectivemobiletesttask.rest;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.Notification;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.AdminService;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

import java.util.Set;

@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("api/main/admin")
public class AdminController {


    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @GetMapping("history/{id}")
    public Set<PurchaseHistory> specificUserHistory(@PathVariable("id") User user) {
        RestPreconditions.checkUser(user);

        return adminService.purchaseHistoryByUser(user);
    }


    @GetMapping("topUp/{id}")
    public void topUp(@PathVariable("id") User user,
                      @RequestParam Double moneyAmount) {
        RestPreconditions.checkUser(user);

        adminService.topUpUserBalance(user, moneyAmount);
    }


    @GetMapping("{id}")
    public User getUser(@PathVariable("id") User user) {
        return RestPreconditions.checkUser(user);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable("id") User user) {
        RestPreconditions.checkUser(user);
        adminService.deleteUser(user);
    }

    @PutMapping("freeze/{id}")
    public void freezeUser(@PathVariable("id") User user) {
        RestPreconditions.checkUser(user);
        adminService.freezeUser(user);
    }


    @PostMapping("notify/{id}")
    public void notifyUser(@PathVariable("id") User user,
                           @RequestBody Notification notification) {
        RestPreconditions.checkUser(user);
        adminService.notifyUser(user, notification);
    }

    @PutMapping("freeze/organization/{id}")
    public void freezeOrganization(@PathVariable("id") Organization organization) {
        RestPreconditions.checkOrganization(organization);

        adminService.freezeOrganization(organization);

    }

    @DeleteMapping("organization/{id}")
    public void deleteOrganization(@PathVariable("id") Organization organization) {
        RestPreconditions.checkOrganization(organization);

        adminService.deleteOrganization(organization);

    }


}
