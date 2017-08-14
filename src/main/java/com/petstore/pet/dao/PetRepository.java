package com.petstore.pet.dao;

import com.petstore.pet.entity.Pet;
import com.petstore.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PetRepository extends JpaRepository<Pet, Long> {
    Collection<Pet> findByStatus(Status status);
}
