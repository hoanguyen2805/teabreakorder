package com.nta.teabreakorder.controller;

import com.nta.teabreakorder.model.Product;
import com.nta.teabreakorder.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("")
    public ResponseEntity create(@RequestBody Product product) throws Exception {
        return productService.create(product);
    }

    @PutMapping("")
    public ResponseEntity put(@RequestBody Product product) throws Exception {
        return productService.update(product);
    }

    @DeleteMapping("")
    public ResponseEntity put(@RequestBody List<Long> ids) throws Exception {
        return productService.deletes(ids);
    }

}
