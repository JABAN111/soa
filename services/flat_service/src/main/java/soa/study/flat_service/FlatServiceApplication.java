package soa.study.flat_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class FlatServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(FlatServiceApplication.class, args);
    }

}
