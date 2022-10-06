package com.nta.teabreakorder.controller;

import com.nta.teabreakorder.service.impl.Crawler;
import com.nta.teabreakorder.model.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private Crawler crawler;

    @PostMapping("/by-url")
    public ResponseEntity<Store> createByUrl(@RequestParam String url) throws Exception {
        return ResponseEntity.ok(crawler.crawler(url));
    }

}
