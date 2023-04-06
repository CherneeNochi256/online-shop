package ru.maxim.effectivemobiletesttask.dto;

import lombok.Getter;
import lombok.Setter;


public class OrganizationDto {

    @Getter
    @Setter
    public static class Request{
        private String name;
        private String description;
    }
    @Getter
    @Setter
    public static class Response{
        private Long id;
        private String name;
        private String description;
        private UserDto user;
        private String status;
    }
}
