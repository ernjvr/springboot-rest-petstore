package com.petstore.order.entity;

import com.petstore.pet.entity.Pet;
import com.petstore.user.entity.User;
import com.petstore.util.OrderStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class PetOrder {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.NEW;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Collection<Pet> pets = new ArrayList<>();

    @ManyToOne(optional = false)
    private User user;

    public PetOrder() {//jpa
    }

    public PetOrder(List<Pet> pets, User user) {
        this.pets = pets;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Collection<Pet> getPets() {
        return pets;
    }

    public void setPets(Collection<Pet> pets) {
        this.pets = pets;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
