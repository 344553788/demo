package com.example.demo.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public abstract class BaseEntity implements Serializable {

    private  int id;

    private String createUser;

    private Instant createTime;

    private String lastModifyUser;

    private Instant updateTime;
}
