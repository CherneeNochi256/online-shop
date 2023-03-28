package ru.maxim.effectivemobiletesttask.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.Role;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.UserService;

import java.util.Collections;
import java.util.Set;

@Controller
@RequestMapping()
public class MainController {

    private UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("registration")
    public String registration(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email) {
        User user = new User();

        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setBalance(0.0);
        user.setRoles(Collections.singleton(Role.USER));

        userService.createUser(user);
        return "redirect:/login";
    }

    @PostMapping("admin/registration")
    public String registrationAdmin(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email) {
        User user = new User();

        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setBalance(0.0);
        user.setRoles(Set.of(Role.USER, Role.ADMIN));

        userService.createUser(user);
        return "redirect:/login";
    }
}
