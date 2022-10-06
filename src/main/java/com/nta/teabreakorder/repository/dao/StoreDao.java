package com.nta.teabreakorder.repository.dao;

import com.nta.teabreakorder.common.DaoUtils;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class StoreDao implements CommonDao<Store> {
    @Autowired
    private EntityManager entityManager;

    @Override
    public Map<String, Object> get(Pageable pageable) {
        Map<String, Object> resMap = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        Map<String, String> searchMap = null;
        Map<String, String> sortMap = null;
        List<String> filterList = null;
        String id = null;

        if (pageable.getSearchData() != null) {
            searchMap = DaoUtils.getSearchDataFromParam(pageable.getSearchData());
        }

        sql.append("select s.id, " +
                "       s.url, " +
                "       s.name, " +
                "       s.address, " +
                "       s.created_by, " +
                "       s.modified_by, " +
                "       s.created_at, " +
                "       s.modified_at, " +
                "       s.is_deleted " +
                " from stores s ");
        sql.append(" WHERE s.id is not null ");


        if (searchMap != null) {
            id = searchMap.get("id");
            if (id != null) {
                sql.append(" AND s.id = :id");
            }

        }

        String sortData= null;
        if (pageable.getSortData() != null) {
            sortData = DaoUtils.covertSortDataToStringByAlias(pageable.getSortData(),"s");
            sortMap = DaoUtils.getSortMapFormParam(pageable.getSortData());
        }
        sql.append(String.format(" ORDER BY %s", sortData != null ? sortData : "(SELECT NULL) "));
        sql.append(" OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY  ");
        Query query = entityManager.createNativeQuery(sql.toString(), "StoreMapping");
        query.setParameter("offset", (pageable.getPage() * pageable.getPageSize()));
        query.setParameter("pageSize", pageable.getPageSize());


        if (pageable.getFields() != null) {
            filterList = DaoUtils.getFieldsFilter(pageable.getFields());
            pageable.setFieldList(filterList);
        }


        if (searchMap != null) {
            if (id != null) {
                query.setParameter("id", Long.valueOf(id));
            }
        }

        long count = count(pageable);
        pageable.setTotal(count);
        pageable.setSearch(searchMap);
        pageable.setSort(sortMap);
        resMap.put("pagination", pageable);
        List<Object> objects = query.getResultList();

        if (pageable.getFieldList() != null) {
            objects = objects.stream().map(ob -> ob = DaoUtils.filterField(pageable.getFieldList(), ob)).collect(Collectors.toList());
        }

        resMap.put("data", objects);


        return resMap;
    }

    @Override
    public long count(Pageable pageable) {

        StringBuilder sql = new StringBuilder();
        Map<String, String> searchMap = null;
        String id = null;

        if (pageable.getSearchData() != null) {
            searchMap = DaoUtils.getSearchDataFromParam(pageable.getSearchData());
        }

        sql.append("select count(s.id) from stores s ");
        sql.append(" WHERE s.id is not null ");

        if (searchMap != null) {
            id = searchMap.get("id");
            if (id != null) {
                sql.append(" AND s.id = :id");
            }

        }

        Query query = entityManager.createNativeQuery(sql.toString());

        if (searchMap != null) {
            if (id != null) {
                query.setParameter("id", Long.valueOf(id));
            }
        }

        return Long.parseLong(query.getSingleResult().toString());
    }
}
