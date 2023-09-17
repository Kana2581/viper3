package com.example.demo.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Info {
    int id;
    String name;
    String tel;
    String address;
    String orderId;
    String status;
    String CreateAt;
    String sfId;
}
