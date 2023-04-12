package ru.maxim.effectivemobiletesttask.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.maxim.effectivemobiletesttask.dto.UserDto;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.UserService;
import ru.maxim.effectivemobiletesttask.utils.EntityMapper;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

@RestController
@RequestMapping("api/main/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EntityMapper entityMapper;

    @GetMapping("{id}")
    public UserDto.Response getUser(@PathVariable("id") Long id) {
        User user = RestPreconditions.checkUser(userService.userById(id));
        return entityMapper.entityToUserDto(user);
    }


}
