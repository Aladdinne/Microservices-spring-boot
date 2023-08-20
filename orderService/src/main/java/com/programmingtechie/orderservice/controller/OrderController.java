package com.programmingtechie.orderservice.controller;

import com.programmingtechie.orderservice.dto.OrderRequest;
import com.programmingtechie.orderservice.model.Order;
import com.programmingtechie.orderservice.repository.OrderRepository;
import com.programmingtechie.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oder")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private OrderService orderService;
    private final OrderRepository orderRepository;

    @PostMapping("/addOrder")
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        orderService.placeOrder(orderRequest);
        return "Order Place Successfuly";
    }
    @PostMapping("/add")
    public void add(){
        Order order = new Order();
        orderRepository.save(order);
    }

}
