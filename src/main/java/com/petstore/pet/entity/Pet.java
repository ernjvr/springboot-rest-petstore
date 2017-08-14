package com.petstore.pet.entity;

import com.petstore.util.Gender;
import com.petstore.util.Status;

import javax.persistence.*;

@Entity
public class Pet {
    @Id
    @GeneratedValue
    private Long id;
    private int age;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Type type;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Pet() {//jpa
    }

    public Pet(int age, Gender gender, Type type, Status status) {
        this.age = age;
        this.gender = gender;
        this.type = type;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public Type getType() {
        return type;
    }

    public Status getStatus() {
        return status;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
