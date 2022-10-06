package com.nta.teabreakorder.service.impl;

import com.nta.teabreakorder.common.CommonUtil;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.enums.Status;
import com.nta.teabreakorder.model.Store;
import com.nta.teabreakorder.repository.StoreRepository;
import com.nta.teabreakorder.repository.dao.StoreDao;
import com.nta.teabreakorder.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private Crawler crawler;

    @Autowired
    private StoreDao storeDao;

    @Override
    public ResponseEntity get(Pageable pageable) throws Exception {
        return CommonUtil.createResponseEntityOK(storeDao.get(pageable));
    }

    @Override
    public ResponseEntity getById(Long id) throws Exception {
        Store store = storeRepository.findById(id).orElseThrow(() -> new Exception("Không tồn tại"));
        return CommonUtil.createResponseEntityOK(store);
    }

    @Override
    public ResponseEntity create(Store store) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity createByUrl(String url) throws Exception {
        return CommonUtil.createResponseEntityOK(crawler.crawler(url));
    }

    @Override
    public ResponseEntity update(Store store) throws Exception {
        return CommonUtil.createResponseEntityOK(storeRepository.save(store));
    }

    @Override
    public ResponseEntity deletes(List<Long> ids) throws Exception {
        storeRepository.setDeleted(ids, true);
        return CommonUtil.createResponseEntityOK(1);
    }


}
