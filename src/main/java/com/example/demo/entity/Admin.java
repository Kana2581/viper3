package com.example.demo.entity;

import lombok.Data;

import java.sql.Timestamp;
@Data
public class Admin {
    int id;
    String account;
    String psw;
    String name;

    String createAt;
    String role;
    String status;
}
