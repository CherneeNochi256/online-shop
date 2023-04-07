package ru.maxim.effectivemobiletesttask.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class DiscountDto implements Serializable {

   @Getter
   @Setter
    public static class Request{
        private Double discount;
        private  Long interval;
    }


    @Getter
    @Setter
    public static class Response{
        private Long id;
        private Double discount;
        private Long interval;
    }
}
