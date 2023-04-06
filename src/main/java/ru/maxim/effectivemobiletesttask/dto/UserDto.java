package ru.maxim.effectivemobiletesttask.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    public static class Request{
        private String username;
        private String email;
        private String password;
    }
    public static class Response{
        private Long id;
        private String username;
        private String email;
        private Double balance;
    }
}
