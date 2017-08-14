package com.petstore.user.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private boolean loggedIn;

    public User() {//jpa
    }

    public User(String firstname, String lastName, boolean loggedin) {
        this.username = firstname;
        this.password = lastName;
        this.loggedIn = loggedin;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    
}
