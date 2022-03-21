package com.ljz.phd.city.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CityTmp {
    private Integer id;
    private Date dt;
    private String name;
    private String filename;
    private Integer sn;
    private Integer columnsize;
    private Integer ensize;
    private Integer datsize;

    private String h1;
    private String h2;
    private String h3;
    private String h4;
    private String h5;
    private String h6;
    private String h7;
    private String h8;

    private Integer year;
    private String city;
    private String province;

    private String d01;
    private String d02;
    private String d03;
    private String d04;
    private String d05;
    private String d06;
    private String d07;
    private String d08;
}