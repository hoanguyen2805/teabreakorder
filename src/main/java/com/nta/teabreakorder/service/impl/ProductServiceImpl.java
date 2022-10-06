package com.nta.teabreakorder.service.impl;

import com.nta.teabreakorder.common.CommonUtil;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.Product;
import com.nta.teabreakorder.repository.ProductRepository;
import com.nta.teabreakorder.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public ResponseEntity get(Pageable pageable) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity getById(Long id) throws Exception {
        Product product = productRepository.findById(id).orElseThrow(() -> new Exception("No product found"));
        return CommonUtil.createResponseEntityOK(product);
    }

    @Override
    public ResponseEntity create(Product product) throws Exception {
        return CommonUtil.createResponseEntityOK(productRepository.save(product));
    }

    @Override
    public ResponseEntity update(Product product) throws Exception {
        return CommonUtil.createResponseEntityOK(productRepository.save(product));
    }


    @Override
    public ResponseEntity deletes(List<Long> ids) throws Exception {
        productRepository.deletes(ids);
        return CommonUtil.createResponseEntityOK(1);
    }
}
