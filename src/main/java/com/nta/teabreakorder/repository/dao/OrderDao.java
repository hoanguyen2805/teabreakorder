package com.nta.teabreakorder.repository.dao;

import com.nta.teabreakorder.common.Const;
import com.nta.teabreakorder.common.DaoUtils;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.Order;
import com.nta.teabreakorder.payload.response.AmountProduct;
import com.nta.teabreakorder.payload.response.TotalBill;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class OrderDao implements CommonDao<OrderDao> {
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
        String createdAtFrom = null;
        String createdAtTo = null;
        String status = null;
        String isDeleted = null;
        String timeRemaining = null;
        String createdBy = null;


        if (pageable.getSearchData() != null) {
            searchMap = DaoUtils.getSearchDataFromParam(pageable.getSearchData());
        }

        sql.append("select o.id, " +
                "       o.status, " +
                "       o.created_by, " +
                "       o.modified_by, " +
                "       o.created_at, " +
                "       o.modified_at, " +
                "       o.is_deleted, " +
                "       s.id as store_id, " +
                "       s.name as store_name, " +
                "       o.time_remaining, " +
                "       s.img, " +
                "       s.address " +
                "   from orders o " +
                "         inner join stores s on o.store_id = s.id ");
        sql.append(" WHERE o.id is not null");


        if (searchMap != null) {
            id = searchMap.get("id");
            if (id != null) {
                sql.append(" AND o.id = :id ");
            }

            createdAtFrom = searchMap.get("created_at_from");
            if (createdAtFrom != null) {
                sql.append(" AND o.created_at >= :createdAtFrom ");
            }

            createdAtTo = searchMap.get("created_at_to");
            if (createdAtTo != null) {
                sql.append(" AND o.created_at <= :createdAtTo ");
            }

            timeRemaining = searchMap.get("time_remaining");
            if (timeRemaining != null) {
                sql.append(" AND o.time_remaining >= :timeRemaining ");
            }

            status = searchMap.get("status");
            if (status != null) {
                sql.append(" AND o.status = :status ");
            }

            isDeleted = searchMap.get("is_deleted");
            if (isDeleted != null) {
                sql.append(" AND o.is_deleted = :is_deleted");
            }

            createdBy = searchMap.get("created_by");
            if (createdBy != null) {
                sql.append(" AND o.created_by = :created_by");
            }


        }

        String sortData = null;
        if (pageable.getSortData() != null) {
            sortData = DaoUtils.covertSortDataToStringByAlias(pageable.getSortData(),"o");
            sortMap = DaoUtils.getSortMapFormParam(pageable.getSortData());
        }
        sql.append(String.format(" ORDER BY %s", sortData != null ? sortData : "(SELECT NULL) "));
        sql.append(" OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY  ");
        Query query = entityManager.createNativeQuery(sql.toString(), "OrderMapping");
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
            if (createdAtFrom != null) {
                query.setParameter("createdAtFrom", simpleDateFormat.parse(createdAtFrom));
            }
            if (createdAtTo != null) {
                query.setParameter("createdAtTo", simpleDateFormat.parse(createdAtTo));
            }
            if (timeRemaining != null) {
                query.setParameter("timeRemaining", simpleDateFormat.parse(timeRemaining));
            }

            if (status != null) {
                query.setParameter("status", status);
            }
            if (isDeleted != null) {
                query.setParameter("is_deleted", Boolean.parseBoolean(isDeleted));
            }
            if (createdBy != null) {
                query.setParameter("created_by", createdBy);
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

    @SneakyThrows
    @Override
    public long count(Pageable pageable) {
        StringBuilder sql = new StringBuilder();
        Map<String, String> searchMap = null;
        String id = null;
        String createdAtFrom = null;
        String createdAtTo = null;
        String status = null;
        String isDeleted = null;
        String timeRemaining = null;
        String createdBy = null;
        if (pageable.getSearchData() != null) {
            searchMap = DaoUtils.getSearchDataFromParam(pageable.getSearchData());
        }

        sql.append("select count(o.id) " +
                " from orders o ");
        sql.append(" WHERE o.id is not null ");

        if (searchMap != null) {
            id = searchMap.get("id");
            if (id != null) {
                sql.append(" AND o.id = :id ");
            }

            createdAtFrom = searchMap.get("created_at_from");
            if (createdAtFrom != null) {
                sql.append(" AND o.created_at >= :createdAtFrom ");
            }

            createdAtTo = searchMap.get("created_at_to");
            if (createdAtTo != null) {
                sql.append(" AND o.created_at <= :createdAtTo ");
            }
            timeRemaining = searchMap.get("time_remaining");
            if (timeRemaining != null) {
                sql.append(" AND o.time_remaining >= :timeRemaining ");
            }
            status = searchMap.get("status");
            if (status != null) {
                sql.append(" AND o.status = :status ");
            }

            isDeleted = searchMap.get("is_deleted");
            if (isDeleted != null) {
                sql.append(" AND o.is_deleted = :is_deleted");
            }
            createdBy = searchMap.get("created_by");
            if (createdBy != null) {
                sql.append(" AND o.created_by = :created_by");
            }

        }

        Query query = entityManager.createNativeQuery(sql.toString());

        if (searchMap != null) {
            if (id != null) {
                query.setParameter("id", Long.valueOf(id));
            }
            if (createdAtFrom != null) {
                query.setParameter("createdAtFrom", simpleDateFormat.parse(createdAtFrom));
            }
            if (timeRemaining != null) {
                query.setParameter("timeRemaining", simpleDateFormat.parse(timeRemaining));
            }
            if (createdAtTo != null) {
                query.setParameter("createdAtTo", simpleDateFormat.parse(createdAtTo));
            }
            if (status != null) {
                query.setParameter("status", status);
            }
            if (isDeleted != null) {
                query.setParameter("is_deleted", Boolean.parseBoolean(isDeleted));
            }
            if (createdBy != null) {
                query.setParameter("created_by", createdBy);
            }
        }

        return Long.parseLong(query.getSingleResult().toString());
    }


    public Map<String, Object> getTotalBill(String id) {
        Map<String, Object> resMap = new HashMap<>();

        StringBuilder sql = new StringBuilder();

        sql.append("select o.id, " +
                "       o.created_at, " +
                "       o.created_by, " +
                "       o.is_deleted, " +
                "       o.status, " +
                "       s.name as store, " +
                "       s.address, " +
                "       p.name as product, " +
                "       max(od.quantity) as quantity, " +
                "       (max(od.quantity) * ps.value) as total," +
                "       pt.photo as photo," +
                "       od.description as description"
        );
        sql.append(" from orders o");
        sql.append(" left join (select order_id, product_id, status, sum(quantity) as quantity, STRING_AGG(concat(created_by, '-', description), ';') as description");
        sql.append(" from order_detail group by order_id,product_id,status) od on o.id = od.order_id");
        sql.append(" left join products p on od.product_id = p.id");
        sql.append(" left join stores s on o.store_id = s.id");
        sql.append(" left join (select product_id, max(value) as photo from photos group by product_id) as pt on p.id = pt.product_id");
        sql.append(" left join prices ps on ps.id =");
        sql.append(" CASE WHEN p.price_discount_id IS NOT NULL THEN p.price_discount_id");
        sql.append(" ELSE p.price_id END");
        sql.append(" where o.id= :id and od.status ='ACTIVATED' ");
        sql.append(" group by o.id, o.created_at, o.created_by, o.is_deleted, o.status , s.name, s.address, p.id, p.name,ps.value,pt.photo,od.description");


        Query query = entityManager.createNativeQuery(sql.toString(), "TotalBillMapping");
        query.setParameter("id", Long.valueOf(id));
        List<TotalBill> objects = query.getResultList();
        TotalBill totalAll = new TotalBill();
        List<AmountProduct> totalAmount = new ArrayList<>();
        BigDecimal total = new BigDecimal(0);
        for (TotalBill obj : objects) {
            AmountProduct amountProduct = new AmountProduct();
            if(obj.getProduct() != null) {
                amountProduct.setProduct(obj.getProduct());
                amountProduct.setQuantity(obj.getQuantity());
                amountProduct.setTotal(obj.getTotal());
                amountProduct.setPhoto(obj.getPhoto());
                amountProduct.setDescription(obj.getDescription());
            }
            totalAmount.add(amountProduct);
            totalAll.setId(obj.getId());
            totalAll.setCreatedAt(obj.getCreatedAt());
            totalAll.setCreatedBy(obj.getCreatedBy());
            totalAll.setDeleted(obj.getDeleted());
            totalAll.setStatus(obj.getStatus());
            totalAll.setStore(obj.getStore());
            totalAll.setAddress(obj.getAddress());
            totalAll.setTotalProduct(totalAmount);
            if(obj.getTotal() != null){
                total = total.add(obj.getTotal());
            }
        }
        totalAll.setTotalAll(total);
        resMap.put("data", totalAll);

        return resMap;
    }

    public List<Order> findByTimeRemainingOut(LocalDateTime localDateTime) {

        StringBuilder sql = new StringBuilder();

        sql.append("select o.id, " +
                "       o.status, " +
                "       o.created_by, " +
                "       o.modified_by, " +
                "       o.created_at, " +
                "       o.modified_at, " +
                "       o.is_deleted, " +
                "       s.id as store_id, " +
                "       s.name as store_name, " +
                "       o.time_remaining, " +
                "       s.img, " +
                "       s.address " +
                "   from orders o " +
                "         inner join stores s on o.store_id = s.id ");
        sql.append(" WHERE DATEDIFF(MINUTE, :strDate, o.time_remaining) <= 5 and DATEDIFF(MINUTE, :strDate, o.time_remaining) > 0 ");

        Query query = entityManager.createNativeQuery(sql.toString(), "OrderMapping");
        query.setParameter("strDate", localDateTime);
        List<Order> orderList = query.getResultList();

        return orderList;
    }

    public List<Order> findCreateByOrder(LocalDateTime localDateTime) {

        StringBuilder sql = new StringBuilder();

        sql.append("select o.id, " +
                "       o.status, " +
                "       o.created_by, " +
                "       o.modified_by, " +
                "       o.created_at, " +
                "       o.modified_at, " +
                "       o.is_deleted, " +
                "       s.id as store_id, " +
                "       s.name as store_name, " +
                "       o.time_remaining, " +
                "       s.img, " +
                "       s.address " +
                "   from orders o " +
                "         inner join stores s on o.store_id = s.id ");
        sql.append(" WHERE DATEDIFF(MINUTE, :strDate, o.time_remaining) = 0 and DATEDIFF(MINUTE, :strDate, o.time_remaining) > -2  ");

        Query query = entityManager.createNativeQuery(sql.toString(), "OrderMapping");
        query.setParameter("strDate", localDateTime);
        List<Order> orderList = query.getResultList();

        return orderList;
    }

}
