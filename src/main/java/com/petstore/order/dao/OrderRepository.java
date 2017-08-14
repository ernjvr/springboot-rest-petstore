package com.petstore.order.dao;

import com.petstore.order.entity.PetOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<PetOrder, Long> {
}
