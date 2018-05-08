package com.duplex.presentation.demo.model;

import com.duplex.presentation.demo.dto.CSVDataDTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private int age;
    private String height;

    public Person() {
    }

    public Person(String name, int age, String height) {
        this.name = name;
        this.age = age;
        this.height = height;
    }

    public Person(CSVDataDTO dataDTO) {
        this.name = dataDTO.getName();
        this.age = dataDTO.getAge();
        this.height = dataDTO.getHeight();
    }

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
