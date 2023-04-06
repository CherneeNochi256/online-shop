package ru.maxim.effectivemobiletesttask.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.maxim.effectivemobiletesttask.entity.Grade} entity
 */

public class GradeDto implements Serializable {

    @Getter
    @Setter
    public static class Request{
        private  Double value;
    }
    @Getter
    @Setter
    public static class Response{
        private  Long id;
        private  Double value;
        private  ProductDto product;
        private  UserDto user;
    }
}