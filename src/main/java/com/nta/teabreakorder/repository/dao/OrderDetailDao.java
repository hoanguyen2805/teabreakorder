package com.nta.teabreakorder.repository.dao;

import com.nta.teabreakorder.common.Const;
import com.nta.teabreakorder.common.DaoUtils;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.OrderDetail;
import lombok.SneakyThrows;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDetailDao implements CommonDao<OrderDetail> {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Const.DATETIME_PATTERN);

    @Autowired
    private EntityManager entityManager;

    @SneakyThrows
    @Override
    public Map<String, Object> get(Pageable pageable) {

        Map<String, Object> resMap = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        Map<String, String> searchMap = null;
        Map<String, String> sortMap = null;
        List<String> filterList = null;
        String id = null;
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String createdAtFrom = null;
        String createdAtTo = null;
        String status = null;
        String isDeleted = null;


        if (pageable.getSearchData() != null) {
            searchMap = DaoUtils.getSearchDataFromParam(pageable.getSearchData());
        }

        sql.append(" select * ");
        sql.append(" FROM order_detail o ");
        sql.append(" inner join orders o2 on o2.id = o.order_id ");
        sql.append(" WHERE o.id IS NOT NULL ");
        sql.append(" AND o.created_by = :username ");

        if (searchMap != null) {
            id = searchMap.get("id");
            if (id != null) {
                sql.append(" AND o.id = :id");
            }
            createdAtFrom = searchMap.get("created_at_from");
            if (createdAtFrom != null) {
                sql.append(" AND o.created_at >= :createdAtFrom ");
            }

            createdAtTo = searchMap.get("created_at_to");
            if (createdAtTo != null) {
                sql.append(" AND o.created_at <= :createdAtTo ");
            }

            status = searchMap.get("status");
            if (status != null) {
                switch (status) {
                    case "ORDERED": {
                        sql.append(" AND o.status = 'ACTIVATED' AND o2.status = 'DONE' ");
                        break;
                    }
                    case "CART": {
                        sql.append(" AND o.status = 'UNACTIVATED'  AND o2.status = 'ACTIVATED' ");
                        break;
                    }
                    case "ORDERING": {
                        sql.append(" AND o.status = 'ACTIVATED'  AND o2.status = 'ACTIVATED' ");
                        break;
                    }
                    case "REJECT": {
                        sql.append(" AND o.status = 'REJECT' ");
                        break;
                    }
                    default: {
                        sql.append(" AND o.id is not null ");
                    }
                }

            }

            isDeleted = searchMap.get("is_deleted");
            if (isDeleted != null) {
                sql.append(" AND o.is_deleted = :is_deleted");
            }
        }

        String sortData = null;
        if (pageable.getSortData() != null) {
            sortData = DaoUtils.covertSortDataToStringByAlias(pageable.getSortData(),"o");
            sortMap = DaoUtils.getSortMapFormParam(pageable.getSortData());
        }
        sql.append(String.format(" ORDER BY %s", sortData != null ? sortData : " o.id desc "));
        sql.append(" OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY  ");
        Query query = entityManager.createNativeQuery(sql.toString(), OrderDetail.class);
        query.setParameter("offset", (pageable.getPage() * pageable.getPageSize()));
        query.setParameter("pageSize", pageable.getPageSize());
        query.setParameter("username", username);

        if (pageable.getFields() != null) {
            filterList = DaoUtils.getFieldsFilter(pageable.getFields());
            pageable.setFieldList(filterList);
        }


        if (searchMap != null) {
            if (id != null) {
                query.setParameter("id", Long.valueOf(id));
            }
            if (createdAtFrom != null) {
                query.setParameter("createdAtFrom", simpleDateFormat.parse(createdAtFrom));
            }
            if (createdAtTo != null) {
                query.setParameter("createdAtTo", simpleDateFormat.parse(createdAtTo));
            }
//            if (status != null) {
//                query.setParameter("status", status);
//            }
            if (isDeleted != null) {
                query.setParameter("is_deleted", Boolean.parseBoolean(isDeleted));
            }
        }

        long count = count(pageable);
        pageable.setTotal(count);
        pageable.setSearch(searchMap);
        pageable.setSort(sortMap);
        resMap.put("pagination", pageable);
        List<OrderDetail> objects = query.getResultList();

        resMap.put("data", objects);
        return resMap;
    }

    @SneakyThrows
    @Override
    public long count(Pageable pageable) {

        StringBuilder sql = new StringBuilder();
        Map<String, String> searchMap = null;
        Map<String, String> sortMap = null;
        List<String> filterList = null;
        String id = null;


        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String createdAtFrom = null;
        String createdAtTo = null;
        String status = null;
        String isDeleted = null;

        if (pageable.getSearchData() != null) {
            searchMap = DaoUtils.getSearchDataFromParam(pageable.getSearchData());
        }

        sql.append(" SELECT count(*) ");
        sql.append(" FROM order_detail o ");
        sql.append(" inner join orders o2 on o2.id = o.order_id ");
        sql.append(" WHERE o.id IS NOT NULL ");
        sql.append(" AND o.created_by = :username ");

        if (searchMap != null) {
            id = searchMap.get("id");
            if (id != null) {
                sql.append(" AND o.id = :id");
            }
            createdAtFrom = searchMap.get("created_at_from");
            if (createdAtFrom != null) {
                sql.append(" AND o.created_at >= :createdAtFrom ");
            }

            createdAtTo = searchMap.get("created_at_to");
            if (createdAtTo != null) {
                sql.append(" AND o.created_at <= :createdAtTo ");
            }

            status = searchMap.get("status");
            if (status != null) {
                switch (status) {
                    case "ORDERED": {
                        sql.append(" AND o.status = 'ACTIVATED' AND o2.status = 'DONE' ");
                        break;
                    }
                    case "ORDERING": {
                        sql.append(" AND o.status = 'ACTIVATED'  AND o2.status = 'ACTIVATED' ");
                        break;
                    }
                    case "CART": {
                        sql.append(" AND o.status = 'UNACTIVATED'  AND o2.status = 'ACTIVATED' ");
                        break;
                    }
                    case "REJECT": {
                        sql.append(" AND o.status = 'REJECT' ");
                        break;
                    }

                    default: {
                        sql.append(" AND o.id is not null ");
                    }
                }
            }

            isDeleted = searchMap.get("is_deleted");
            if (isDeleted != null) {
                sql.append(" AND o.is_deleted = :is_deleted");
            }
        }

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("username", username);

        if (searchMap != null) {
            if (id != null) {
                query.setParameter("id", Long.valueOf(id));
            }
            if (createdAtFrom != null) {
                query.setParameter("createdAtFrom", simpleDateFormat.parse(createdAtFrom));
            }
            if (createdAtTo != null) {
                query.setParameter("createdAtTo", simpleDateFormat.parse(createdAtTo));
            }
//            if (status != null) {
//                query.setParameter("status", status);
//            }
            if (isDeleted != null) {
                query.setParameter("is_deleted", Boolean.parseBoolean(isDeleted));
            }
        }

        return Long.parseLong(query.getSingleResult().toString());
    }


    public Object getOrderDetailsHistory(String username) {
        StringBuilder sql = new StringBuilder();

        sql.append("""
                select SUM(CASE WHEN o.status = 'ACTIVATED' AND od.status = 'UNACTIVATED' THEN 1 ELSE 0 END)      as 'cart',
                         SUM(CASE WHEN o.status = 'DONE' AND od.status = 'ACTIVATED' THEN 1 ELSE 0 END)      as 'ordered',
                         SUM(CASE WHEN o.status = 'ACTIVATED' AND od.status = 'ACTIVATED' THEN 1 ELSE 0 END) as 'ordering',
                         SUM(CASE WHEN od.status = 'REJECT' THEN 1 ELSE 0 END)                               as 'reject'
                  from order_detail od
                           inner join orders o on o.id = od.order_id
                           inner join users u on od.user_id = u.id
                  where od.is_deleted = 'false'  AND u.username = :username""");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("username", username);
        NativeQueryImpl nativeQuery = (NativeQueryImpl) query;
        nativeQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.getSingleResult();
    }
}
