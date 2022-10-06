package com.nta.teabreakorder.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class CommonUtil {
    private static final String DATA_KEY = "data";
    private static final String PAGINATION_KEY = "pagination";

    public static ResponseEntity createResponseEntityOK(Object dataValue) {
        Map<String, Object> map = new HashMap<>();
        map.put(DATA_KEY,dataValue);
        return ResponseEntity.ok(map);
    }


    public static ResponseEntity createResponseEntityOKWithPagination(Object dataValue,Object pagination) {
        Map<String, Object> map = new HashMap<>();
        map.put(DATA_KEY,dataValue);
        map.put(PAGINATION_KEY,pagination);
        return ResponseEntity.ok(map);
    }


    public static ResponseEntity createResponseEntityOK(Map<String, Object> dataValue) {
        return ResponseEntity.ok(dataValue);
    }

    public static ObjectMapper getObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }


}
