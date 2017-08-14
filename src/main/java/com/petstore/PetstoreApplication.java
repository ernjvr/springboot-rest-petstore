package com.petstore;

import com.petstore.order.dao.OrderRepository;
import com.petstore.order.entity.PetOrder;
import com.petstore.pet.dao.PetRepository;
import com.petstore.pet.entity.Pet;
import com.petstore.pet.entity.Type;
import com.petstore.user.dao.UserRepository;
import com.petstore.user.entity.User;
import com.petstore.util.Gender;
import com.petstore.util.Status;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.IntStream;

@SpringBootApplication
public class PetstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetstoreApplication.class, args);
    }

    @Bean
    CommandLineRunner init(PetRepository petRepository, UserRepository userRepository, OrderRepository orderRepository) {
        return (evt) -> {
            final List<Pet> pets = new ArrayList<>();
            IntStream.rangeClosed(1, 10)
                    .forEach(i -> {
                        final Pet pet = new Pet(i, i % 2 == 0 ? Gender.FEMALE : Gender.MALE,
                                i % 2 == 0 ? Type.CAT : Type.DOG, i % 2 == 0 ? Status.FOR_SALE : Status.ORDER);
                        final User user = new User("username" + i, "password" + i, false);
                        final User savedUser = userRepository.save(user);
                        pets.clear();
                        pets.add(pet);
                        final PetOrder savedOrder = orderRepository.save(new PetOrder(pets, savedUser));
                    });
        };
    }
}
