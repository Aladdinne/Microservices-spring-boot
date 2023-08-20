package com.programmingtechie.orderservice.service;

import com.programmingtechie.orderservice.dto.InventoryResponse;
import com.programmingtechie.orderservice.dto.OrderLineItemsDto;
import com.programmingtechie.orderservice.dto.OrderRequest;
import com.programmingtechie.orderservice.model.Order;
import com.programmingtechie.orderservice.model.OrderLineItems;
import com.programmingtechie.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.swing.border.Border;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest){
    Order order = new Order();
    order.setOrderNumber(UUID.randomUUID().toString());

    List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
            .stream()
            .map(this::mapToDto)
            .toList();

    order.setOrderLineItemsList(orderLineItems);
    List<String> SkuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();
    InventoryResponse[] inventoryResponse = webClient.get()
            .uri("http://localhost:1117/api/inventory/skuCode",uriBuilder -> uriBuilder.queryParam("skuCode",SkuCodes)
                    .build())
            .retrieve()
            .bodyToMono(InventoryResponse[].class)
            .block();
    System.out.println(Arrays.stream(inventoryResponse).toList());
    boolean AllProductsInStock = Arrays.stream(inventoryResponse).allMatch(InventoryResponse::isInStock);
    System.out.println(AllProductsInStock);
    if (AllProductsInStock){
        orderRepository.save(order);
    } else {
        throw new IllegalArgumentException("Product is not in stock, please try again");
    }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItems.getQuantity());
        orderLineItems.setSkuCode(orderLineItems.getSkuCode());
        return orderLineItems;
    }
}
