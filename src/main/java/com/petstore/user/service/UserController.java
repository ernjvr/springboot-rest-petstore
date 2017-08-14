/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.petstore.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.petstore.pet.entity.Pet;
import com.petstore.user.resource.UserResource;
import com.petstore.user.dao.UserRepository;
import com.petstore.user.entity.User;
import java.net.URI;
import java.util.Collection;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import static com.petstore.util.JsonUtil.*;
import java.util.Optional;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/users")
@Api(value = "UserControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserRepository repository;

    @Autowired
    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation("Get all users")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = User.class)})
    public Collection<User> findAll() {
        return repository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation("Get a user by id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = User.class)})
    public User findById(@PathVariable Long id) {
        return repository.findOne(id);
    }

    @RequestMapping(value = "/username/{username}", method = RequestMethod.GET)
    @ApiOperation("Get all users by username")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = User.class)})
    public User findByUsername(@PathVariable String username) {
        return repository.findByUsername(username);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Create a new user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<?> add(@RequestBody User user) {
        final User saved = repository.save(user);
        final Link selfLink = new UserResource(saved).getLink("self");
        return ResponseEntity.created(URI.create(selfLink.getHref())).build();
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ApiOperation("Update a user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<?> update(@RequestBody User user) {
        repository.save(user);
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation("Delet a user by id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<?> delete(@PathVariable Long id) {
        repository.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}/login", method = RequestMethod.PATCH)
    @ApiOperation("Log in a user by id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<?> login(@PathVariable Long id,@RequestBody String json) {
        try {
                final User fromDb = repository.findOne(id);
                if (fromDb != null) {
                    final Optional<Map.Entry<String, JsonNode>> username = getColumn("username", streamFrom(json));
                    final Optional<Map.Entry<String, JsonNode>> password = getColumn("password", streamFrom(json));
                    if (username.isPresent() && password.isPresent()) {
                        if (fromDb.getUsername().equals(stringValue(username))
                                && fromDb.getPassword().equals(stringValue(password))) {
                            fromDb.setLoggedIn(true);
                            repository.save(fromDb);
                            return ResponseEntity.accepted().build();
                        } else {
                            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("username and password is invalid");
                        }
                    } else {
                        return ResponseEntity.badRequest().body("username and password is required");
                    }
                } else {
                    return ResponseEntity.notFound().build();
                }
        } catch (RuntimeException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }
    
    @RequestMapping(value = "/{id}/logout", method = RequestMethod.PATCH)
    @ApiOperation("Log out a user by id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<?> logout(@PathVariable Long id) {
        final User fromDb = repository.findOne(id);
        if (fromDb != null) {
            fromDb.setLoggedIn(false);
            repository.save(fromDb);
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
