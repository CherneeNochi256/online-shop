package ru.maxim.effectivemobiletesttask.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.AdminService;
import ru.maxim.effectivemobiletesttask.service.UserService;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("api/main/admin")
@RequiredArgsConstructor
public class AdminController {


    private final AdminService adminService;
    private final UserService userService;


    @GetMapping("topUp/{id}")
    public void topUp(@PathVariable("id") Long id,
                      @RequestParam Double moneyAmount) {
        User user = RestPreconditions.checkUser(userService.userById(id));

        adminService.topUpUserBalance(user, moneyAmount);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        User user = RestPreconditions.checkUser(userService.userById(id));
        adminService.deleteUser(user);
    }

    @PutMapping("freeze/{id}")
    public void freezeUser(@PathVariable("id") Long id) {
        User user = RestPreconditions.checkUser(userService.userById(id));
        adminService.freezeUser(user);
    }


}
