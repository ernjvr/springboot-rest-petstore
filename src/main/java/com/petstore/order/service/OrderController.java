package com.petstore.order.service;

import com.petstore.order.dao.OrderRepository;
import com.petstore.order.entity.PetOrder;
import com.petstore.order.resource.OrderResource;
import com.petstore.util.OrderStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/api/orders")
@Api(value = "OrderControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation("Get all orders")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = PetOrder.class)})
    public Collection<PetOrder> findAll() {
        return orderRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation("Get an order by id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = PetOrder.class)})
    public PetOrder findById(@PathVariable Long id) {
        return orderRepository.findOne(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Create a new order")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<?> add(@RequestBody PetOrder order) {
        try {
            final PetOrder saved = orderRepository.save(order);
            final Link selfLink = new OrderResource(saved).getLink("self");
            return ResponseEntity.created(URI.create(selfLink.getHref())).build();
        } catch (JpaObjectRetrievalFailureException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @RequestMapping(value = "/{id}/cancel", method = RequestMethod.PATCH)
    @ApiOperation("Cancel an order by id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        final PetOrder fromDb = orderRepository.findOne(id);
        if (fromDb != null) {
            fromDb.setStatus(OrderStatus.CANCEL);
            orderRepository.save(fromDb);
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
