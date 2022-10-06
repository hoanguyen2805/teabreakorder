package com.nta.teabreakorder.repository.dao;

import com.nta.teabreakorder.common.Pageable;

import java.util.Map;

public interface CommonDao<T> {

    public Map<String,Object> get(Pageable pageable);

    public long count(Pageable pageable);
}
