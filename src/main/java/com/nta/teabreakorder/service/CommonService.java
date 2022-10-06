package com.nta.teabreakorder.service;

import com.nta.teabreakorder.common.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommonService<T> {

    public ResponseEntity get(Pageable pageable) throws Exception;

    public ResponseEntity getById(Long id) throws Exception;

    public ResponseEntity create(T t) throws Exception;

    public ResponseEntity update(T t) throws Exception;

    public ResponseEntity deletes(List<Long> ids) throws Exception;


}
