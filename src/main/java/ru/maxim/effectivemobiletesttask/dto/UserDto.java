package ru.maxim.effectivemobiletesttask.dto;

import lombok.Getter;
import lombok.Setter;


public class UserDto {
    @Getter
    @Setter
    public static class Request{
        private String username;
        private String email;
        private String password;
    }
    @Getter
    @Setter
    public static class Response{
        private Long id;
        private String username;
        private String email;
        private Double balance;
    }
}
