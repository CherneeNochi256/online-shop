package ru.maxim.effectivemobiletesttask.dto;


import lombok.Getter;
import lombok.Setter;


public class CommentDto {

    @Getter
    @Setter
   public static class Response{
        private Long id;
        private String message;
        private ProductDto product;
        private UserDto user;
    }

    @Getter
    @Setter
   public static class Request{
        private String message;
    }


}
