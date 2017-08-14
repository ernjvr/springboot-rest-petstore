package com.petstore.pet.service;

import com.petstore.pet.dao.PetRepository;
import com.petstore.pet.entity.Pet;
import com.petstore.pet.entity.Type;
import com.petstore.pet.resource.PetResource;
import com.petstore.util.Gender;
import com.petstore.util.Status;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import static com.petstore.util.JsonUtil.*;

@RestController
@RequestMapping("/api/pets")
@Api(value = "PetControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class PetController {

    private final PetRepository petRepository;

    @Autowired
    public PetController(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation("Get all pets")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Pet.class)})
    public Collection<Pet> findAll() {
        return petRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation("Get a pet by id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Pet.class)})
    public Pet findById(@PathVariable Long id) {
        return petRepository.findOne(id);
    }

    @RequestMapping(value = "/status/{status}", method = RequestMethod.GET)
    @ApiOperation("Get all pets by status")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Pet.class)})
    public Collection<Pet> findByStatus(@PathVariable Status status) {
        return petRepository.findByStatus(status);
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ApiOperation("Get pets count by status")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Status.class)})
    public Map<Status, Long> countByStatus() {
        return petRepository.findAll().stream().map(Pet::getStatus)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Create a new pet")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<?> add(@RequestBody Pet pet) {
        final Pet saved = petRepository.save(pet);
        final Link selfLink = new PetResource(saved).getLink("self");
        return ResponseEntity.created(URI.create(selfLink.getHref())).build();
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ApiOperation("Update a pet")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<?> update(@RequestBody Pet pet) {
        petRepository.save(pet);
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(method = RequestMethod.PATCH)
    @ApiOperation("Partially update a pet")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<?> patch(@RequestBody String petJson) {
        try {
            return getColumn("id", streamFrom(petJson)).map(id -> {
                final Pet fromDb = petRepository.findOne(Long.parseLong(id.getValue().toString()));
                if (fromDb != null) {
                    getColumn("age", streamFrom(petJson)).ifPresent(age -> fromDb.setAge(Integer.parseInt(age.getValue().toString())));
                    getColumn("gender", streamFrom(petJson)).ifPresent(gender -> fromDb.setGender(Gender.valueOf(stringValue(gender))));
                    getColumn("type", streamFrom(petJson)).ifPresent(type -> fromDb.setType(Type.valueOf(stringValue(type))));
                    getColumn("status", streamFrom(petJson)).ifPresent(status -> fromDb.setStatus(Status.valueOf(stringValue(status))));
                    petRepository.save(fromDb);
                    return ResponseEntity.accepted().build();
                } else {
                    return ResponseEntity.notFound().build();
                }
            }).orElse(ResponseEntity.noContent().build());
        } catch (RuntimeException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }
}
