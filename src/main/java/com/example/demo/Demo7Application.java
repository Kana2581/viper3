package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.io.UnsupportedEncodingException;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class Demo7Application {
    public static void main(String[] args)throws UnsupportedEncodingException {
             SpringApplication.run(Demo7Application.class, args);
    }
}
