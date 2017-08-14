/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.petstore.user.resource;

import com.petstore.user.entity.User;
import com.petstore.user.service.UserController;
import org.springframework.hateoas.ResourceSupport;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 *
 * @author admin
 */
public class UserResource extends ResourceSupport {
    
    private final User user;

    public UserResource(User user) {
        this.user = user;
        add(linkTo(UserController.class).withRel("users"));
        add(linkTo(methodOn(UserController.class, user.getId()).findById(user.getId())).withSelfRel());
    }

    public User getUser() {
        return user;
    }
}
