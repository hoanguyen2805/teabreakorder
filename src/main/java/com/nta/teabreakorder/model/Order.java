package com.nta.teabreakorder.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nta.teabreakorder.common.Const;
import com.nta.teabreakorder.config.AuditingModel;
import com.nta.teabreakorder.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
@SqlResultSetMapping(
        name = "OrderMapping",
        classes = {
                @ConstructorResult(
                        targetClass = com.nta.teabreakorder.model.Order.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "status", type = String.class),
                                @ColumnResult(name = "created_by", type = String.class),
                                @ColumnResult(name = "modified_by", type = String.class),
                                @ColumnResult(name = "created_at", type = LocalDateTime.class),
                                @ColumnResult(name = "modified_at", type = LocalDateTime.class),
                                @ColumnResult(name = "is_deleted", type = Boolean.class),
                                @ColumnResult(name = "store_id", type = Long.class),
                                @ColumnResult(name = "store_name", type = String.class),
                                @ColumnResult(name = "time_remaining", type = LocalDateTime.class),
                                @ColumnResult(name = "img", type = String.class),
                                @ColumnResult(name = "address", type = String.class),
                        }
                )
        }

)
public class Order extends AuditingModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    @Column(name = "status")
    @JsonProperty("status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Store store;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private List<OrderDetail> orderDetailList = new ArrayList<>();

    @JsonFormat(pattern = Const.DATETIME_PATTERN)
    @JsonProperty(value = "time_remaining")
    @Column(name = "time_remaining")
    private LocalDateTime timeRemaining;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;

    @PreUpdate
    private void preUpdate() {
        this.getOrderDetailList().forEach(item -> item.setOrder(this));
    }

    @PreRemove
    public void preRemove() {
        this.getOrderDetailList().forEach(item -> item.beforeRemove());
    }

    public Order(Long id, String status, String createdBy, String modifiedBy, LocalDateTime createdAt, LocalDateTime modifiedAt, boolean isDeleted, LocalDateTime timeRemaining) {
        super(createdBy, createdAt, modifiedBy, modifiedAt, isDeleted);
        this.id = id;
        this.status = Status.valueOf(status);
        this.timeRemaining = timeRemaining;
    }

    public Order(Long id, String status, String createdBy, String modifiedBy, LocalDateTime createdAt, LocalDateTime modifiedAt, boolean isDeleted, Long storeId, String storeName, LocalDateTime timeRemaining, String img, String address) {
        super(createdBy, createdAt, modifiedBy, modifiedAt, isDeleted);
        this.id = id;
        this.status = Status.valueOf(status);
        this.store = new Store(storeId, storeName, img, address);
        this.timeRemaining = timeRemaining;
    }

    public Order(Long id, Status status, Store store, List<OrderDetail> orderDetailList, LocalDateTime timeRemaining, User user) {
        this.id = id;
        this.status = status;
        this.store = store;
        this.orderDetailList = orderDetailList;
        this.timeRemaining = timeRemaining;
        this.user = user;
    }

    public Order(String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt, boolean isDeleted, Long id, Status status, Store store, List<OrderDetail> orderDetailList, LocalDateTime timeRemaining, User user) {
        super(createdBy, createdAt, modifiedBy, modifiedAt, isDeleted);
        this.id = id;
        this.status = status;
        this.store = store;
        this.orderDetailList = orderDetailList;
        this.timeRemaining = timeRemaining;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = new User(user.getId(), user.getUsername(), user.getBankingPaymentInfoList());
    }
}
