package ru.maxim.effectivemobiletesttask.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.ApiResponse;
import ru.maxim.effectivemobiletesttask.dto.user.UserDtoResponse;
import ru.maxim.effectivemobiletesttask.service.UserService;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("{id}")
    public ResponseEntity<UserDtoResponse> getUser(@PathVariable("id") Long id) {
        return userService.userById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}/balance")
    public ResponseEntity<UserDtoResponse> topUp(@PathVariable("id") Long userId,
                                                 @RequestParam Double moneyAmount) {
        return userService.topUpUserBalance(userId, moneyAmount);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("id") Long userId) {
        return userService.deleteUser(userId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}/frozen")
    public ResponseEntity<ApiResponse> freezeUser(@PathVariable("id") Long userId) {
        return userService.freezeUser(userId);
    }


}
