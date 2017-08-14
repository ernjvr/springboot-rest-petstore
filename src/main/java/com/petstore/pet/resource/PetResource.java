package com.petstore.pet.resource;

import com.petstore.pet.entity.Pet;
import com.petstore.pet.service.PetController;
import org.springframework.hateoas.ResourceSupport;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

public class PetResource extends ResourceSupport {

    private final Pet pet;

    public PetResource(Pet pet) {
        this.pet = pet;
        add(linkTo(PetController.class).withRel("pets"));
        add(linkTo(methodOn(PetController.class, pet.getId()).findById(pet.getId())).withSelfRel());
    }

    public Pet getPet() {
        return pet;
    }
}
