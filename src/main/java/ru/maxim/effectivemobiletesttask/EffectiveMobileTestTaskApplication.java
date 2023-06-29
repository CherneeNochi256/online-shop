package ru.maxim.effectivemobiletesttask;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class EffectiveMobileTestTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(EffectiveMobileTestTaskApplication.class, args);
    }

    @Profile({"railway"})
    @Bean
    CommandLineRunner run(){
        return args -> {};
    }

}
