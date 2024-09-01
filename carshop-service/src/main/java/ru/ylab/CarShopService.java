package ru.ylab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class CarShopService {
    public static void main(String[] args) {
        SpringApplication.run(CarShopService.class, args);
    }
}

