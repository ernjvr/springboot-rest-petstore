package com.petstore.order.resource;

import com.petstore.order.entity.PetOrder;
import com.petstore.order.service.OrderController;
import org.springframework.hateoas.ResourceSupport;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

public class OrderResource extends ResourceSupport {

    private final PetOrder order;

    public OrderResource(PetOrder order) {
        this.order = order;
        add(linkTo(OrderController.class).withRel("orders"));
        add(linkTo(methodOn(OrderController.class, order.getId()).findById(order.getId())).withSelfRel());
    }

    public PetOrder getOrder() {
        return order;
    }
}
