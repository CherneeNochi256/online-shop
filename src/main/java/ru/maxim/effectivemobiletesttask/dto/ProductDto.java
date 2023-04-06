package ru.maxim.effectivemobiletesttask.dto;

import lombok.Getter;
import lombok.Setter;


public class ProductDto {

    @Getter
    @Setter
    public static class Request{
        private String title;
        private Double price;
        private Long quantity;
    }
    @Getter
    @Setter
    public static class Response{
        private Long id;
        private String title;
        private OrganizationDto organization;
        private DiscountDto discount;
        private Double price;
        private Long quantity;
    }
}
