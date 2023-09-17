package com.example.demo.entity;

import lombok.Data;

@Data
public class RestBean<T> {
    int status;
    String message;
    T data;

    public RestBean(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public RestBean(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
