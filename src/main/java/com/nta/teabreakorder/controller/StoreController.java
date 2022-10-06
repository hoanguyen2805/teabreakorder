package com.nta.teabreakorder.controller;

import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.Store;
import com.nta.teabreakorder.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping("")
    public ResponseEntity get(@RequestParam(required = false) Integer page,
                              @RequestParam(required = false) Integer pageSize,
                              @RequestParam(required = false) String searchData,
                              @RequestParam(required = false) String sortData,
                              @RequestParam(required = false) String fields) throws Exception {

        Pageable pageable = Pageable.ofValue(page, pageSize, searchData, sortData, fields);
        return storeService.get(pageable);
    }

    @PostMapping("")
    public ResponseEntity create(@RequestParam(required = true) String url) throws Exception {
        return storeService.createByUrl(url);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id) throws Exception {
        return storeService.getById(id);
    }


    @PutMapping("")
    public ResponseEntity update(@RequestBody Store store) throws Exception {
        return storeService.update(store);
    }

    @DeleteMapping("")
    public ResponseEntity deletes(@RequestBody List<Long> ids) throws Exception {
        storeService.deletes(ids);
        return ResponseEntity.ok(1);
    }


}
