package com.programmingtechie.productservice.Controller;

import com.programmingtechie.productservice.Dto.ProductRequest;
import com.programmingtechie.productservice.Dto.ProductResponse;
import com.programmingtechie.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest){
        productService.createProduct(productRequest);
    }
    @GetMapping("/ListProduct")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> ProductList(){
        return productService.getAllProduct();
    }
}
