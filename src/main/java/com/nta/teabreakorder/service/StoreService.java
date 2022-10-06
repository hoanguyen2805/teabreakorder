package com.nta.teabreakorder.service;

import com.nta.teabreakorder.model.Store;
import org.springframework.http.ResponseEntity;

public interface StoreService extends CommonService<Store> {

    public ResponseEntity createByUrl(String url) throws  Exception;

}
