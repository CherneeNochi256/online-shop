package ru.maxim.effectivemobiletesttask.dto;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link ru.maxim.effectivemobiletesttask.entity.Notification} entity
 */

public class NotificationDto implements Serializable {


    @Getter
    @Setter
    public static class Request{
        private String header;
        private String text;
    }
    @Getter
    @Setter
    public static class Response{
        private Long id;
        private Date dateOfCreation;
        private String header;
        private String text;
        private UserDto user;
    }

}